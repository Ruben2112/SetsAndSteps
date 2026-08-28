package com.heveamobile.setsandsteps.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heveamobile.setsandsteps.core.designsystem.theme.color
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.model.Rarity

@Composable
fun CardText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
    ) {
        text.forEach {
            Box(
                modifier = Modifier
                    .aspectRatio(5F / 7F)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .border(
                        width = 2.dp,
                        color = Rarity.Common.color(MaterialTheme.colorScheme.onSurface),
                        shape = MaterialTheme.shapes.extraSmall,
                    ),
            ) {
                Column {
                    Text(
                        text = it.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.aspectRatio(1F),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = fontSize,
                        ),
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp,
                        color = Rarity.Common.color(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardTextPreview() {
    CardText(
        text = "SETS&",
        fontSize = 40.sp,
    )
}