package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.KeyValueRow
import com.heveamobile.setsandsteps.core.designsystem.component.SetStatisticsList
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_packs_opened
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_set_points_gained
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SummarySetHeader(
    cardSet: CardSet,
    packsOpened: Int,
    pointsGained: Int,
    newCards: Map<Rarity, Int>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        title = cardSet.name,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.overlay_packs_opened),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = packsOpened.toString(),
            )
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.overlay_set_points_gained),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = pointsGained.toString(),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                SetStatisticsList(
                    set = cardSet,
                    showExpandIcon = false,
                    modifier = Modifier.weight(1F),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.spacing.small,
                    ),
                ) {
                    Text(
                        text = "+${newCards.values.sum()}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme
                                .colorScheme.primary,
                        ),
                    )
                    newCards.forEach {
                        if (it.value > 0) {
                            Text(
                                text = "+${it.value}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color =
                                        MaterialTheme.colorScheme.primary,
                                ),
                            )
                        } else {
                            Text("")
                        }
                    }
                }
            }
        }
    }
}
