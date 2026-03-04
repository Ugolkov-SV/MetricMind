package io.ugolkov.metric_mind.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ClickableOutlinedTextField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(text = label) },
        readOnly = true,
        interactionSource = interactionSource,
        modifier = modifier,
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions
            .collect { interaction ->
                if (interaction is PressInteraction.Release) {
                    onClick()
                }
            }
    }
}