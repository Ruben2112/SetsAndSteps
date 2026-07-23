package com.heveamobile.setsandsteps.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heveamobile.setsandsteps.domain.model.CardSet
import com.heveamobile.setsandsteps.domain.model.Rarity
import com.heveamobile.setsandsteps.theme.color
import com.heveamobile.setsandsteps.theme.spacing
import org.jetbrains.compose.resources.stringResource
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.expand_icon_description
import setsandsteps.shared.generated.resources.set_statistics_total_label

@Composable
fun SetStatisticsList(
    modifier: Modifier = Modifier,
    set: CardSet,
    isExpanded: Boolean = true,
    showExpandIcon: Boolean = true,
) {
    val showProgress = set.userData != null

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.set_statistics_total_label),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = if (showProgress) set.userData.formatProgress(null) else set.cards.size.toString(),
                style = MaterialTheme.typography.bodyMedium,
            )
            if (showExpandIcon) {
                Spacer(
                    modifier = Modifier.width(MaterialTheme.spacing.small),
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = if (isExpanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                    contentDescription = stringResource(Res.string.expand_icon_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        AnimatedVisibility(
            visible = isExpanded,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            ) {
                Rarity.entries.forEach { rarity ->
                    KeyValueRow(
                        modifier = Modifier.padding(
                            end = if (showExpandIcon) MaterialTheme.spacing.large else 0.dp,
                        ),
                        key = rarity.name,
                        keyStyle = MaterialTheme.typography.bodyMedium.copy(color = rarity.color(MaterialTheme.colorScheme.onPrimary)),
                        value = if (showProgress) set.userData.formatProgress(rarity) else set.cards
                            .count { it.rarity == rarity }
                            .toString(),
                    )
                }
            }
        }
    }
}