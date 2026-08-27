package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.card_image_description
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.card_image_loading_failed
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.card_new
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.card_set_image_description
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.ic_question_mark
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.unrevealed_card_icon_description
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.warning_card_icon_description
import com.heveamobile.setsandsteps.core.designsystem.theme.color
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun CollectableCardLayout(
    modifier: Modifier = Modifier,
    backsideImageUrl: String?,
    card: CollectableCard,
    isRevealed: Boolean,
    isNew: Boolean = false,
    onClick: (() -> Unit)? = null,
    isLarge: Boolean = false,
    mapPointsGained: Int = 0,
) {
    val rotationAngle = 180F + (card.rarity.intValue - 1) * 360F
    val rotation = animateFloatAsState(
        targetValue = if (isRevealed) 0F else rotationAngle,
        animationSpec = tween(
            durationMillis = card.animationTime.toInt(),
            easing = LinearEasing,
        ),
    )

    val currentRarityInt = (card.rarity.intValue - (rotation.value / 360f).roundToInt()).coerceIn(
        1,
        card.rarity.intValue,
    )
    val currentRarity = Rarity.fromInt(currentRarityInt)
    val animatedCard = card.copy(rarity = currentRarity)

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 16F * density
            }
            .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
            .clickable {
                onClick?.invoke()
            },
    ) {
        Box(
            modifier = Modifier.graphicsLayer {
                val normalizedRotation = (rotation.value % 360F + 360F) % 360F
                alpha = if (normalizedRotation <= 90F || normalizedRotation >= 270F) 1F else 0F
            },
        ) {
            CardFront(
                isLarge = isLarge,
                isNew = isNew,
                mapPointsGained = mapPointsGained,
                card = animatedCard,
                isFinalRarity = animatedCard.rarity == card.rarity,
            )
        }
        Box(
            modifier = Modifier.graphicsLayer {
                rotationY = 180F
                val normalizedRotation = (rotation.value % 360F + 360F) % 360F
                alpha = if (normalizedRotation > 90F && normalizedRotation < 270F) 1F else 0F
            },
        ) {
            CardBack(
                isLarge = isLarge,
                backsideImageUrl = backsideImageUrl,
            )
        }
    }
}

private fun monochromeColorFilter(tint: Color): ColorFilter {
    // Standard luminance weights (Rec. 601 style)
    val lumR = 0.299f
    val lumG = 0.587f
    val lumB = 0.114f

    val matrix = ColorMatrix(
        floatArrayOf(
            lumR * tint.red,
            lumG * tint.red,
            lumB * tint.red,
            0f,
            0f,
            lumR * tint.green,
            lumG * tint.green,
            lumB * tint.green,
            0f,
            0f,
            lumR * tint.blue,
            lumG * tint.blue,
            lumB * tint.blue,
            0f,
            0f,
            0f,
            0f,
            0f,
            1f,
            0f,
        ),
    )
    return ColorFilter.colorMatrix(matrix)
}

@OptIn(InternalResourceApi::class)
@Composable
private fun CardFront(
    modifier: Modifier = Modifier,
    isLarge: Boolean,
    isNew: Boolean = false,
    mapPointsGained: Int,
    card: CollectableCard,
    isFinalRarity: Boolean,
) {
    val userData = card.userData
    if (userData == null || !userData.isDiscovered) return

    Box(
        modifier = modifier.aspectRatio(
            5F / 7F,
            matchHeightConstraintsFirst = true,
        ),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
                .border(
                    width = if (isLarge) 4.dp else 1.dp,
                    color = card.rarity.color(MaterialTheme.colorScheme.onSurface),
                    shape = if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
                )
                .background(
                    if (userData.findCount > 0) MaterialTheme.colorScheme.surfaceContainer
                    else MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1F),
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val context = LocalPlatformContext.current
                    val imageUrl = card.imageUrl
                    val imageRequest = ImageRequest
                        .Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .size(512)
                        .build()

                    SubcomposeAsyncImage(
                        modifier = modifier
                            .fillMaxSize()
                            .aspectRatio(1F),
                        model = imageRequest,
                        colorFilter = if (!isFinalRarity) {
                            monochromeColorFilter(card.rarity.color(MaterialTheme.colorScheme.onSurface))
                        } else null,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = card.rarity.color(MaterialTheme.colorScheme.onSurface),
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
                                        tint = card.rarity.color(MaterialTheme.colorScheme.onSurface),
                                    )
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                    Text(
                                        stringResource(Res.string.card_image_loading_failed),
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        },
                        contentScale = ContentScale.Fit,
                        contentDescription = stringResource(
                            Res.string.card_image_description,
                            card.name,
                        ),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isLarge) 4.dp else 1.dp)
                    .background(card.rarity.color(MaterialTheme.colorScheme.onSurface)),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = if (isLarge) MaterialTheme.spacing.large else MaterialTheme.spacing.small,
                        vertical = MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = card.name,
                    textAlign = TextAlign.Center,
                    style = if (isLarge) MaterialTheme.typography.bodyLarge.copy(card.rarity.color(MaterialTheme.colorScheme.onSurface)) else MaterialTheme.typography.bodySmall.copy(card.rarity.color(MaterialTheme.colorScheme.onSurface)),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (isNew) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        if (isLarge) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.TopEnd,
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = if (isLarge) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
                            vertical = if (isLarge) MaterialTheme.spacing.small else MaterialTheme.spacing.extraSmall,
                        ),
                        text = stringResource(Res.string.card_new),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
        if (isLarge && mapPointsGained > 0) {
            Box(
                modifier = Modifier
                    .aspectRatio(1F)
                    .padding(
                        if (isLarge) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small,
                        ),
                        text = formatAmount(
                            mapPointsGained,
                            formatMode = FormatMode.Long,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun CardBack(
    modifier: Modifier = Modifier,
    isLarge: Boolean,
    backsideImageUrl: String?,
) {
    if (backsideImageUrl == null) {
        Icon(
            painter = painterResource(resource = Res.drawable.ic_question_mark),
            contentDescription = stringResource(Res.string.unrevealed_card_icon_description),
            modifier = modifier
                .aspectRatio(
                    5F / 7F,
                    matchHeightConstraintsFirst = true,
                )
                .fillMaxSize()
                .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
                .border(
                    width = if (isLarge) 4.dp else 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        val context = LocalPlatformContext.current
        val imageRequest = ImageRequest
            .Builder(context)
            .data(backsideImageUrl)
            .crossfade(true)
            .build()

        SubcomposeAsyncImage(
            modifier = modifier
                .aspectRatio(
                    5F / 7F,
                    matchHeightConstraintsFirst = true,
                )
                .fillMaxSize()
                .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
                .border(
                    width = if (isLarge) 4.dp else 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            model = imageRequest,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
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
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Text(
                            stringResource(Res.string.card_image_loading_failed),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            },
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(Res.string.card_set_image_description),
        )
    }
}

val CollectableCard.animationTime: Float
    get() = ((rarity.intValue - 0.5F) * (400 * 2))