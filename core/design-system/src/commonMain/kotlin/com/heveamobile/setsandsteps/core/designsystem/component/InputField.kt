package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    inputTransformation: InputTransformation? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    imeAction: ImeAction = ImeAction.Next,
) {
    OutlinedTextField(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        textStyle = textStyle,
        inputTransformation = inputTransformation,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Number,
            showKeyboardOnFocus = true,
            imeAction = imeAction,
        ),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
            ),
        ),
    )
}