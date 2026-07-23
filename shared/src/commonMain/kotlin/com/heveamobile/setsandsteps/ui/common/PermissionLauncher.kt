package com.heveamobile.setsandsteps.ui.common

import androidx.compose.runtime.Composable
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType

@Composable
expect fun rememberPermissionLauncher(
    manager: PermissionManager,
    type: PermissionType,
    onResult: (PermissionStatus) -> Unit,
): () -> Unit
