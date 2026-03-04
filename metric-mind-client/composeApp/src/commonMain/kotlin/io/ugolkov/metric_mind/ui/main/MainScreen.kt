@file:OptIn(ExperimentalMaterial3Api::class)

package io.ugolkov.metric_mind.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.ugolkov.metric_mind.data.model.TrackType
import io.ugolkov.metric_mind.data.model.Visibility
import io.ugolkov.metric_mind.ui.common.AppDialog
import io.ugolkov.metric_mind.ui.common.AppDropdownMenu
import io.ugolkov.metric_mind.ui.common.AppThemePreview
import io.ugolkov.metric_mind.ui.common.ClickableOutlinedTextField
import io.ugolkov.metric_mind.ui.common.MenuItem
import io.ugolkov.metric_mind.ui.common.ThemePreview
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackCreate
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackDelete
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackRecordCreate
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackRecordDelete
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackSelect
import io.ugolkov.metric_mind.util.Callback
import io.ugolkov.metric_mind.util.Event
import io.ugolkov.metric_mind.util.format
import io.ugolkov.metric_mind.util.now
import io.ugolkov.metric_mind.util.toLocalDate
import io.ugolkov.metric_mind.util.toTimestampMillis
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import metric_mind_client.composeapp.generated.resources.Res
import metric_mind_client.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel<MainViewModel>()) {
    var dialogType by retain { mutableStateOf<DialogType?>(null) }

    Content(
        trackIdSelected = viewModel.trackIdSelected,
        tracks = viewModel.tracksUiState,
        trackRecords = viewModel.trackRecordsUiState,
        onEvent = viewModel::onEvent,
        onShowDialog = { dialogType = it },
    )

    Crossfade(
        targetState = dialogType
    ) {
        when (it) {
            DialogType.TRACK -> TrackDialog(
                onDismissRequest = { dialogType = null },
                onEvent = viewModel::onEvent,
            )

            DialogType.TRACK_RECORD -> TrackRecordDialog(
                onDismissRequest = { dialogType = null },
                onEvent = viewModel::onEvent,
            )

            null -> Unit
        }
    }
}

@Composable
private fun Content(
    trackIdSelected: Long,
    tracks: List<TrackUiModel>,
    trackRecords: List<TrackRecordUiModel>,
    onEvent: Event<MainEvent>,
    onShowDialog: Event<DialogType>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier.height(48.dp),
        ) {
            items(
                items = tracks,
                key = { it.id },
            ) {
                TrackItem(
                    track = it,
                    selected = trackIdSelected == it.id,
                    onEvent = onEvent,
                )
            }
            item {
                Button(
                    onClick = { onShowDialog(DialogType.TRACK) },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Text(text = "+")
                }
            }
        }
        if (trackIdSelected != -1L) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                item {
                    Button(
                        onClick = { onShowDialog(DialogType.TRACK_RECORD) },
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "+")
                    }
                }
                items(
                    items = trackRecords,
                    key = { it.id },
                ) {
                    TrackRecordItem(
                        trackRecord = it,
                        onEvent = onEvent,
                    )
                }
            }
        } else {
            Text(
                text = "No tracks",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun TrackItem(
    track: TrackUiModel,
    selected: Boolean,
    onEvent: Event<MainEvent>,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .height(48.dp)
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer
            )
            .clickable { onEvent(TrackSelect(track.id)) }
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
    ) {
        Text(text = track.title)
        IconButton(
            onClick = { onEvent(TrackDelete(track.id)) },
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = "Delete track",
            )
        }
    }
}

@Composable
private fun TrackRecordItem(
    trackRecord: TrackRecordUiModel,
    onEvent: Event<MainEvent>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .height(48.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = trackRecord.value
        )
        Text(
            text = "${trackRecord.time} ${trackRecord.date}"
        )
        IconButton(
            onClick = { onEvent(TrackRecordDelete(trackRecord.id)) },
            modifier = Modifier.size(24.dp)
                .padding(4.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = "Delete track",
            )
        }
    }
}

@Composable
private fun TrackDialog(
    onDismissRequest: Callback,
    onEvent: Event<MainEvent>,
) {
    AppDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            var title by retain { mutableStateOf("") }
            var titleError by retain { mutableStateOf(false) }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Title") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                singleLine = true,
                isError = titleError,
            )

            var trackType by retain { mutableStateOf(TrackType.NUMBER) }
            AppDropdownMenu(
                value = trackType.name,
                items = rememberTrackTypes(),
                onItemClick = { trackType = it },
                label = "Type"
            )

            var unit by retain { mutableStateOf("") }
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text(text = "Unit") },
                singleLine = true,
            )

            var visibility by retain { mutableStateOf(Visibility.PRIVATE) }
            AppDropdownMenu(
                value = visibility.name,
                items = rememberVisibilities(),
                onItemClick = { visibility = it },
                label = "Visibility"
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.End),
            ) {
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    Text(text = "Cancel")
                }
                TextButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            onDismissRequest()
                            onEvent(TrackCreate(title, trackType, unit, visibility))
                        } else {
                            titleError = title.isBlank()
                        }
                    },
                ) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Composable
