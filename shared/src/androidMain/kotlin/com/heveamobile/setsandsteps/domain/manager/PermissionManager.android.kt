package com.heveamobile.setsandsteps.domain.manager

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType

class AndroidPermissionManager(private val context: Context) : PermissionManager {

    override suspend fun checkPermissionStatus(type: PermissionType): PermissionStatus {
        return when (type) {
            PermissionType.Notifications -> checkNotificationPermission()
            PermissionType.Health -> checkHealthPermission()
        }
    }

    override fun getRequiredPermissions(type: PermissionType): Set<String> {
        return when (type) {
            PermissionType.Notifications -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    setOf(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    emptySet()
                }
            }

            PermissionType.Health -> {
                setOf(
                    HealthPermission.getReadPermission(StepsRecord::class),
                    "android.permission.health.READ_HEALTH_DATA_HISTORY",
                    "android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND",
                )
            }
        }
    }

    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(
                "package",
                context.packageName,
                null,
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun checkNotificationPermission(): PermissionStatus {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return PermissionStatus.Granted
        }

        val permission = Manifest.permission.POST_NOTIFICATIONS
        return when {
            ContextCompat.checkSelfPermission(
                context,
                permission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                PermissionStatus.Granted
            }

            context is ComponentActivity && ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                permission,
            ) -> {
                PermissionStatus.RationaleRequired
            }

            else -> {
                PermissionStatus.NotGranted
            }
        }
    }

    private suspend fun checkHealthPermission(): PermissionStatus {
        val availability = HealthConnectClient.getSdkStatus(context)
        if (availability == HealthConnectClient.SDK_UNAVAILABLE) return PermissionStatus.NotInstalled

        val client = HealthConnectClient.getOrCreate(context)
        return try {
            val granted = client.permissionController.getGrantedPermissions()
            if (granted.containsAll(getRequiredPermissions(PermissionType.Health))) {
                PermissionStatus.Granted
            } else {
                PermissionStatus.NotGranted
            }
        } catch (e: Exception) {
            PermissionStatus.NotGranted
        }
    }
}
