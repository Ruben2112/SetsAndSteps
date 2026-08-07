package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.component.CircularPackLayout
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PackOpeningPager(
    modifier: Modifier = Modifier,
    packOpeningState: PackOpeningUiState,
    onAction: (FoundCardsAction) -> Unit,
) {
    val setPages = packOpeningState.setPages
    val parentPagerState = rememberPagerState { setPages.size }
    val coroutineScope = rememberCoroutineScope()

    var revealTarget by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    LaunchedEffect(packOpeningState.isRevealing) {
        if (packOpeningState.isRevealing) {
            val target = packOpeningState.firstUnrevealedPosition
            revealTarget = target
            if (target != null && parentPagerState.settledPage != target.first) {
                parentPagerState.animateScrollToPage(target.first)
            }
        }
    }

    val currentSetAllRevealed =
        setPages.getOrNull(parentPagerState.settledPage)?.allRevealed == true
    LaunchedEffect(
        packOpeningState.isRevealing,
        parentPagerState.settledPage,
        currentSetAllRevealed,
    ) {
        val target = awaitAutoAdvanceTarget(
            isRevealing = packOpeningState.isRevealing,
            allRevealedOnCurrentPage = currentSetAllRevealed,
            targetPage = packOpeningState.firstUnrevealedPosition
                ?.takeIf { it.first != parentPagerState.settledPage }
                ?.first,
        )
        if (target != null) {
            coroutineScope.launch { parentPagerState.animateScrollToPage(target) }
        }
    }

    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = parentPagerState,
        // Limits a single fling to advance at most one page so a fast fling can't skip
        // past a set the visibility-reporting/auto-advance logic hasn't caught up with.
        flingBehavior = PagerDefaults.flingBehavior(
            state = parentPagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(1),
        ),
    ) { parentPage ->
        SetPage(
            setIndex = parentPage,
            packOpeningState = packOpeningState,
            isActiveSet = parentPagerState.settledPage == parentPage,
            revealTargetPackIndex = revealTarget?.takeIf { it.first == parentPage }?.second,
            onAction = onAction,
        )
    }
}

/**
 * Once the page currently settled on is fully revealed, waits out the suspense delay and
 * returns [targetPage] to scroll to next, or null if nothing should happen. Left to run inside
 * the caller's `LaunchedEffect` so it's cancelled if that effect's keys change during the delay.
 */
private suspend fun awaitAutoAdvanceTarget(
    isRevealing: Boolean,
    allRevealedOnCurrentPage: Boolean,
    targetPage: Int?,
): Int? {
    if (isRevealing && allRevealedOnCurrentPage && targetPage != null) {
        delay(1000)
        return targetPage
    }
    return null
}

@Composable
private fun SetPage(
    setIndex: Int,
    packOpeningState: PackOpeningUiState,
    isActiveSet: Boolean,
    revealTargetPackIndex: Int?,
    onAction: (FoundCardsAction) -> Unit,
) {
    val isRevealing = packOpeningState.isRevealing
    val setPageUiState = packOpeningState.setPages[setIndex]
    val packs = setPageUiState.packs
    val childPagerState = rememberPagerState(pageCount = { packs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        isActiveSet,
        revealTargetPackIndex,
    ) {
        if (isActiveSet &&
            revealTargetPackIndex != null &&
            childPagerState.settledPage != revealTargetPackIndex
        ) {
            childPagerState.animateScrollToPage(revealTargetPackIndex)
        }
    }
    LaunchedEffect(
        isActiveSet,
        childPagerState.settledPage,
    ) {
        if (isActiveSet) {
            onAction(
                FoundCardsAction.UpdateVisiblePack(
                    setIndex,
                    childPagerState.settledPage,
                ),
            )
        }
    }

    val currentPackAllRevealed = packs.getOrNull(childPagerState.settledPage)?.allRevealed == true
    LaunchedEffect(
        isRevealing,
        childPagerState.settledPage,
        currentPackAllRevealed,
    ) {
        val target = awaitAutoAdvanceTarget(
            isRevealing = isRevealing,
            allRevealedOnCurrentPage = currentPackAllRevealed,
            targetPage = packOpeningState.firstUnrevealedPosition
                ?.takeIf { it.first == setIndex && it.second != childPagerState.settledPage }
                ?.second,
        )
        if (target != null) {
            coroutineScope.launch { childPagerState.animateScrollToPage(target) }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SetHeader(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            setName = setPageUiState.cardSet.name,
            packProgressValue = "${childPagerState.settledPage + 1} / ${packs.size}",
            newCardsCount = setPageUiState.newCardsCount,
            pointsGained = setPageUiState.pointsRevealedSoFar,
        )
        HorizontalPager(
            modifier = Modifier
                .weight(1F)
                .padding(bottom = MaterialTheme.spacing.extraLarge + MaterialTheme.spacing.large),
            state = childPagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = childPagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(1),
            ),
        ) { packPage ->
            PackPage(
                setIndex = setIndex,
                packIndex = packPage,
                packUiState = packs[packPage],
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun PackPage(
    setIndex: Int,
    packIndex: Int,
    packUiState: PackUiState,
    onAction: (FoundCardsAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CircularPackLayout(
            modifier = Modifier.fillMaxSize(),
            cards = packUiState.cards,
            onCardClick = { card ->
                val found = packUiState.cards.first { it.card == card }
                if (found.isRevealed) {
                    onAction(FoundCardsAction.ToggleCardInfo(card))
                } else {
                    onAction(
                        FoundCardsAction.RevealPackCard(
                            setIndex,
                            packIndex,
                            card,
                        ),
                    )
                }
            },
        )
    }
}
