package com.heveamobile.mapbystep.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heveamobile.mapbystep.domain.model.Destination
import com.heveamobile.mapbystep.domain.model.Info
import com.heveamobile.mapbystep.domain.model.Rarity
import com.heveamobile.mapbystep.formatPopulation
import com.heveamobile.mapbystep.theme.color
import com.heveamobile.mapbystep.theme.spacing
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.country_info_card_capitals
import mapbystep.shared.generated.resources.country_info_card_continents
import mapbystep.shared.generated.resources.country_info_card_currencies
import mapbystep.shared.generated.resources.country_info_card_flag
import mapbystep.shared.generated.resources.country_info_card_languages
import mapbystep.shared.generated.resources.country_info_card_population
import mapbystep.shared.generated.resources.country_info_card_visits
import mapbystep.shared.generated.resources.ic_steps
import mapbystep.shared.generated.resources.rarity_icon_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CountryInfoCard(
    modifier: Modifier = Modifier,
    destination: Destination,
    visitCountOverride: Int? = null,
) {
    val info = destination.info as Info.CountryInfo
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium),
                text = destination.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (destination.rarity == Rarity.Common) MaterialTheme.colorScheme.onSurface else destination.rarity.color(MaterialTheme.colorScheme.onPrimary),
                ),
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Box(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.medium)
                    .height(160.dp),
            ) {
                MapboxMap(
                    modifier = Modifier.fillMaxSize(),
                    boundingBox = destination.info.boundingBox,
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
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
                        tint = if (destination.rarity == Rarity.Common) MaterialTheme.colorScheme.onSurface else destination.rarity.color(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = stringResource(
                            Res.string.rarity_icon_description,
                            destination.rarity.name,
                        ),
                    )
                    Text(
                        text = destination.rarity.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (destination.rarity == Rarity.Common) MaterialTheme.colorScheme.onSurface else destination.rarity.color(MaterialTheme.colorScheme.onPrimary),
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
                    Box(
                        modifier = Modifier.weight(1F),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.padding(MaterialTheme.spacing.small),
                            text = info.flag,
                            textAlign = TextAlign.Center,
                            autoSize = TextAutoSize.StepBased(),
                        )
                    }
                    Text(
                        text = stringResource(Res.string.country_info_card_flag),
                        style = MaterialTheme.typography.bodyMedium,
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
                    Box(
                        modifier = Modifier.weight(1F),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = visitCountOverride?.toString()
                                ?: destination.totalVisits.toString(),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Text(
                        text = pluralStringResource(
                            Res.plurals.country_info_card_visits,
                            visitCountOverride
                                ?: destination.totalVisits,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            ) {
                val population = info.population
                val capitals = info.capitals.split(",")
                val continents = info.continents.split(",")
                val languages = info.languages.split(",")
                val currencies = info.currencies.split(",")

                KeyValueRow(
                    key = stringResource(Res.string.country_info_card_population),
                    value = formatPopulation(population),
                )
                KeyValueRow(
                    key = pluralStringResource(
                        Res.plurals.country_info_card_capitals,
                        capitals.size,
                    ),
                    values = capitals,
                )
                KeyValueRow(
                    key = pluralStringResource(
                        Res.plurals.country_info_card_continents,
                        continents.size,
                    ),
                    values = continents,
                )
                KeyValueRow(
                    key = pluralStringResource(
                        Res.plurals.country_info_card_languages,
                        languages.size,
                    ),
                    values = languages,
                )
                KeyValueRow(
                    key = pluralStringResource(
                        Res.plurals.country_info_card_currencies,
                        currencies.size,
                    ),
                    values = currencies,
                )
            }
        }
    }
}