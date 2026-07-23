package com.heveamobile.setsandsteps.domain.usecase

import com.heveamobile.setsandsteps.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class GetDailyStepsChartDataUseCase(
    private val getUserUseCase: GetUserUseCase,
) {

    val defaultEmptyMap = mapOf(
        Pair(
            Clock.System.now(),
            0L,
        ),
    )

    suspend operator fun invoke(): Flow<Map<Instant, Long>> {
        return getUserUseCase().map { user ->
            if (user != null) {
                processStepData(user)
            } else {
                defaultEmptyMap
            }
        }
    }

    private fun processStepData(user: User): Map<Instant, Long> {
        val timeZone = TimeZone.currentSystemDefault()

        val dailyStepData = user.stepData
            .groupBy { step ->
                // Convert Instant to the start of its local day to use as a Map key
                val localDateTime = step.startTime.toLocalDateTime(timeZone)
                val startOfDay = LocalDateTime(
                    localDateTime.year,
                    localDateTime.month,
                    localDateTime.day,
                    0,
                    0,
                    0,
                    0,
                )
                startOfDay.toInstant(timeZone)
            }
            .mapValues { entry ->
                // Sum all step counts for that specific day
                entry.value.sumOf { it.count }
            }
            .toList() // Convert to List of Pairs to sort and take
            .sortedByDescending { it.first } // Sort by Instant (newest first)
            .take(14) // Take the last 14 days
            .reversed() // Reverse back so the chart flows from oldest to newest
            .toMap() // Convert back to Map

        return dailyStepData.ifEmpty {
            defaultEmptyMap
        }
    }
}