package com.heveamobile.setsandsteps.ui.common

import androidx.compose.runtime.Composable
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler

@Composable
expect fun FilePickerHandlerEffect(handler: FilePickerHandler)
