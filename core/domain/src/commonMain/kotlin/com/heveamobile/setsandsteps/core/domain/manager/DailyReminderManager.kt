package com.heveamobile.setsandsteps.core.domain.manager

import kotlinx.datetime.LocalTime

interface DailyReminderManager {
    fun scheduleDailyReminderNotification(time: LocalTime)
    fun cancelDailyReminderNotification()
}