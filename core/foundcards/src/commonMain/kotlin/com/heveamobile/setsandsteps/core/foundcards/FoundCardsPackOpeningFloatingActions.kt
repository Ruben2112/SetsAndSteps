package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_hold_to_reveal_button
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_show_summary_button
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PackOpeningFloatingActions(
    packOpeningState: PackOpeningUiState,
    onAction: (FoundCardsAction) -> Unit,
    bottomPadding: Dp,
) {
    AnimatedVisibility(
        visible = !packOpeningState.showSummaryPage,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
                .padding(MaterialTheme.spacing.medium),
            contentAlignment = Alignment.BottomEnd,
        ) {
            val holdInteractionSource = remember { MutableInteractionSource() }
            val isHeld by holdInteractionSource.collectIsPressedAsState()
            LaunchedEffect(isHeld) {
                onAction(
                    if (isHeld) {
                        FoundCardsAction.PackOpening.StartRevealing
                    } else {
                        FoundCardsAction.PackOpening.StopRevealing
                    },
                )
            }
            Button(
                colors = ButtonDefaults
                    .buttonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                interactionSource = holdInteractionSource,
                onClick = {},
            ) {
                Text(
                    text = stringResource(Res.string.overlay_hold_to_reveal_button),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                )
            }
        }
    }

    AnimatedVisibility(
        visible = packOpeningState.showSummaryPage && !packOpeningState.showSummaryScreen,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
                .padding(bottom = MaterialTheme.spacing.medium),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Button(
                colors = ButtonDefaults
                    .buttonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                onClick = { onAction(FoundCardsAction.PackOpening.ShowPackOpeningSummary) },
            ) {
                Text(
                    text = stringResource(Res.string.overlay_show_summary_button),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                )
            }
        }
    }
}
