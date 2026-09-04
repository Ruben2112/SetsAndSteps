package com.heveamobile.setsandsteps.core.designsystem.component

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.toTitleCase

@Composable
fun KeyValueRow(
    value: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    valueTitleCasingEnabled: Boolean = true,
    key: @Composable () -> Unit,
) {
    KeyValueRow(
        modifier = modifier,
        values = listOf(value),
        valueStyle = valueStyle,
        valueTitleCasingEnabled = valueTitleCasingEnabled,
        key = key,
    )
}

@Composable
fun KeyValueRow(
    values: List<String>,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    valueTitleCasingEnabled: Boolean = true,
    key: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        key()
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLarge))
        Column(
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.End,
        ) {
            values.forEach { value ->
                Text(
                    text = if (valueTitleCasingEnabled) value.toTitleCase() else value,
                    style = valueStyle,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
