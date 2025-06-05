package com.example.digital_diary.presentation.landing

import android.app.Activity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Mic
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.digital_diary.R
import com.example.digital_diary.data.MemoryEvent
import com.example.digital_diary.data.MemoryViewModel
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogEvent
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogViewModel
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemorySheet
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
) {
    val state by landingViewModel.state.collectAsStateWithLifecycle()
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
                        value = state.searchBarInput,
                        onValueChange = landingViewModel::onSearchBarInputChange
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
                item {
                    Memory()
                }
                item {
                    Memory()
                }
            }
        }
        if (memoryState.isAddingMemory) {
            AddMemorySheet(
                memoryState = memoryState,
                onMemoryEvent = { memoryViewModel.onEvent(it) },
                sheetState = sheetState,
                onDismiss = { memoryViewModel.onEvent(MemoryEvent.HideDialog) },
                addMemoryDialogState = addMemoryDialogState,
                onAddMemoryDialogEvent = onAddMemoryDialogEvent,
                activity = activity
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
                text = "search for memories...",
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

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.b_krajobraz1),
                contentDescription = "memory main photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(22.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            MemoryRightSection()
        }
        Spacer(modifier = Modifier.height(26.dp))
        MemoryBottomSection()
    }
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun MemoryRightSection(

) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        MemoryRightSectionAudioChip(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(8.dp))
        MemoryRightSectionMoodChip(mood = R.drawable.love, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MemoryRightSectionMoodChip(
    mood: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(ThirdColor),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = mood!!),
            contentDescription = "mood",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
        )
        Image(
            painter = painterResource(id = mood!!),
            contentDescription = "mood",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
        )
    }
}

@Composable
fun MemoryRightSectionAudioChip(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(ThirdColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "image",
                modifier = Modifier
                    .size(42.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.sound_waves),
                contentDescription = "sound waves",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp)
            )
        }
    }
}

@Composable
fun MemoryBottomSection(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        Text(
            text = "We are thrilled to present our journal app design concept.\n" +

                    "Explore is an innovative journal app concept that seamlessly combines elegant design with powerful functionality to enhance the user's journaling experience. The app is meticulously crafted to cater to users seeking a visually appealing and intuitive platform to capture their thoughts, emotions, and memories.\n" +

                    "This innovative application offers users an intuitive interface for effortless navigation and a personalized experience through customizable themes and multimedia integration.",
            style = MaterialTheme.typography.displaySmall,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(ThirdColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                text = "Warszawa, 03 czerwca 2025",
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













