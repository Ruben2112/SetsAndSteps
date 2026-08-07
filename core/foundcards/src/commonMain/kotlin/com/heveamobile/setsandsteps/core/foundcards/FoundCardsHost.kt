package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoundCardsHost(
    modifier: Modifier = Modifier,
    onVisibilityChanged: (Boolean) -> Unit = {},
) {
    val viewModel = koinViewModel<FoundCardsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isVisible) {
        onVisibilityChanged(state.isVisible)
    }

    AnimatedVisibility(
        visible = state.isVisible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300)),
    ) {
        FoundCardsOverlay(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
        )
    }
}
