package com.heveamobile.setsandsteps.shell

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
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.ic_footstep
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.navigation.icons.ic_pack
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.pack_icon_description
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun StepProgressPill(
    modifier: Modifier = Modifier,
    packCount: Int,
    onTap: () -> Unit,
) {
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
        Text(
            text = formatAmount(
                packCount.toLong(),
                FormatMode.Long,
            ),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ic_pack,
            contentDescription = stringResource(Res.string.pack_icon_description),
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
        )
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
            painter = painterResource(DesignSystemRes.drawable.ic_footstep),
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