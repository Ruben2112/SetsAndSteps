package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.StepData
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class UpdateUserRecordsUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() {
        val user = userRepository.getUserWithStepData()
        val stepData = user?.stepData
            ?: return

        // Sort by time to ensure the sliding window moves correctly
        val sortedSteps = stepData.sortedBy { it.startTime }

        // Calculate all current stats using current window
        val current24h = calculateCurrentWindowSum(
            steps = sortedSteps,
            windowDuration = 24.hours,
        )
        val current7d = calculateCurrentWindowSum(
            steps = sortedSteps,
            windowDuration = 7.days,
        )
        val current30d = calculateCurrentWindowSum(
            steps = sortedSteps,
            windowDuration = 30.days,
        )

        // Calculate all records using sliding windows
        val new24h = calculateSlidingWindowMax(
            steps = sortedSteps,
            windowDuration = 24.hours,
        )
        val new7d = calculateSlidingWindowMax(
            steps = sortedSteps,
            windowDuration = 7.days,
        )
        val new30d = calculateSlidingWindowMax(
            steps = sortedSteps,
            windowDuration = 30.days,
        )

        userRepository.updateUser(
            user.copy(
                previousTwentyFourHours = current24h,
                twentyFourHourRecord = maxOf(
                    user.twentyFourHourRecord,
                    new24h,
                ),
                previousSevenDays = current7d,
                sevenDayRecord = maxOf(
                    user.sevenDayRecord,
                    new7d,
                ),
                previousThirtyDays = current30d,
                thirtyDayRecord = maxOf(
                    user.thirtyDayRecord,
                    new30d,
                ),
            ),
        )
    }

    /**
     * Calculates the maximum steps found within any rolling window of [windowDuration].
     */
    private fun calculateSlidingWindowMax(
        steps: List<StepData>,
        windowDuration: Duration,
    ): Long {
        var maxSteps = 0L
        var currentWindowSum = 0L
        var right = 0

        // Sliding window using two pointers (left and right)
        for (left in steps.indices) {
            val windowStart = steps[left].startTime
            val windowEnd = windowStart + windowDuration

            // Expand the window to the right as long as steps fall within the duration
            while (right < steps.size && steps[right].startTime < windowEnd) {
                currentWindowSum += steps[right].count
                right++
            }

            if (currentWindowSum > maxSteps) {
                maxSteps = currentWindowSum
            }

            // Shrink the window from the left for the next iteration
            currentWindowSum -= steps[left].count
        }

        return maxSteps
    }

    private fun calculateCurrentWindowSum(
        steps: List<StepData>,
        windowDuration: Duration,
    ): Long {
        val now = Clock.System.now()
        val windowStart = now - windowDuration

        return steps
            .filter { it.startTime in windowStart..now }
            .sumOf { it.count }
    }
}