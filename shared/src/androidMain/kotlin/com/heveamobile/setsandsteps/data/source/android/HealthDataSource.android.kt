package com.heveamobile.setsandsteps.data.source.android

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.heveamobile.setsandsteps.core.domain.source.HealthDataSource
import com.heveamobile.setsandsteps.core.domain.model.StepData
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

class HealthConnectManager(private val context: Context) : HealthDataSource {
    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    override suspend fun fetchSteps(
        startTime: Instant,
        endTime: Instant,
    ): List<StepData> {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(
                startTime.toJavaInstant(),
                endTime.toJavaInstant(),
            ),
        )

        val response = healthConnectClient.readRecords(request)

        return response.records.map { record ->
            StepData(
                count = record.count,
                startTime = record.startTime.toKotlinInstant(),
                endTime = record.endTime.toKotlinInstant(),
            )
        }
    }
}