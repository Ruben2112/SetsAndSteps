package com.heveamobile.mapbystep.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.heveamobile.mapbystep.domain.model.Map

@Composable
fun MapDropDownMenu(
    modifier: Modifier = Modifier,
    maps: List<Map>,
    selectedMap: Map,
    onItemSelected: (Map) -> Unit,
) {
    DropDownMenu(
        modifier = modifier,
        items = maps,
        selectedItem = selectedMap,
        onItemSelected = { map -> onItemSelected(map) },
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