package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    primaryActionLabel: String? = null,
    primaryAction: (() -> Unit)? = null,
    isPrimaryActionDestructive: Boolean = false,
    secondaryActionLabel: String? = null,
    secondaryAction: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        if
                                (isPrimaryActionDestructive) MaterialTheme.colorScheme.onErrorContainer else
                            MaterialTheme.colorScheme.onSurface,
                    ),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = body,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                )
                if (primaryActionLabel != null && primaryAction != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    ) {
                        if (secondaryActionLabel != null && secondaryAction != null) {
                            SecondaryButton(
                                label = secondaryActionLabel,
                                onClick = secondaryAction,
                            )
                        }
                        PrimaryButton(
                            label = primaryActionLabel,
                            onClick = primaryAction,
                            isDestructive = isPrimaryActionDestructive,
                        )
                    }
                }
            }
        }
    }
}