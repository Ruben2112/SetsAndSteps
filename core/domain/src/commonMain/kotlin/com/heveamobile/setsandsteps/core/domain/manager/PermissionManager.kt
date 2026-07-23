package com.heveamobile.setsandsteps.core.domain.manager

enum class PermissionType {
    Notifications,
    Health
}

interface PermissionManager {
    suspend fun checkPermissionStatus(type: PermissionType): PermissionStatus
    fun openAppSettings()
    fun getRequiredPermissions(type: PermissionType): Set<String>
}
