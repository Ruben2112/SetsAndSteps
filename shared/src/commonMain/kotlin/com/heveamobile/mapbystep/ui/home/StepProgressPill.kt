package com.heveamobile.mapbystep.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.heveamobile.mapbystep.FormatMode
import com.heveamobile.mapbystep.formatAmount
import com.heveamobile.mapbystep.theme.spacing
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.footsteps_icon_description
import mapbystep.shared.generated.resources.ic_footstep
import mapbystep.shared.generated.resources.ic_steps
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StepProgressPill(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    availableSteps: Long,
    requiredSteps: Long,
    onTap: () -> Unit,
) {
    AnimatedContent(targetState = isLoading) {
        Row(
            modifier = modifier
                .padding(horizontal = MaterialTheme.spacing.small)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    onTap()
                }
                .padding(
                    vertical = MaterialTheme.spacing.small,
                    horizontal = MaterialTheme.spacing.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = formatAmount(
                        availableSteps,
                        FormatMode.Long,
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                )
                if (availableSteps >= requiredSteps) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    WalkingFootsteps()
                } else {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.ic_steps),
                        contentDescription = stringResource(Res.string.footsteps_icon_description),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun WalkingFootsteps() {
    val infiniteTransition = rememberInfiniteTransition(label = "WalkingTransition")

    @Composable
    fun AnimatedFootstep(
        isTop: Boolean,
    ) {
        // 0F to 1F is for top step animation, 1F to 2F is for bottom step animation
        val progress by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 2F,
            animationSpec = infiniteRepeatable(
                animation = TweenSpec(
                    durationMillis = 2000,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "FootstepProgress",
        )

        // Calculate visual properties based on progress
        val normalisedProgress = if (isTop && progress < 1F) {
            progress
        } else if (!isTop && progress > 1F) {
            progress - 1F
        } else {
            0F
        }
        val offset = (1F - normalisedProgress * 20F)
        val translucency =
            if (normalisedProgress < 0.2f) normalisedProgress * 5F else (1F - normalisedProgress) * 1.2F

        Icon(
            painter = painterResource(Res.drawable.ic_footstep),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer {
                    translationX = offset
                    translationY = if (isTop) (-5).dp.toPx() else 5.dp.toPx()
                    alpha = translucency.coerceIn(
                        0F,
                        1F,
                    )
                    rotationZ = 90F
                },
        )
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        AnimatedFootstep(
            isTop = true,
        )
        AnimatedFootstep(
            isTop = false,
        )
    }
}