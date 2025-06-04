package com.example.digital_diary.presentation.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraEnhance
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.digital_diary.R
import com.example.digital_diary.data.MemoryEvent
import com.example.digital_diary.data.MemoryState
import com.example.digital_diary.ui.theme.BackgroundColor
import com.example.digital_diary.ui.theme.ButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemorySheet(
    state: MemoryState,
    onEvent: (MemoryEvent) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundColor,
        scrimColor = Color.Transparent,
    ){
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = state.description ?: "",
                onValueChange = { onEvent(MemoryEvent.SetDescription(it)) },
                textStyle = MaterialTheme.typography.titleSmall.copy(),
                placeholder = {
                    Text(
                        text = "tell me about it...",
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly

            ){
                Image(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(64.dp)
                )
                Image(
                    imageVector = Icons.Outlined.EmojiEmotions,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(64.dp)
                )
                Image(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(64.dp)
                )
                Image(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onEvent(MemoryEvent.SaveMemory)
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