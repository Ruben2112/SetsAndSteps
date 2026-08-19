package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.KeyValueRow
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_new_cards
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_pack_progress
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_set_points_gained
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SetHeader(
    modifier: Modifier = Modifier,
    setName: String,
    packProgressValue: String,
    newCardsCount: Int,
    pointsGained: Int,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        title = setName,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                key = { Text(text = stringResource(Res.string.overlay_pack_progress)) },
                value = packProgressValue,
            )
            KeyValueRow(
                key = { Text(text = stringResource(Res.string.overlay_new_cards)) },
                value = newCardsCount.toString(),
            )
            KeyValueRow(
                key = { Text(text = stringResource(Res.string.overlay_set_points_gained)) },
                value = pointsGained.toString(),
            )
        }
    }
}
