package com.heveamobile.setsandsteps.feature.sets.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.KeyValueRow
import com.heveamobile.setsandsteps.core.designsystem.component.PrimaryButton
import com.heveamobile.setsandsteps.core.designsystem.component.SecondaryButton
import com.heveamobile.setsandsteps.core.designsystem.component.SetStatisticsList
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CardSetDownloadState
import com.heveamobile.setsandsteps.core.navigation.icons.ic_catalog
import com.heveamobile.setsandsteps.core.navigation.icons.ic_sets
import com.heveamobile.setsandsteps.core.presentation.LocalBottomBarState
import com.heveamobile.setsandsteps.core.presentation.LocalDrawerState
import com.heveamobile.setsandsteps.core.presentation.LocalSnackbarHostState
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.Res
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_activate
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_catalog_empty_state
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_deactivate
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_download
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_download_failed
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_level
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_my_sets_list_footer
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_purchase_successful
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_steps_per_pack
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_steps_to_next_pack
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_tab_catalog
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_tab_my_sets
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_total_findings
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_update
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_update_available_icon_description
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_update_failed
import com.heveamobile.setsandsteps.feature.sets.presentation.generated.resources.sets_updating
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetsScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SetsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val bottomBarState = LocalBottomBarState.current
    val onAction = rememberUpdatedState(viewModel::onAction)
    val currentState = rememberUpdatedState(state)

    DisposableEffect(Unit) {
        bottomBarState.content.value = {
            SetsTabBar(
                selectedTab = currentState.value.selectedTab,
                onAction = onAction.value,
            )
        }
        onDispose {
            bottomBarState.content.value = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is SetsEvent.PurchaseSucceeded -> snackbarHostState.showSnackbar(
                    getString(Res.string.sets_purchase_successful),
                )

                is SetsEvent.DownloadFailed -> snackbarHostState.showSnackbar(
                    getString(
                        Res.string.sets_download_failed,
                        event.setName,
                    ),
                )

                is SetsEvent.UpdateFailed -> snackbarHostState.showSnackbar(
                    getString(
                        Res.string.sets_update_failed,
                        event.setName,
                    ),
                )
            }
        }
    }

    SetsContent(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun SetsContent(
    state: SetsState,
    onAction: (SetsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.ordinal,
        pageCount = { SetsTab.entries.size },
    )

    LaunchedEffect(state.selectedTab) {
        if (pagerState.currentPage != state.selectedTab.ordinal) {
            pagerState.animateScrollToPage(state.selectedTab.ordinal)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAction(SetsAction.SelectTab(SetsTab.entries[page]))
        }
    }

    val drawerState = LocalDrawerState.current
    val scope = rememberCoroutineScope()

    val nestedScrollConnection = remember(
        pagerState,
        drawerState,
        scope,
    ) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                // If we're on the first page and swiping right, we let the parent handle it
                return if (pagerState.currentPage == 0 && available.x > 0) {
                    Offset.Zero
                } else {
                    super.onPreScroll(
                        available,
                        source,
                    )
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                // If there's remaining delta after the pager (and others) handled the scroll,
                // and we're on the first page swiping right, we open the drawer.
                if (pagerState.currentPage == 0 && available.x > 0 && source == NestedScrollSource.UserInput) {
                    scope.launch {
                        drawerState.open()
                    }
                }
                return super.onPostScroll(
                    consumed,
                    available,
                    source,
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) { page ->
        val tab = SetsTab.entries[page]
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            when (tab) {
                SetsTab.MySets -> {
                    items(
                        items = state.sets,
                        key = { it.id },
                    ) { set ->
                        OwnedSetCard(
                            set = set,
                            state = state,
                            onAction = onAction,
                        )
                    }
                    if (!state.catalogSets.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(Res.string.sets_my_sets_list_footer),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                SetsTab.Catalog -> {
                    if (state.catalogSets.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(MaterialTheme.spacing.medium),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(Res.string.sets_catalog_empty_state),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    } else {
                        items(
                            items = state.catalogSets,
                            key = { it.id },
                        ) { set ->
                            CatalogSetCard(
                                set = set,
                                onAction = onAction,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetsTabBar(
    selectedTab: SetsTab,
    onAction: (SetsAction) -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == SetsTab.MySets,
            onClick = { onAction(SetsAction.SelectTab(SetsTab.MySets)) },
            icon = {
                Icon(
                    imageVector = ic_sets,
                    contentDescription = null,
                )
            },
            label = { Text(stringResource(Res.string.sets_tab_my_sets)) },
        )
        NavigationBarItem(
            selected = selectedTab == SetsTab.Catalog,
            onClick = { onAction(SetsAction.SelectTab(SetsTab.Catalog)) },
            icon = {
                Icon(
                    imageVector = ic_catalog,
                    contentDescription = null,
                )
            },
            label = { Text(stringResource(Res.string.sets_tab_catalog)) },
        )
    }
}

@Composable
private fun OwnedSetCard(
    set: CardSet,
    state: SetsState,
    onAction: (SetsAction) -> Unit,
) {
    val userData = set.userData
        ?: return
    val downloadState = state.downloadStates[set.id]
    val updateAvailable = set.id in state.updateAvailableSetIds

    Card(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onAction(
                    SetsAction.ExpandProgress(set),
                )
            },
        title = set.name,
        subtitle = stringResource(
            Res.string.sets_level,
            userData.currentLevel,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                value = formatAmount(
                    userData.currentSteps,
                    FormatMode.Long,
                ) + " / " + formatAmount(
                    userData.calculatedDistance,
                    FormatMode.Long,
                ),
                modifier = Modifier.padding(end = MaterialTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.sets_steps_to_next_pack),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            KeyValueRow(
                value = set.cards
                    .sumOf {
                        it.userData?.findCount
                            ?: 0
                    }
                    .toString(),
                modifier = Modifier.padding(end = MaterialTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.sets_total_findings),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            SetStatisticsList(
                set = set,
                isExpanded = state.expandedSetId == set.id,
            )
            val needsAttention =
                updateAvailable || downloadState == CardSetDownloadState.Failed || (set.cards.isEmpty() && downloadState != CardSetDownloadState.InProgress)

            if (needsAttention || downloadState == CardSetDownloadState.InProgress) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    if (downloadState == CardSetDownloadState.InProgress) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                        )
                        Text(
                            text = stringResource(Res.string.sets_updating),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = stringResource(Res.string.sets_update_available_icon_description),
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Spacer(modifier = Modifier.weight(1F))
                        SecondaryButton(label = stringResource(Res.string.sets_update)) {
                            onAction(SetsAction.UpdateSet(set.id))
                        }
                    }
                }
            }
            if (set.cards.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    PrimaryButton(
                        label = stringResource(
                            if (userData.isActive) Res.string.sets_deactivate else Res.string.sets_activate,
                        ),
                    ) {
                        onAction(SetsAction.ToggleActiveState(set))
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogSetCard(
    set: CardSet,
    onAction: (SetsAction) -> Unit,
) {
    Card(
        title = set.name,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                value = formatAmount(
                    set.baseDistance,
                    FormatMode.Long,
                ),
                modifier = Modifier.padding(end = MaterialTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.sets_steps_per_pack),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            KeyValueRow(
                value = set.cards
                    .sumOf {
                        it.userData?.findCount
                            ?: 0
                    }
                    .toString(),
                modifier = Modifier.padding(end = MaterialTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.sets_total_findings),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            SetStatisticsList(
                set = set,
                isExpanded = true,
                showExpandIcon = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                PrimaryButton(label = stringResource(Res.string.sets_download)) {
                    onAction(SetsAction.DownloadSet(set.id))
                }
            }
        }
    }
}
