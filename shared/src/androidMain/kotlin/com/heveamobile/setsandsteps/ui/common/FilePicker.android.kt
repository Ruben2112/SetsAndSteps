package com.heveamobile.setsandsteps.ui.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.heveamobile.setsandsteps.data.source.android.AndroidFilePickerHandler
import com.heveamobile.setsandsteps.domain.repository.FilePickerHandler

@Composable
actual fun FilePickerHandlerEffect(handler: FilePickerHandler) {
    val androidHandler = handler as AndroidFilePickerHandler

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
    ) { uri ->
        androidHandler.onResult(
            uri,
            isExport = true,
        )
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        androidHandler.onResult(
            uri,
            isExport = false,
        )
    }

    LaunchedEffect(androidHandler) {
        androidHandler.setListeners(
            onExport = { fileName ->
                exportLauncher.launch(fileName)
            },
            onImport = {
                importLauncher.launch(arrayOf("*/*"))
            },
        )
    }
}
