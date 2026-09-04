package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.question_mark_icon_description
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun Card(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            var topPadding = 0.dp
            if (title != null || onAction != null) {
                CardHeader(
                    title = title,
                    subtitle = subtitle,
                    actionIcon = actionIcon,
                    onAction = onAction,
                )
            } else {
                topPadding = MaterialTheme.spacing.medium
            }
            Box(
                modifier = Modifier
                    .padding(top = topPadding)
                    .padding(horizontal = MaterialTheme.spacing.medium),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun CardHeader(
    title: String? = null,
    subtitle: String? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.padding(
            start = MaterialTheme.spacing.medium,
        ),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier
                .weight(1F),
        ) {
            if (title != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.small),
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                )
            }
            if (subtitle != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        if (onAction != null && actionIcon != null) {
            IconButton(
                onClick = onAction,
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = stringResource(Res.string.question_mark_icon_description),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}