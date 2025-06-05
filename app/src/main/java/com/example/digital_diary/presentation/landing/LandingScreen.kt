package com.example.digital_diary.presentation.landing

import android.app.Activity
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.digital_diary.R
import com.example.digital_diary.data.Memory
import com.example.digital_diary.data.MemoryEvent
import com.example.digital_diary.data.MemoryState
import com.example.digital_diary.data.MemoryViewModel
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogEvent
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogState
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogViewModel
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogSheet
import com.example.digital_diary.presentation.AddMemoryDialog.AudioBox
import com.example.digital_diary.presentation.AddMemoryDialog.MoodBox
import com.example.digital_diary.presentation.sign_in.UserData
import com.example.digital_diary.ui.theme.BackgroundColor
import com.example.digital_diary.ui.theme.ButtonColor
import com.example.digital_diary.ui.theme.ThirdColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    userData: UserData?,
    navController: NavController,
    landingViewModel: LandingViewModel = viewModel<LandingViewModel>(),
    memoryViewModel: MemoryViewModel = viewModel<MemoryViewModel>(),
    addMemoryDialogViewModel: AddMemoryDialogViewModel = viewModel<AddMemoryDialogViewModel>(),
    onMemoryEvent: (MemoryEvent) -> Unit,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit,
    activity: Activity,
    onLandingEvent: (LandingEvent) -> Unit,
) {
    val landingState by landingViewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val memoryState by memoryViewModel.state.collectAsStateWithLifecycle()
    val addMemoryDialogState by addMemoryDialogViewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .then(
                if (memoryState.isAddingMemory) {
                    Modifier
                        .graphicsLayer { alpha = 0.99f }
                        .blur(4.dp)
                } else {
                    Modifier
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar(
                userData = userData,
                onProfileClick = { navController.navigate("profile") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    SearchBar(
                        value = landingState.searchBarInput,
                        onValueChange = {}/*landingViewModel::onSearchBarInputChange*/
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    AddMemory(
                        onAddMemoryClick = {
                            onMemoryEvent(MemoryEvent.ShowDialog)
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
                itemsIndexed(memoryState.memories) { _, memory ->
                    Memory(
                        memory = memory,
                        memoryState = memoryState,
                        addMemoryDialogState = addMemoryDialogState,
                        onAddMemoryDialogEvent = onAddMemoryDialogEvent,
                        onMemoryEvent = onMemoryEvent,
                        onLandingEvent = onLandingEvent,
                        landingState = landingState,
                    )
                }
            }
        }
        if (memoryState.isAddingMemory) {
            AddMemoryDialogSheet(
                memoryState = memoryState,
                onMemoryEvent = { memoryViewModel.onEvent(it) },
                sheetState = sheetState,
                onDismiss = {
                    memoryViewModel.onEvent(MemoryEvent.HideDialog)
                    if(landingState.memoryToEdit != null){
                        onMemoryEvent(MemoryEvent.ClearMemory)
                        onLandingEvent(LandingEvent.SetMemoryToEdit(null))
                    }
                            },
                addMemoryDialogState = addMemoryDialogState,
                onAddMemoryDialogEvent = onAddMemoryDialogEvent,
                activity = activity,
                landingState = landingState,
                onLandingEvent = onLandingEvent
            )
        }
    }
}

@Composable
fun TopBar(
    userData: UserData?,
    onProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.landing_subtitle),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable { onProfileClick() },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelSmall.copy(),
        placeholder = {
            Text(
                text = stringResource(R.string.searchbar_placeholder),
                style = MaterialTheme.typography.labelSmall,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "search icon",
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun AddMemory(
    onAddMemoryClick: () -> Unit,
) {
    Button(
        onClick = onAddMemoryClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor,
        ),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.add_memory_button),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun Memory(
    memory: Memory,
    memoryState: MemoryState,
    onMemoryEvent: (MemoryEvent) -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit,
    onLandingEvent: (LandingEvent) -> Unit,
    landingState: LandingState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                onLandingEvent(LandingEvent.SetMemoryToEdit(memory.id))
                onMemoryEvent(MemoryEvent.ShowDialog)
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (memory.photoPath != null) Modifier.height(160.dp) else Modifier)
        ) {
            if (memory.photoPath != null) {
                Image(
                    bitmap = BitmapFactory.decodeFile(memory.photoPath).asImageBitmap(),
                    contentDescription = "memory main photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clip(RoundedCornerShape(22.dp))
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            MemoryRightSection(
                memory = memory,
                onMemoryEvent = onMemoryEvent,
                addMemoryDialogState = addMemoryDialogState,
                onAddMemoryDialogEvent = onAddMemoryDialogEvent,
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        MemoryBottomSection(memory)
    }
    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
fun MemoryRightSection(
    memory: Memory,
    onMemoryEvent: (MemoryEvent) -> Unit,
    addMemoryDialogState: AddMemoryDialogState,
    onAddMemoryDialogEvent: (AddMemoryDialogEvent) -> Unit
) {
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (memory.audioPath != null) {
            AudioBox(
                isPlaying = addMemoryDialogState.isPlaying,
                onPlayPauseToggle = {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(memory.audioPath)
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
                onRemoveAudio = {  },
                showRemoveButton = false,
                fillMaxWidth = true,
                fillMaxHeight = false
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (memory.mood != null) {
            MoodBox(
                mood = memory.mood,
                onRemoveMood = { onMemoryEvent(MemoryEvent.SetMood(null)) },
                showRemoveButton = false
            )
        }
    }
}

@Composable
fun MemoryBottomSection(
    memory: Memory
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        if (memory.description != null) {
            Text(
                text = memory.description,
                style = MaterialTheme.typography.displaySmall,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ThirdColor)
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${memory.city ?: ""}, ${memory.date}",
                style = MaterialTheme.typography.titleSmall
            )
            Image(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = "bookmark",
                modifier = Modifier
                    .size(36.dp)
            )
        }
    }
}













