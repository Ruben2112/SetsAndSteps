package com.heveamobile.setsandsteps.domain.manager

import androidx.compose.runtime.Composable
import com.heveamobile.setsandsteps.ui.common.PermissionStatus

enum class PermissionType {
    Notifications,
    Health
}

interface PermissionManager {
    suspend fun checkPermissionStatus(type: PermissionType): PermissionStatus
    fun openAppSettings()
    fun getRequiredPermissions(type: PermissionType): Set<String>
}

@Composable
expect fun rememberPermissionLauncher(
    manager: PermissionManager,
    type: PermissionType,
    onResult: (PermissionStatus) -> Unit,
): () -> Unit
