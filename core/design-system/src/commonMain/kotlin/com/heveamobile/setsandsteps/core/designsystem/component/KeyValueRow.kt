package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.toTitleCase

@Composable
fun KeyValueRow(
    modifier: Modifier = Modifier,
    key: @Composable () -> Unit,
    value: String,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    valueTitleCasingEnabled: Boolean = true,
) {
    KeyValueRow(
        modifier = modifier,
        key = key,
        values = listOf(value),
        valueStyle = valueStyle,
        valueTitleCasingEnabled = valueTitleCasingEnabled,
    )
}

@Composable
fun KeyValueRow(
    modifier: Modifier = Modifier,
    key: @Composable () -> Unit,
    values: List<String>,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    valueTitleCasingEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(modifier = Modifier.weight(1F)) {
            key()
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLarge))
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            values.forEach { value ->
                Text(
                    text = if (valueTitleCasingEnabled) value.toTitleCase() else value,
                    style = valueStyle,
                )
            }
        }
    }
}
