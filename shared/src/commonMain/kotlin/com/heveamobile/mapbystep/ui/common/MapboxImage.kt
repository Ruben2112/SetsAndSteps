package com.heveamobile.mapbystep.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.heveamobile.mapbystep.domain.model.Info
import com.heveamobile.mapbystep.domain.model.Rarity
import com.heveamobile.mapbystep.encodeUrl
import com.heveamobile.mapbystep.theme.color
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.theme.toHex
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.map_image_description
import mapbystep.shared.generated.resources.mapbox_access_token
import mapbystep.shared.generated.resources.mapbox_image_loading_failed
import mapbystep.shared.generated.resources.mapbox_style_id_dark
import mapbystep.shared.generated.resources.mapbox_style_id_light
import mapbystep.shared.generated.resources.mapbox_username
import mapbystep.shared.generated.resources.warning_card_icon_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun MapboxImage(
    modifier: Modifier = Modifier,
    countryInfo: Info.CountryInfo,
    rarity: Rarity,
    size: IntSize,
) {
    val context = LocalPlatformContext.current

    /** These string resources are kept in a file ignored by git to prevent leaking it into the
     * public repository. Use commented out variables below instead to disable static mapbox image.
     **/
    val token = stringResource(Res.string.mapbox_access_token)
    val styleId = stringResource(
        if (isSystemInDarkTheme()) Res.string.mapbox_style_id_dark else Res.string.mapbox_style_id_light,
    )
    val username = stringResource(Res.string.mapbox_username)

//    val token = ""
//    val styleId = ""
//    val username = ""

    val lonMin = countryInfo.boundingBox[0]
    val latMin = countryInfo.boundingBox[1]
    val lonMax = countryInfo.boundingBox[2]
    val latMax = countryInfo.boundingBox[3]

    val highlightColor = rarity
        .color(MaterialTheme.colorScheme.onPrimary)
        .toHex()
    val padding = 50

    val fillOverlay =
        ("{\"id\":\"country-highlight\",\"type\":\"fill\",\"source\":{\"type\":\"vector\",\"url\":\"mapbox://mapbox.country-boundaries-v1\"},\"source-layer\":\"country_boundaries\",\"filter\":[\"==\",\"iso_3166_1\",\"${countryInfo.cca2}\"],\"paint\":{\"fill-color\":\"$highlightColor\",\"fill-opacity\":1.0}}").trimIndent()
    val encodedFill = encodeUrl(fillOverlay)

    val imageUrl =
        "https://api.mapbox.com/styles/v1/$username/$styleId/static/[$lonMin,$latMin,$lonMax,$latMax]/${size.width}x${size.height}?padding=$padding,$padding,$padding&addlayer=$encodedFill&access_token=$token"
    val imageRequest = ImageRequest
        .Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .build()

    LaunchedEffect(imageUrl) {
        // Preload the image so the user is less likely to see the loading state
        SingletonImageLoader
            .get(context)
            .enqueue(imageRequest)
    }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = imageRequest,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = rarity.color(MaterialTheme.colorScheme.onPrimary),
                    strokeWidth = 2.dp,
                )
            }
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Rounded.Warning,
                        contentDescription = stringResource(Res.string.warning_card_icon_description),
                        modifier = Modifier.size(24.dp),
                        tint = rarity.color(MaterialTheme.colorScheme.onPrimary),
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        stringResource(Res.string.mapbox_image_loading_failed),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(
            Res.string.map_image_description,
            countryInfo.cca2,
        ),
    )
}