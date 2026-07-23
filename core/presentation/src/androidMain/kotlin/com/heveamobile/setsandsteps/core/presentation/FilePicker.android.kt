package com.heveamobile.setsandsteps.core.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler

@Composable
actual fun FilePickerHandlerEffect(handler: FilePickerHandler) {
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
    ) { uri ->
        handler.onResult(
            uri?.toString(),
            isExport = true,
        )
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        handler.onResult(
            uri?.toString(),
            isExport = false,
        )
    }

    LaunchedEffect(handler) {
        handler.setListeners(
            onExport = { fileName ->
                exportLauncher.launch(fileName)
            },
            onImport = {
                importLauncher.launch(arrayOf("*/*"))
            },
        )
    }
}
