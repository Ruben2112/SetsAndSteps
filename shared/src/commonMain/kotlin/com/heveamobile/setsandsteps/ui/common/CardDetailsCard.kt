package com.heveamobile.setsandsteps.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.formatAmount
import com.heveamobile.setsandsteps.theme.color
import com.heveamobile.setsandsteps.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.details_card_times_found
import setsandsteps.shared.generated.resources.ic_steps
import setsandsteps.shared.generated.resources.rarity_icon_description

@Composable
fun CardDetailsCard(
    modifier: Modifier = Modifier,
    cardSet: CardSet,
    card: CollectableCard,
    foundCountOverride: Int? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        CollectableCardLayout(
            card = card,
            isLarge = true,
            isRevealed = true,
        )
        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1F)
                            .padding(MaterialTheme.spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier
                                .weight(1F)
                                .padding(MaterialTheme.spacing.small)
                                .size(48.dp),
                            painter = painterResource(Res.drawable.ic_steps),
                            tint = if (card.rarity == Rarity.Common) MaterialTheme.colorScheme.onSurface else card.rarity.color(MaterialTheme.colorScheme.onPrimary),
                            contentDescription = stringResource(
                                Res.string.rarity_icon_description,
                                card.rarity.name,
                            ),
                        )
                        Text(
                            text = card.rarity.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (card.rarity == Rarity.Common) MaterialTheme.colorScheme.onSurface else card.rarity.color(MaterialTheme.colorScheme.onPrimary),
                            ),
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = MaterialTheme.spacing.medium),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    Column(
                        modifier = Modifier
                            .weight(1F)
                            .padding(MaterialTheme.spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        val foundCount = foundCountOverride
                            ?: (card.userData?.findCount
                                ?: 0)
                        Box(
                            modifier = Modifier.weight(1F),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = foundCount.toString(),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        Text(
                            text = pluralStringResource(
                                Res.plurals.details_card_times_found,
                                foundCount,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                val bbox = card.bbox
                if (bbox != null) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.spacing.medium)
                            .height(160.dp)
                            .clip(shape = MaterialTheme.shapes.small),
                    ) {
                        MapboxMap(
                            modifier = Modifier.fillMaxSize(),
                            boundingBox = bbox
                                .split(",")
                                .map { it.toDouble() },
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
                val properties = listOfNotNull(
                    cardSet.propertyName1?.let { it to card.propertyValue1 },
                    cardSet.propertyName2?.let { it to card.propertyValue2 },
                    cardSet.propertyName3?.let { it to card.propertyValue3 },
                    cardSet.propertyName4?.let { it to card.propertyValue4 },
                    cardSet.propertyName5?.let { it to card.propertyValue5 },
                    cardSet.propertyName6?.let { it to card.propertyValue6 },
                    cardSet.propertyName7?.let { it to card.propertyValue7 },
                    cardSet.propertyName8?.let { it to card.propertyValue8 },
                    cardSet.propertyName9?.let { it to card.propertyValue9 },
                    cardSet.propertyName10?.let { it to card.propertyValue10 },
                ).mapNotNull { (name, value) -> value?.let { name to it } }

                if (properties.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        properties.forEach { (name, value) ->
                            KeyValueRow(
                                key = name,
                                values = value
                                    .split(",")
                                    .map {
                                        it
                                            .toIntOrNull()
                                            ?.let(::formatAmount)
                                            ?: it
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}