private fun TrackRecordDialog(
    onDismissRequest: Callback,
    onEvent: Event<MainEvent>,
) {
    AppDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            var value by retain { mutableStateOf("") }
            var valueError by retain { mutableStateOf(false) }
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    value = newValue.takeIf { it.toDoubleOrNull() != null }
                        ?: value
                },
                label = { Text(text = "Value") },
                isError = valueError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            var dateTime by retain { mutableStateOf(LocalDateTime.now()) }
            var timePickerDialog by retain { mutableStateOf(false) }
            var time by retain(dateTime) { mutableStateOf(dateTime.time.format()) }
            ClickableOutlinedTextField(
                value = time,
                label = "Time",
                onClick = { timePickerDialog = true },
            )
            if (timePickerDialog) {
                val state = rememberTimePickerState(
                    initialHour = dateTime.hour,
                    initialMinute = dateTime.minute,
                )
                TimePickerDialog(
                    onDismissRequest = { timePickerDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                timePickerDialog = false
                                dateTime = LocalDateTime(
                                    dateTime.date,
                                    LocalTime(state.hour, state.minute)
                                )
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { timePickerDialog = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    },
                    title = {}
                ) {
                    TimePicker(
                        state = state,
                        layoutType = TimePickerLayoutType.Vertical,
                        modifier = Modifier
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                            }
                    )
                }
            }

            var datePickerDialog by retain { mutableStateOf(false) }
            var date by retain(dateTime) { mutableStateOf(dateTime.date.format()) }
            ClickableOutlinedTextField(
                value = date,
                label = "Date",
                onClick = { datePickerDialog = true },
            )
            if (datePickerDialog) {
                val state = rememberDatePickerState(
                    initialSelectedDateMillis = dateTime.date.toTimestampMillis(),
                )
                DatePickerDialog(
                    onDismissRequest = { datePickerDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerDialog = false
                                dateTime = LocalDateTime(
                                    state.selectedDateMillis!!.toLocalDate(),
                                    dateTime.time
                                )
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { datePickerDialog = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    },
                ) {
                    DatePicker(state = state)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.End),
            ) {
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    Text(text = "Cancel")
                }
                TextButton(
                    onClick = {
                        if (value.isNotBlank()) {
                            onDismissRequest()
                            onEvent(TrackRecordCreate(value, dateTime))
                        } else {
                            valueError = value.isBlank()
                        }
                    },
                ) {
                    Text(text = "Create")
                }
            }
        }
    }
}

private enum class DialogType {
    TRACK,
    TRACK_RECORD
}

@Composable
private fun rememberTrackTypes(): List<MenuItem<TrackType>> =
    remember {
        TrackType.entries
            .map { MenuItem(it.name, it) }
    }

@Composable
private fun rememberVisibilities(): List<MenuItem<Visibility>> =
    remember {
        Visibility.entries
            .map { MenuItem(it.name, it) }
    }

@ThemePreview
@Composable
private fun ContentPreview() {
    val tracks = listOf(
        TrackUiModel(
            id = 0L,
            title = "Track 1",
            trackType = TrackType.NUMBER,
        ),
        TrackUiModel(
            id = 1L,
            title = "Track 2",
            trackType = TrackType.NUMBER,
        ),
        TrackUiModel(
            id = 2L,
            title = "Track 3",
            trackType = TrackType.NUMBER,
        ),
        TrackUiModel(
            id = 3L,
            title = "Track 4",
            trackType = TrackType.NUMBER,
        ),
        TrackUiModel(
            id = 4L,
            title = "Track 5",
            trackType = TrackType.NUMBER,
        ),
    )
    AppThemePreview {
        Content(
            trackIdSelected = tracks.first().id,
            tracks = tracks,
            trackRecords = listOf(
                TrackRecordUiModel(
                    id = 0L,
                    value = "62.2 kg",
                    date = "2020-01-01",
                    time = "12:00",
                )
            ),
            onEvent = {},
            onShowDialog = {},
        )
    }
}

@ThemePreview
@Composable
private fun TrackDialogPreview() {
    AppThemePreview {
        TrackDialog(
            onDismissRequest = {},
            onEvent = {},
        )
    }
}

@ThemePreview
@Composable
private fun TrackRecordDialogPreview() {
    AppThemePreview {
        TrackRecordDialog(
            onDismissRequest = {},
            onEvent = {},
        )
    }
}
