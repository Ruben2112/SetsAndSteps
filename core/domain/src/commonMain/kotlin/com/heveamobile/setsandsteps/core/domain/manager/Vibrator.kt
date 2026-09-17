package com.heveamobile.setsandsteps.core.domain.manager

interface Vibrator {
    /**
     * Vibrates with the given [durationMillis].
     * [amplitude] is the intensity of the vibration (1-255), or -1 for default.
     */
    fun vibrate(
        durationMillis: Long,
        amplitude: Int = -1,
    )

    /**
     * Vibrates with a given [timings] and [amplitudes] pattern.
     * [timings] is an array of durations in milliseconds.
     * [amplitudes] is an array of intensities (0-255).
     * [repeat] is the index to repeat from, or -1 for no repeat.
     */
    fun vibrateWaveform(
        timings: LongArray,
        amplitudes: IntArray,
        repeat: Int = -1,
    )

    /**
     * Stops any ongoing vibration.
     */
    fun cancel()
}
