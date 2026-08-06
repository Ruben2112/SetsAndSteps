package com.heveamobile.setsandsteps.core.navigation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val ic_pack: ImageVector
    get() {
        if (_pack != null) {
            return _pack!!
        }
        _pack =
            ImageVector
                .Builder(
                    name = "pack",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
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
                            9.27f,
                            13.75f,
                        )
                        lineTo(
                            16.81f,
                            16.5f,
                        )
                        lineTo(
                            16.12f,
                            18.38f,
                        )
                        lineTo(
                            8.59f,
                            15.63f,
                        )
                        close()
                        moveTo(
                            4f,
                            5.17f,
                        )
                        lineTo(
                            3.18f,
                            5.57f,
                        )
                        quadTo(
                            2.4f,
                            5.9f,
                            2.13f,
                            6.69f,
                        )
                        reflectiveQuadTo(
                            2.2f,
                            8.25f,
                        )
                        lineTo(
                            4f,
                            12.15f,
                        )
                        verticalLineToRelative(-6.98f)
                        close()
                        moveTo(
                            8f,
                            3f,
                        )
                        quadTo(
                            7.18f,
                            3f,
                            6.59f,
                            3.6f,
                        )
                        reflectiveQuadTo(
                            6f,
                            5.02f,
                        )
                        verticalLineTo(11f)
                        lineToRelative(
                            2.68f,
                            -7.35f,
                        )
                        quadToRelative(
                            0.07f,
                            -0.17f,
                            0.13f,
                            -0.34f,
                        )
                        reflectiveQuadTo(
                            8.98f,
                            3f,
                        )
                        horizontalLineTo(8f)
                        close()
                        moveToRelative(
                            5.15f,
                            0.13f,
                        )
                        quadTo(
                            12.38f,
                            2.85f,
                            11.6f,
                            3.2f,
                        )
                        reflectiveQuadTo(
                            10.55f,
                            4.32f,
                        )
                        lineTo(
                            6.13f,
                            16.55f,
                        )
                        quadTo(
                            5.85f,
                            17.32f,
                            6.2f,
                            18.09f,
                        )
                        reflectiveQuadTo(
                            7.33f,
                            19.12f,
                        )
                        lineTo(
                            14.85f,
                            21.87f,
                        )
                        quadTo(
                            15.63f,
                            22.15f,
                            16.39f,
                            21.8f,
                        )
                        reflectiveQuadToRelative(
                            1.04f,
                            -1.13f,
                        )
                        lineToRelative(
                            4.45f,
                            -12.23f,
                        )
                        quadToRelative(
                            0.28f,
                            -0.78f,
                            -0.07f,
                            -1.54f,
                        )
                        reflectiveQuadToRelative(
                            -1.13f,
                            -1.04f,
                        )
                        lineToRelative(
                            -7.53f,
                            -2.75f,
                        )
                        close()
                        moveTo(
                            12.45f,
                            5f,
                        )
                        lineTo(
                            20f,
                            7.75f,
                        )
                        lineTo(
                            15.53f,
                            20f,
                        )
                        lineTo(
                            8f,
                            17.25f,
                        )
                        lineTo(
                            12.45f,
                            5f,
                        )
                        close()
                        moveTo(
                            14f,
                            12.5f,
                        )
                        close()
                    }
                }
                .build()
        return _pack!!
    }

private var _pack: ImageVector? = null
