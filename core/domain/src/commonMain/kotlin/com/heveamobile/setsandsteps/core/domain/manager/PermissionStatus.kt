package com.heveamobile.setsandsteps.core.domain.manager

sealed class PermissionStatus {
    data object Loading : PermissionStatus()
    data object Granted : PermissionStatus()
    data object NotGranted : PermissionStatus()
    data object RationaleRequired : PermissionStatus()
    data object NotInstalled : PermissionStatus()
}
