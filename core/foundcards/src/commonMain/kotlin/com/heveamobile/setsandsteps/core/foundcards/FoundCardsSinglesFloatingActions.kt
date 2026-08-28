package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_reveal_all_button
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_reveal_button
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_skip_button
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SinglesFloatingActions(
    singlesState: SinglesUiState,
    onAction: (FoundCardsAction) -> Unit,
    bottomPadding: Dp,
) {
    val foundCards = singlesState.foundCards
    val showRevealAllButton = foundCards.any { !it.isRevealed }

    AnimatedVisibility(
        visible = showRevealAllButton,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
                .padding(bottom = MaterialTheme.spacing.small),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val buttonTextResId = if (!singlesState.isRevealingAll) {
                if (foundCards.size > 1) Res.string.overlay_reveal_all_button else Res.string.overlay_reveal_button
            } else {
                Res.string.overlay_skip_button
            }
            Button(
                onClick = {
                    if (!singlesState.isRevealingAll) {
                        onAction(FoundCardsAction.Singles.RevealAllCards)
                    } else {
                        onAction(FoundCardsAction.Singles.SkipRevealingAllCards)
                    }
                },
            ) {
                Text(text = stringResource(buttonTextResId))
            }
        }
    }
}
