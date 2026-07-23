package com.heveamobile.setsandsteps.core.presentation

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.health.connect.client.PermissionController
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import kotlinx.coroutines.launch

@Composable
actual fun rememberPermissionLauncher(
    manager: PermissionManager,
    type: PermissionType,
    onResult: (PermissionStatus) -> Unit,
): () -> Unit {
    val scope = rememberCoroutineScope()

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ ->
        scope.launch {
            onResult(manager.checkPermissionStatus(type))
        }
    }

    val healthLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
    ) { _ ->
        scope.launch {
            onResult(manager.checkPermissionStatus(type))
        }
    }

    return {
        when (type) {
            PermissionType.Notifications -> {
                val perms = manager.getRequiredPermissions(type)
                if (perms.isNotEmpty()) {
                    notificationLauncher.launch(perms.first())
                }
            }

            PermissionType.Health -> {
                @Suppress("UNCHECKED_CAST")
                val launcher = healthLauncher as ManagedActivityResultLauncher<Set<String>, *>
                launcher.launch(manager.getRequiredPermissions(type))
            }
        }
    }
}
