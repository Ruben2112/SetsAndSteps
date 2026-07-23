package com.heveamobile.setsandsteps.ui.common

sealed class PermissionStatus {
    data object Loading : PermissionStatus()
    data object Granted : PermissionStatus()
    data object NotGranted : PermissionStatus()
    data object RationaleRequired : PermissionStatus()
    data object NotInstalled : PermissionStatus()
}
