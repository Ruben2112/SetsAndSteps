package com.heveamobile.setsandsteps.core.data.manager

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresPermission
import com.heveamobile.setsandsteps.core.domain.manager.Vibrator
import android.os.Vibrator as AndroidVibrator

class AndroidVibratorImpl(
    context: Context,
) : Vibrator {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as AndroidVibrator
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(
        durationMillis: Long,
        amplitude: Int,
    ) {
        val effect = if (amplitude != -1) {
            VibrationEffect.createOneShot(
                durationMillis,
                amplitude.coerceIn(
                    1,
                    255,
                ),
            )
        } else {
            VibrationEffect.createOneShot(
                durationMillis,
                VibrationEffect.DEFAULT_AMPLITUDE,
            )
        }
        vibrator.vibrate(effect)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrateWaveform(
        timings: LongArray,
        amplitudes: IntArray,
        repeat: Int,
    ) {
        val effect = VibrationEffect.createWaveform(
            timings,
            amplitudes,
            repeat,
        )
        vibrator.vibrate(effect)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun cancel() {
        vibrator.cancel()
    }
}
