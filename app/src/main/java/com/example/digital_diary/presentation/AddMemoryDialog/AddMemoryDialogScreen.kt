package com.example.digital_diary.presentation.AddMemoryDialog

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.digital_diary.R
import com.example.digital_diary.data.MemoryEvent
import com.example.digital_diary.data.MemoryState
import com.example.digital_diary.presentation.landing.LandingEvent
import com.example.digital_diary.presentation.landing.LandingState
import com.example.digital_diary.ui.theme.BackgroundColor
import com.example.digital_diary.ui.theme.ButtonColor
import com.example.digital_diary.ui.theme.ThirdColor
import com.example.digital_diary.utils.LocationUtils
import com.example.digital_diary.utils.VoiceRecorder
import com.example.digital_diary.utils.byteArrayToBitmap
import com.example.digital_diary.utils.drawTextOnBottom
import com.example.digital_diary.utils.saveBitmapToInternalStorage
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryDialogSheet(
    memoryState: MemoryState,
    onMemoryEvent: (MemoryEvent) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit,
    activity: Activity,
    landingState: LandingState,
    onLandingEvent: (LandingEvent) -> Unit
) {
    val context = LocalContext.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                LocationUtils.getLastKnownLocation(activity) { location ->
                    val cityName = LocationUtils.getCityName(context, location)
                    onMemoryEvent(MemoryEvent.SetCity(cityName))
                    onMemoryEvent(MemoryEvent.SaveMemory)
                    onDismiss()
                }
            } else {
                onMemoryEvent(MemoryEvent.SaveMemory)
                onDismiss()
            }
        }
    )

    if (landingState.memoryToEdit != null) {
        LaunchedEffect(Unit) {
            onMemoryEvent(MemoryEvent.SetPhotoPath(memoryState.memories[landingState.memoryToEdit].photoPath))
            onMemoryEvent(MemoryEvent.SetAudioPath(memoryState.memories[landingState.memoryToEdit].audioPath))
            onMemoryEvent(
                MemoryEvent.SetDescription(
                    memoryState.memories[landingState.memoryToEdit].description ?: ""
                )
            )
            onMemoryEvent(MemoryEvent.SetMood(memoryState.memories[landingState.memoryToEdit].mood))
            onMemoryEvent(MemoryEvent.SetCity(memoryState.memories[landingState.memoryToEdit].city))
            onMemoryEvent(MemoryEvent.SetDate(memoryState.memories[landingState.memoryToEdit].date))
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundColor,
        scrimColor = Color.Transparent,
        modifier = Modifier
            .fillMaxHeight(0.90f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            TextField(
                value = memoryState.description ?: "",
                onValueChange = { onMemoryEvent(MemoryEvent.SetDescription(it)) },
                textStyle = MaterialTheme.typography.titleSmall.copy(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.textfield_placeholder),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = BackgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            MediaPicker(
                onMemoryEvent = onMemoryEvent,
                memoryState = memoryState,
                onAddMemoryDialogEvent = onAddMemoryDialogEvent,
                addMemoryDialogState = addMemoryDialogState,
                onDismiss = { onAddMemoryDialogEvent(AddMemoryDialogEvent.HideDialog) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onMemoryEvent(MemoryEvent.SetDate(LocalDate.now().toString()))
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        LocationUtils.getLastKnownLocation(activity) { location ->
                            val cityName = LocationUtils.getCityName(context, location)
                            onMemoryEvent(MemoryEvent.SetCity(cityName))
                            onMemoryEvent(MemoryEvent.SaveMemory)
                            onDismiss()
                        }
                    } else {
                        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor,
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.save_memory_button),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodPickerDialog(
    onDismiss: () -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit,
    memoryState: MemoryState,
    onMemoryEvent: (MemoryEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = stringResource(R.string.picking_mood_alert_title),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = ThirdColor,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        },
        containerColor = ButtonColor,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(memoryState.moodList.size) { index ->
                    IconButton(
                        onClick = {
                            onMemoryEvent(MemoryEvent.SetMood(memoryState.moodList[index]))
                            onAddMemoryDialogEvent(AddMemoryDialogEvent.HideDialog)
                        },
                        modifier = Modifier
                            .aspectRatio(1f)
                    ) {
                        Image(
                            painter = painterResource(id = memoryState.moodList[index]),
                            contentDescription = "Mood",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPicker(
    onMemoryEvent: (MemoryEvent) -> Unit,
    memoryState: MemoryState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val photoByteArray =
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        inputStream.readBytes()
                    }
                if (photoByteArray != null) {
                    val photoPath =
                        saveBitmapToInternalStorage(context, byteArrayToBitmap(photoByteArray))
                    onMemoryEvent(MemoryEvent.SetPhotoPath(photoPath))
                }
            }
        }
    )
    val voiceRecorder = remember { VoiceRecorder(context) }
    val micPermission = android.Manifest.permission.RECORD_AUDIO
    val micPermissionGranted = remember {
        mutableStateOf(
            android.content.pm.PackageManager.PERMISSION_GRANTED ==
                    context.checkSelfPermission(micPermission)
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            micPermissionGranted.value = granted
        }
    )
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    if (addMemoryDialogState.isPainting && memoryState.photoPath != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = addMemoryDialogState.paintedText,
                onValueChange = { onAddMemoryDialogEvent(AddMemoryDialogEvent.InputTextOnPhoto(it)) },
                label = {
                    Text(
                        text = stringResource(R.string.painting_textfield_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val originalBitmap = BitmapFactory.decodeFile(memoryState.photoPath)
                        val modifiedBitmap = drawTextOnBottom(
                            bitmap = originalBitmap,
                            text = addMemoryDialogState.paintedText
                        )
                        val stream = java.io.ByteArrayOutputStream()
                        modifiedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val updatedByteArray = stream.toByteArray()

                        onMemoryEvent(
                            MemoryEvent.SetPhotoPath(
                                saveBitmapToInternalStorage(
                                    context,
                                    byteArrayToBitmap(updatedByteArray)
                                )
                            )
                        )
                        onAddMemoryDialogEvent(AddMemoryDialogEvent.StopPainting)
                        onAddMemoryDialogEvent(AddMemoryDialogEvent.InputTextOnPhoto(""))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColor,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = stringResource(R.string.apply_text_button),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 20.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onAddMemoryDialogEvent(AddMemoryDialogEvent.InputTextOnPhoto(""))
                        onAddMemoryDialogEvent(AddMemoryDialogEvent.StopPainting)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColor,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.decline_text_button),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (memoryState.photoPath != null) {
        val bitmap = BitmapFactory.decodeFile(memoryState.photoPath)
        val imageWidth = bitmap.width.toFloat()

        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val density = LocalDensity.current
        val screenWidthPx = with(density) { screenWidthDp.toPx() }
        val threeQuartersScreenPx = screenWidthPx * 0.75f

        val contentScale =
            if (imageWidth > threeQuartersScreenPx) ContentScale.FillBounds else ContentScale.Fit
        val imageAlignment =
            if (imageWidth > threeQuartersScreenPx) Alignment.Center else Alignment.CenterStart

        Box(
            modifier = Modifier
                .height(200.dp)
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "photo",
                contentScale = contentScale,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(imageAlignment)
                    .clip(RoundedCornerShape(22.dp))
            )
            IconButton(
                onClick = { onMemoryEvent(MemoryEvent.SetPhotoPath(null)) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove photo",
                    tint = Color.White
                )
            }
        }
    }

    if (addMemoryDialogState.isAddingMood) {
        MoodPickerDialog(
            onDismiss = onDismiss,
            addMemoryDialogState = addMemoryDialogState,
            onAddMemoryDialogEvent = onAddMemoryDialogEvent,
            memoryState = memoryState,
            onMemoryEvent = onMemoryEvent,
        )
    }
    if (memoryState.audioPath != null || memoryState.mood != null) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (memoryState.audioPath != null) {
                AudioBox(
                    isPlaying = addMemoryDialogState.isPlaying,
                    onPlayPauseToggle = {
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(addMemoryDialogState.currentAudioPath)
                                prepare()
                                setOnCompletionListener {
                                    onAddMemoryDialogEvent(AddMemoryDialogEvent.StopPlaying)
                                    release()
                                    mediaPlayer = null
                                }
                            }
                        }
                        mediaPlayer?.let { player ->
                            if (player.isPlaying) {
                                player.pause()
                                onAddMemoryDialogEvent(AddMemoryDialogEvent.StopPlaying)
                            } else {
                                player.start()
                                onAddMemoryDialogEvent(AddMemoryDialogEvent.StartPlaying)
                            }
                        }
                    },
                    onRemoveAudio = { onMemoryEvent(MemoryEvent.SetAudioPath(null)) }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (memoryState.mood != null) {
                MoodBox(
                    mood = memoryState.mood,
                    onRemoveMood = { onMemoryEvent(MemoryEvent.SetMood(null)) }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        IconButton(
            modifier = Modifier
                .size(64.dp),
            onClick = {
                if (!addMemoryDialogState.isRecording) {
                    if (micPermissionGranted.value) {
                        onAddMemoryDialogEvent(
                            AddMemoryDialogEvent.SetCurrentAudioPath(
                                voiceRecorder.startRecording()
                            )
                        )
                        onAddMemoryDialogEvent(AddMemoryDialogEvent.StartRecording)
                    } else {
                        permissionLauncher.launch(micPermission)
                    }
                } else {
                    val path = voiceRecorder.stopRecording()
                    onAddMemoryDialogEvent(AddMemoryDialogEvent.StopRecording)
                    if (path != null) {
                        onMemoryEvent(MemoryEvent.SetAudioPath(path))
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "mic",
                tint = if (addMemoryDialogState.isRecording) Color.Red else Color.Black,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(
            modifier = Modifier
                .size(64.dp),
            onClick = {
                if (addMemoryDialogState.isAddingMood) {
                    onAddMemoryDialogEvent(AddMemoryDialogEvent.HideDialog)
                } else {
                    onAddMemoryDialogEvent(AddMemoryDialogEvent.ShowDialog)
                }
            }
        ) {
            Image(
                imageVector = Icons.Outlined.EmojiEmotions,
                contentDescription = "emoji",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        IconButton(
            modifier = Modifier
                .size(64.dp),
            onClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Image(
                imageVector = Icons.Outlined.Image,
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        IconButton(
            modifier = Modifier
                .size(64.dp),
            onClick = {
                if (!addMemoryDialogState.isPainting && memoryState.photoPath != null) {
                    onAddMemoryDialogEvent(AddMemoryDialogEvent.StartPainting)
                } else {
                    onAddMemoryDialogEvent(AddMemoryDialogEvent.StopPainting)
                }
            }
        ) {
            Image(
                imageVector = Icons.Outlined.TextFields,
                contentDescription = "title",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun AudioBox(
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onRemoveAudio: () -> Unit,
    showRemoveButton: Boolean = true,
    fillMaxHeight: Boolean = true,
    fillMaxWidth: Boolean = false,
) {
    Box(
        modifier = Modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier.fillMaxWidth(0.5f))
            .then(if (fillMaxHeight) Modifier.fillMaxHeight() else Modifier.fillMaxHeight(0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp))
                .background(ThirdColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(64.dp),
                onClick = onPlayPauseToggle
            ) {
                Icon(
                    imageVector = if (!isPlaying) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Image(
                painter = painterResource(id = R.drawable.sound_waves),
                contentDescription = "sound waves",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(78.dp)
            )
        }
        if (showRemoveButton) {
            IconButton(
                onClick = onRemoveAudio,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(22.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove audio",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun MoodBox(
    mood: Int,
    onRemoveMood: () -> Unit,
    showRemoveButton: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(ThirdColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) {
                    Image(
                        painter = painterResource(id = mood),
                        contentDescription = "mood",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
        if (showRemoveButton) {
            IconButton(
                onClick = onRemoveMood,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(22.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove mood",
                    tint = Color.White
                )
            }
        }
    }
}

