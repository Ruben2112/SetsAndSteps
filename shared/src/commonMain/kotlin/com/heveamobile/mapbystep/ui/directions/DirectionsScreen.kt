package com.heveamobile.mapbystep.ui.directions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.mapbystep.FormatMode
import com.heveamobile.mapbystep.domain.model.Rarity
import com.heveamobile.mapbystep.formatAmount
import com.heveamobile.mapbystep.theme.color
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.ui.common.Card
import com.heveamobile.mapbystep.ui.common.InfoCard
import com.heveamobile.mapbystep.ui.common.InputField
import com.heveamobile.mapbystep.ui.common.LocalScaffoldPadding
import com.heveamobile.mapbystep.ui.common.MapDropDownMenu
import com.heveamobile.mapbystep.ui.common.MapStatisticsList
import com.heveamobile.mapbystep.ui.common.PrimaryButton
import com.heveamobile.mapbystep.ui.common.SecondaryButton
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.decrease_icon_description
import mapbystep.shared.generated.resources.directions_autofill
import mapbystep.shared.generated.resources.directions_current_map_points
import mapbystep.shared.generated.resources.directions_explanation
import mapbystep.shared.generated.resources.directions_purchase
import mapbystep.shared.generated.resources.directions_reset
import mapbystep.shared.generated.resources.directions_shop
import mapbystep.shared.generated.resources.directions_sold_out
import mapbystep.shared.generated.resources.directions_total_cost
import mapbystep.shared.generated.resources.ic_map_points
import mapbystep.shared.generated.resources.increase_icon_description
import mapbystep.shared.generated.resources.map_points_alternate_text
import mapbystep.shared.generated.resources.map_points_icon_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DirectionsScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DirectionsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DirectionsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun DirectionsContent(
    modifier: Modifier = Modifier,
    state: DirectionsState,
    onAction: (DirectionsAction) -> Unit,
) {

    val density = LocalDensity.current
    val mapPointsIconSize = with(density) { MaterialTheme.typography.bodyMedium.lineHeight.toDp() }

    val explanation = stringResource(
        Res.string.directions_explanation,
        stringResource(Res.string.map_points_alternate_text),
    )
    val inlineContentId = stringResource(Res.string.map_points_icon_description)
    val inlinedString = buildAnnotatedString {
        val splitExplanation =
            explanation.split(stringResource(Res.string.map_points_alternate_text))
        append(splitExplanation[0])
        appendInlineContent(
            inlineContentId,
            stringResource(Res.string.map_points_alternate_text),
        )
        append(splitExplanation[1])
    }
    val inlineContent = mapOf(
        Pair(
            inlineContentId,
            InlineTextContent(
                placeholder = Placeholder(
                    width = MaterialTheme.typography.bodyMedium.lineHeight,
                    height = MaterialTheme.typography.bodyMedium.lineHeight,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) {
                Icon(
                    modifier = Modifier.size(mapPointsIconSize),
                    painter = painterResource(Res.drawable.ic_map_points),
                    contentDescription = stringResource(Res.string.map_points_icon_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
        ),
    )

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .consumeWindowInsets(LocalScaffoldPadding.current)
            .imePadding(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        item {
            InfoCard(
                modifier = Modifier.fillMaxWidth(),
                annotatedText = inlinedString,
                inlineContent = inlineContent,
            )
        }
        if (state.maps.size > 1) {
            item {
                MapDropDownMenu(
                    maps = state.maps,
                    selectedMap = state.selectedMap
                        ?: state.maps.first(),
                    onItemSelected = { map -> onAction(DirectionsAction.SelectMap(map)) },
                )
            }
        }
        if (state.selectedMap != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    bottomContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1F),
                                text = stringResource(Res.string.directions_current_map_points),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLarge))
                            Icon(
                                modifier = Modifier.size(mapPointsIconSize),
                                painter = painterResource(Res.drawable.ic_map_points),
                                contentDescription = stringResource(Res.string.map_points_icon_description),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = formatAmount(
                                    state.selectedMap.currentMapPoints,
                                    formatMode = FormatMode.Long,
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                ) {
                    MapStatisticsList(
                        modifier = Modifier
                            .fillMaxWidth(),
                        map = state.selectedMap,
                        showExpandIcon = false,
                    )
                }
            }
        }
        if (state.selectedMap != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.directions_shop),
                    bottomContent = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = MaterialTheme.spacing.extraSmall,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(Res.string.directions_total_cost),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                                Icon(
                                    modifier = Modifier.size(mapPointsIconSize),
                                    painter = painterResource(Res.drawable.ic_map_points),
                                    contentDescription = stringResource(Res.string.map_points_icon_description),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = formatAmount(
                                        state.totalCost,
                                        formatMode = FormatMode.Long,
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                itemVerticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            ) {
                                SecondaryButton(
                                    label = stringResource(Res.string.directions_autofill),
                                    onClick = { onAction(DirectionsAction.AutofillCart) },
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                SecondaryButton(
                                    label = stringResource(Res.string.directions_reset),
                                    onClick = { onAction(DirectionsAction.ResetCart) },
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                PrimaryButton(
                                    label = stringResource(Res.string.directions_purchase),
                                    onClick = { onAction(DirectionsAction.Purchase) },
                                )
                            }
                        }
                    },
                ) {
                    Rarity.entries.forEach { rarity ->
                        val amountOwned = state.selectedMap.directions.count { it == rarity }
                        val cost = state.selectedMap.storePrice(rarity)
                        val amountInCart = state.cart[rarity]
                            ?: 0
                        val amountInStock = state.amountInStock[rarity]
                            ?: 0
                        DirectionsShopRow(
                            rarity = rarity,
                            cost = cost,
                            amountInCart = amountInCart,
                            amountOwned = amountOwned,
                            amountInStock = amountInStock,
                            mapPointsIconSize = mapPointsIconSize,
                            onAction = onAction,
                        )
                    }

                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

@Composable
private fun DirectionsShopRow(
    modifier: Modifier = Modifier,
    rarity: Rarity,
    cost: Int,
    amountInCart: Int,
    amountOwned: Int,
    amountInStock: Int,
    mapPointsIconSize: Dp,
    onAction: (DirectionsAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = "${rarity.name} ($amountOwned)",
            style = MaterialTheme.typography.bodyMedium.copy(color = rarity.color(MaterialTheme.colorScheme.onPrimary)),
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        Icon(
            modifier = Modifier.size(mapPointsIconSize),
            painter = painterResource(Res.drawable.ic_map_points),
            contentDescription = stringResource(Res.string.map_points_icon_description),
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = formatAmount(
                cost.toLong(),
                formatMode = FormatMode.Long,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        Box(contentAlignment = Alignment.Center) {
            ShopControls(
                modifier = Modifier.alpha(if (amountInStock > 0) 1F else 0F),
                rarity = rarity,
                amountInCart = amountInCart,
                amountInStock = amountInStock,
                isLast = rarity == Rarity.entries.last(),
                onAction = onAction,
            )
            Text(
                modifier = Modifier.alpha(if (amountInStock > 0) 0F else 1F),
                text = stringResource(Res.string.directions_sold_out),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                ),
            )
        }
    }
}

@Composable
private fun ShopControls(
    modifier: Modifier = Modifier,
    rarity: Rarity,
    amountInCart: Int,
    amountInStock: Int,
    isLast: Boolean,
    onAction: (DirectionsAction) -> Unit,
) {
    val textFieldState = rememberTextFieldState(initialText = amountInCart.toString())

    LaunchedEffect(amountInCart) {
        val currentTextAsInt = textFieldState.text
            .toString()
            .toIntOrNull()
            ?: 0
        if (currentTextAsInt != amountInCart) {
            textFieldState.edit {
                replace(
                    0,
                    length,
                    amountInCart.toString(),
                )
            }
        }
    }

    LaunchedEffect(textFieldState.text) {
        val newAmount = textFieldState.text
            .toString()
            .toIntOrNull()
            ?: 0
        if (newAmount != amountInCart) {
            onAction(
                DirectionsAction.UpdateCartAmount(
                    rarity,
                    newAmount,
                ),
            )
        }
    }

    LaunchedEffect(textFieldState.text) {
        val newAmount = textFieldState.text
            .toString()
            .toIntOrNull()
            ?: 0
        if (newAmount != amountInCart) {
            onAction(
                DirectionsAction.UpdateCartAmount(
                    rarity,
                    newAmount,
                ),
            )
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val minusEnabled = amountInCart > 0
        OutlinedIconButton(
            modifier = Modifier.size(20.dp),
            enabled = minusEnabled,
            colors = IconButtonDefaults
                .outlinedIconButtonColors(),
            onClick = {
                onAction(
                    DirectionsAction.UpdateCartAmount(
                        rarity,
                        amountInCart - 1,
                    ),
                )
            },
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(Res.string.decrease_icon_description),
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        InputField(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            state = textFieldState,
            textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
            contentPadding = PaddingValues(MaterialTheme.spacing.small),
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
            inputTransformation = {
                val isNumeric = asCharSequence().all { it.isDigit() }
                if (!isNumeric) {
                    revertAllChanges()
                }
            },
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        val plusEnabled = amountInCart < amountInStock
        OutlinedIconButton(
            modifier = Modifier.size(20.dp),
            enabled = plusEnabled,
            colors = IconButtonDefaults
                .outlinedIconButtonColors(),
            onClick = {
                onAction(
                    DirectionsAction.UpdateCartAmount(
                        rarity,
                        amountInCart + 1,
                    ),
                )
            },
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.increase_icon_description),
            )
        }
    }
}