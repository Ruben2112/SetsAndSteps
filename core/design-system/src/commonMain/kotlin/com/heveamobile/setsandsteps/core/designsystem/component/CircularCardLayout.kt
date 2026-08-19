package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.min
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.FoundCard
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularPackLayout(
    modifier: Modifier = Modifier,
    cards: List<FoundCard>,
    onCardClick: (CollectableCard) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val density = LocalDensity.current
        val cardWidth = min(
            maxWidth,
            maxHeight,
        ) * 0.25F
        val radius = min(
            maxWidth,
            maxHeight,
        ) * 0.32F
        val cardCount = cards.size

        cards.forEachIndexed { index, foundCard ->
            // Anchor the third card (index 2) at 90deg (6 o'clock) and space the rest evenly around it.
            val angleRadians = (90.0 + (360.0 / cardCount) * (index - 2)) * (kotlin.math.PI / 180.0)
            val offsetX = with(density) { (radius.toPx() * cos(angleRadians)).toInt() }
            val offsetY = with(density) { (radius.toPx() * sin(angleRadians)).toInt() }

            CollectableCardLayout(
                modifier = Modifier
                    .width(cardWidth)
                    .offset {
                        IntOffset(
                            offsetX,
                            offsetY,
                        )
                    },
                backsideImageUrl = foundCard.cardSet.backsideImageUrl,
                card = foundCard.card,
                isRevealed = foundCard.isRevealed,
                isLarge = false,
                isNew = foundCard.isNew,
                mapPointsGained = foundCard.setPointsGained,
                onClick = { onCardClick(foundCard.card) },
            )
        }
    }
}
