package com.example.digital_diary.presentation.AddMemoryDialog

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Title
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.digital_diary.R
import com.example.digital_diary.data.MemoryEvent
import com.example.digital_diary.data.MemoryState
import com.example.digital_diary.ui.theme.BackgroundColor
import com.example.digital_diary.ui.theme.ButtonColor
import com.example.digital_diary.ui.theme.ThirdColor
import com.example.digital_diary.utils.VoiceRecorder
import com.example.digital_diary.utils.byteArrayToBitmap
import com.example.digital_diary.utils.drawTextOnBottom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemorySheet(
    memoryState: MemoryState,
    onMemoryEvent: (MemoryEvent) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit
) {
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
                        text = "tell me about it...",
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
                    onMemoryEvent(MemoryEvent.SaveMemory)
                    onDismiss()
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
                text = "what is your mood?",
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
                            onMemoryEvent(MemoryEvent.SetMood(index))
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
                    onMemoryEvent(MemoryEvent.SetPhotoPath(photoByteArray))
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

    if (addMemoryDialogState.isPainting && memoryState.photoPath.isNotEmpty()) {
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
                        text = "Text on photo",
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
                        val originalBitmap = byteArrayToBitmap(memoryState.photoPath)
                        val modifiedBitmap = drawTextOnBottom(
                            bitmap = originalBitmap,
                            text = addMemoryDialogState.paintedText
                        )
                        val stream = java.io.ByteArrayOutputStream()
                        modifiedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val updatedByteArray = stream.toByteArray()
                        onMemoryEvent(MemoryEvent.SetPhotoPath(updatedByteArray))
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
                        text = "Apply",
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
                        text = "Decline",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (memoryState.photoPath.isNotEmpty()) {
        val bitmap = byteArrayToBitmap(memoryState.photoPath)
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
                onClick = { onMemoryEvent(MemoryEvent.SetPhotoPath(byteArrayOf())) },
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(22.dp))
                            .background(ThirdColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(64.dp),
                            onClick = {
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
                            }
                        ) {
                            Image(
                                imageVector = if (!addMemoryDialogState.isPlaying) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                                contentDescription = if (addMemoryDialogState.isPlaying) "Pause" else "Play",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(id = R.drawable.sound_waves),
                            contentDescription = "sound waves",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(78.dp)
                        )
                    }
                    IconButton(
                        onClick = { onMemoryEvent(MemoryEvent.SetAudioPath(null)) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "remove photo",
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (memoryState.mood != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                                    painter = painterResource(memoryState.moodList[memoryState.mood]),
                                    contentDescription = "mood",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = { onMemoryEvent(MemoryEvent.SetMood(null)) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "remove photo",
                            tint = Color.White
                        )
                    }
                }
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
                if (!addMemoryDialogState.isPainting && memoryState.photoPath.isNotEmpty()) {
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