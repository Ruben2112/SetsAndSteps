package com.heveamobile.setsandsteps.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.heveamobile.setsandsteps.core.domain.model.SortingOrder
import com.heveamobile.setsandsteps.core.navigation.DrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawer
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationHandler
import com.heveamobile.setsandsteps.core.navigation.Route
import com.heveamobile.setsandsteps.feature.profile.presentation.ProfileRoute
import com.heveamobile.setsandsteps.feature.settings.presentation.SettingsRoute
import com.heveamobile.setsandsteps.navigation.DestinationInfo
import com.heveamobile.setsandsteps.navigation.Destinations
import com.heveamobile.setsandsteps.navigation.SetPointExchange
import com.heveamobile.setsandsteps.navigation.Sets
import com.heveamobile.setsandsteps.core.presentation.LocalScaffoldPadding
import com.heveamobile.setsandsteps.core.presentation.LocalSnackbarHostState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.cards_hide_undiscovered
import setsandsteps.shared.generated.resources.cards_sort_alphabetically
import setsandsteps.shared.generated.resources.cards_sort_by_rarity
import setsandsteps.shared.generated.resources.cards_sort_by_visit_count
import setsandsteps.shared.generated.resources.checkmark_icon_description
import setsandsteps.shared.generated.resources.navigation_drawer_icon_description
import setsandsteps.shared.generated.resources.overflow_icon_description

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(
    KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val observer = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onAction(HomeAction.SyncSteps)
            }
        }
    }
    LocalLifecycleOwner.current.lifecycle.addObserver(observer)

    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.isClosed && state.isDrawerOpen) {
            onAction(HomeAction.CloseNavigationDrawer)
        } else if (drawerState.isOpen && !state.isDrawerOpen) {
            onAction(HomeAction.OpenNavigationDrawer)
        }
    }

    val navigationHandler = koinInject<NavigationHandler>()
    val entryProvider = koinEntryProvider<Any>()
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        ProfileRoute::class,
                        ProfileRoute.serializer(),
                    )
                    subclass(
                        Sets::class,
                        Sets.serializer(),
                    )
                    subclass(
                        Destinations::class,
                        Destinations.serializer(),
                    )
                    subclass(
                        DestinationInfo::class,
                        DestinationInfo.serializer(),
                    )
                    subclass(
                        SetPointExchange::class,
                        SetPointExchange.serializer(),
                    )
                    subclass(
                        SettingsRoute::class,
                        SettingsRoute.serializer(),
                    )
                }
            }
        },
        elements = arrayOf<NavKey>(
            ProfileRoute,
        ),
    )

    LaunchedEffect(Unit) {
        navigationHandler.navigationEvents.collect { route ->
            if (backStack.size > 1 && !(backStack.last() is Destinations && route is DestinationInfo)) {
                // Unless we are performing nested navigation, clear backstack until only first
                // screen (Profile) remains
                backStack
                    .subList(
                        1,
                        backStack.size,
                    )
                    .clear()
            }
            backStack.add(route)
        }
    }

    val focusManager = LocalFocusManager.current
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        ModalNavigationDrawer(
            modifier = Modifier
                .blur(
                    if (state.foundCardsState.foundCards.isEmpty()) 0.dp else 8.dp,
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        },
                    )
                },
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    onDrawerItemClicked = { route ->
                        onAction(HomeAction.CloseNavigationDrawer)
                        val navKey = when (route) {
                            NavigationDrawerRoute.Profile -> ProfileRoute
                            NavigationDrawerRoute.Sets -> Sets
                            NavigationDrawerRoute.Cards -> Destinations
                            NavigationDrawerRoute.CardDetails -> DestinationInfo(destinationId = null)
                            NavigationDrawerRoute.SetPointExchange -> SetPointExchange
                            NavigationDrawerRoute.Settings -> SettingsRoute
                        }
                        if (backStack.lastOrNull() != navKey) {
                            // Clear backstack until only first screen (Profile) remains
                            backStack
                                .subList(
                                    1,
                                    backStack.size,
                                )
                                .clear()
                        }
                        if (backStack.lastOrNull() != navKey && (navKey != ProfileRoute || backStack.lastOrNull() != ProfileRoute)) {
                            backStack.add(navKey)
                        }
                    },
                )
            },
            scrimColor = Color.Transparent,
        ) {
            Scaffold(
                modifier = modifier,
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData = snackbarData,
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                    )
                },
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults
                            .topAppBarColors()
                            .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        title = {
                            Text(
                                text = stringResource(
                                    (backStack.last() as DrawerRoute).navigationDrawerRoute.routeName,
                                ),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onAction(HomeAction.OpenNavigationDrawer)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = stringResource(Res.string.navigation_drawer_icon_description),
                                )
                            }
                        },
                        actions = {
                            StepProgressPill(
                                isLoading = state.isLoadingSteps,
                                availableSteps = state.availableSteps,
                                requiredSteps = state.requiredSteps,
                                onTap = {
                                    onAction(HomeAction.SpendSteps)
                                },
                            )
                            val currentBackStackEntry = backStack.lastOrNull()
                            if (currentBackStackEntry is Destinations) {
                                IconButton(onClick = { onAction(HomeAction.ToggleDropdownMenu) }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        contentDescription = stringResource(Res.string.overflow_icon_description),
                                    )
                                }
                                DropdownMenu(
                                    expanded = state.sharedCardsState.showDropdownMenu,
                                    onDismissRequest = {
                                        onAction(HomeAction.ToggleDropdownMenu)
                                    },
                                ) {
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            if (state.sharedCardsState.hideUndiscovered) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = stringResource(Res.string.checkmark_icon_description),
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                )
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = stringResource(Res.string.cards_hide_undiscovered),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        },
                                        onClick = {
                                            onAction(HomeAction.ToggleDropdownMenu)
                                            onAction(HomeAction.ToggleHideUndiscovered)
                                        },
                                    )
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            if (state.sharedCardsState.sortingOrder == SortingOrder.Rarity) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = stringResource(Res.string.checkmark_icon_description),
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                )
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = stringResource(Res.string.cards_sort_by_rarity),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        },
                                        onClick = {
                                            onAction(HomeAction.ToggleDropdownMenu)
                                            onAction(HomeAction.UpdateSortOrder(SortingOrder.Rarity))
                                        },
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            if (state.sharedCardsState.sortingOrder == SortingOrder.Alphabetical) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = stringResource(Res.string.checkmark_icon_description),
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                )
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = stringResource(Res.string.cards_sort_alphabetically),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        },
                                        onClick = {
                                            onAction(HomeAction.ToggleDropdownMenu)
                                            onAction(HomeAction.UpdateSortOrder(SortingOrder.Alphabetical))
                                        },
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            if (state.sharedCardsState.sortingOrder == SortingOrder.VisitCount) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = stringResource(Res.string.checkmark_icon_description),
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                )
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = stringResource(Res.string.cards_sort_by_visit_count),
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        },
                                        onClick = {
                                            onAction(HomeAction.ToggleDropdownMenu)
                                            onAction(HomeAction.UpdateSortOrder(SortingOrder.VisitCount))
                                        },
                                    )
                                }
                            }
                        },
                    )
                },
            ) { paddingValues ->
                CompositionLocalProvider(LocalScaffoldPadding provides paddingValues) {
                    NavDisplay(
                        modifier = Modifier.padding(
                            top = paddingValues.calculateTopPadding(),
                        ),
                        backStack = backStack,
                        entryProvider = entryProvider,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        transitionSpec = {
                            fadeIn(tween(400)) togetherWith fadeOut(tween(400))
                        },
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = state.foundCardsState.foundCards.isNotEmpty(),
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300)),
    ) {
        FoundCardsOverlay(
            state = state.foundCardsState,
            onAction = onAction,
        )
    }
}