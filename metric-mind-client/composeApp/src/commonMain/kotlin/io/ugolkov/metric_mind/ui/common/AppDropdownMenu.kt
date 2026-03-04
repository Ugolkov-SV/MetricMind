@file:OptIn(ExperimentalMaterial3Api::class)

package io.ugolkov.metric_mind.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import metric_mind_client.composeapp.generated.resources.Res
import metric_mind_client.composeapp.generated.resources.ic_arrow_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T : Any> AppDropdownMenu(
    value: String,
    items: List<MenuItem<T>>,
    onItemClick: (T) -> Unit,
    label: String,
) {
    var expanded by retain { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(text = label) },
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                )
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        expanded = false
                        onItemClick(item.value)
                    },
                )
            }
        }
    }
}

data class MenuItem<T : Any>(
    val name: String,
    val value: T,
)