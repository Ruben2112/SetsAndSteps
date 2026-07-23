package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import org.jetbrains.compose.resources.stringResource
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.expand_icon_description

@Composable
fun <T> DropDownMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        // The main collapsed view
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable { expanded = !expanded }
                .padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                // Show the currently selected item's composable
                if (items.isNotEmpty()) {
                    itemContent(selectedItem)
                }
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Icon(
                imageVector = if (expanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                contentDescription = stringResource(Res.string.expand_icon_description),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // The DropDown options themselves
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.small,
                    )
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                onItemSelected(item)
                                expanded = false
                            }
                            .padding(
                                // End padding matches size of chevron icon
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.large + MaterialTheme.spacing.medium,
                                top = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium,
                            ),
                    ) {
                        itemContent(item)
                    }
                }
            }
        }
    }
}