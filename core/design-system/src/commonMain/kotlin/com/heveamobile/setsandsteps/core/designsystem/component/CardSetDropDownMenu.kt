package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.heveamobile.setsandsteps.core.domain.model.CardSet

@Composable
fun CardSetDropDownMenu(
    sets: List<CardSet>,
    selectedSet: CardSet,
    onItemSelected: (CardSet) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropDownMenu(
        items = sets,
        selectedItem = selectedSet,
        onItemSelected = { map -> onItemSelected(map) },
        modifier = modifier,
        itemContent = { map ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1F),
                    text = map.name,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                )
            }
        },
    )
}