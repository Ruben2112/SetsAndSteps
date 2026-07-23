package com.heveamobile.setsandsteps.core.presentation

import androidx.compose.runtime.Composable
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler

@Composable
expect fun FilePickerHandlerEffect(handler: FilePickerHandler)
