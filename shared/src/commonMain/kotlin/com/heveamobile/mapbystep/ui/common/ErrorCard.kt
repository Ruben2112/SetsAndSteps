package com.heveamobile.mapbystep.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heveamobile.mapbystep.theme.spacing
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.error_card_icon_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorCard(
    modifier: Modifier = Modifier,
    errorMessage: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier,
        bottomContent = {
            if (actionLabel != null && onAction != null) {
                PrimaryButton(
                    label = actionLabel,
                    isDestructive = true, // Not really destructive but the color works better this way
                    onClick = onAction,
                )
            }
        },
        showOutline = false,
        containerColor = MaterialTheme.colorScheme.errorContainer,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = stringResource(Res.string.error_card_icon_description),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onError,
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Text(
                errorMessage,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onError),
            )
        }
    }
}