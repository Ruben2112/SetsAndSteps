package com.heveamobile.setsandsteps.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val ic_footsteps_off: ImageVector
    get() {
        if (_footstepsOff != null) {
            return _footstepsOff!!
        }
        _footstepsOff =
            ImageVector
                .Builder(
                    name = "footsteps_off_flipped",
                    defaultWidth = 24.dp,
                    defaultHeight = 25.dp,
                    viewportWidth = 24f,
                    viewportHeight = 25f,
                )
                .apply {
                    // Left foot toe pad (High)
                    path(
                        fill = SolidColor(Color(0xFF534340)),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(
                            7f,
                            0f,
                        )
                        curveTo(
                            8.6569f,
                            0.0000475f,
                            10f,
                            3.13846f,
                            10f,
                            4.73147f,
                        )
                        curveTo(
                            10f,
                            5.88604f,
                            9.606f,
                            8.2396f,
                            9.3892f,
                            9.44772f,
                        )
                        curveTo(
                            9.3068f,
                            9.90646f,
                            9.2273f,
                            10.2f,
                            8.7197f,
                            10.2f,
                        )
                        lineTo(
                            7f,
                            10.2f,
                        )
                        lineTo(
                            5.2803f,
                            10.2f,
                        )
                        curveTo(
                            4.7727f,
                            10.2f,
                            4.6932f,
                            9.90646f,
                            4.6108f,
                            9.44772f,
                        )
                        curveTo(
                            4.394f,
                            8.23965f,
                            4f,
                            5.88609f,
                            4f,
                            4.73147f,
                        )
                        curveTo(
                            4f,
                            3.13846f,
                            5.3431f,
                            0.0000473f,
                            7f,
                            0f,
                        )
                        close()
                    }
                    // Right foot toe pad (Low)
                    path(
                        fill = SolidColor(Color(0xFF534340)),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(
                            17f,
                            8.5f,
                        )
                        curveTo(
                            18.6569f,
                            8.50005f,
                            20f,
                            11.6385f,
                            20f,
                            13.2315f,
                        )
                        curveTo(
                            20f,
                            14.386f,
                            19.606f,
                            16.7396f,
                            19.3892f,
                            17.9477f,
                        )
                        curveTo(
                            19.3068f,
                            18.4065f,
                            19.2273f,
                            18.7f,
                            18.7197f,
                            18.7f,
                        )
                        lineTo(
                            17f,
                            18.7f,
                        )
                        lineTo(
                            15.2803f,
                            18.7f,
                        )
                        curveTo(
                            14.7727f,
                            18.7f,
                            14.6932f,
                            18.4065f,
                            14.6108f,
                            17.9477f,
                        )
                        curveTo(
                            14.394f,
                            16.7397f,
                            14f,
                            14.3861f,
                            14f,
                            13.2315f,
                        )
                        curveTo(
                            14f,
                            11.6385f,
                            15.3431f,
                            8.50005f,
                            17f,
                            8.5f,
                        )
                        close()
                    }
                    // Left foot heel (High)
                    path(
                        fill = SolidColor(Color(0xFF534340)),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(
                            5.3704f,
                            12.1f,
                        )
                        lineTo(
                            8.5623f,
                            12.1f,
                        )
                        curveTo(
                            8.7845f,
                            12.1f,
                            9f,
                            12.3443f,
                            9f,
                            12.6802f,
                        )
                        verticalLineTo(13.0771f)
                        curveTo(
                            9f,
                            14.7466f,
                            8.1046f,
                            15.1f,
                            7f,
                            15.1f,
                        )
                        curveTo(
                            5.8954f,
                            15.1f,
                            5f,
                            14.7466f,
                            5f,
                            13.0771f,
                        )
                        verticalLineTo(12.6802f)
                        curveTo(
                            5f,
                            12.3443f,
                            5.1482f,
                            12.1f,
                            5.3704f,
                            12.1f,
                        )
                        close()
                    }
                    // Right foot heel (Low)
                    path(
                        fill = SolidColor(Color(0xFF534340)),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(
                            15.3704f,
                            21.1f,
                        )
                        lineTo(
                            18.5623f,
                            21.1f,
                        )
                        curveTo(
                            18.7845f,
                            21.1f,
                            19f,
                            21.3443f,
                            19f,
                            21.6802f,
                        )
                        lineTo(
                            19f,
                            22.0771f,
                        )
                        curveTo(
                            19f,
                            23.7466f,
                            18.1046f,
                            24.1f,
                            17f,
                            24.1f,
                        )
                        curveTo(
                            15.8954f,
                            24.1f,
                            15f,
                            23.7466f,
                            15f,
                            22.0771f,
                        )
                        lineTo(
                            15f,
                            21.6802f,
                        )
                        curveTo(
                            15f,
                            21.3443f,
                            15.1482f,
                            21.1f,
                            15.3704f,
                            21.1f,
                        )
                        close()
                    }
                    // Diagonal "off" slash (top-left to bottom-right), drawn last so it
                    // sits on top of the foot shapes as the knockout stripe, matching
                    // the stroke width/angle convention used by devices_off /
                    // account_circle_off.
                    path(
                        fill = SolidColor(Color(0xFF534340)),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(
                            0.7f,
                            3.7f,
                        )
                        lineTo(
                            2.1f,
                            2.3f,
                        )
                        lineTo(
                            21.9f,
                            22.6f,
                        )
                        lineTo(
                            20.5f,
                            24f,
                        )
                        close()
                    }
                }
                .build()
        return _footstepsOff!!
    }

private var _footstepsOff: ImageVector? = null