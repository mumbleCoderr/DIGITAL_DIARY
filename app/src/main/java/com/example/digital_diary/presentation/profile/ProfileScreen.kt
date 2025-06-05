package com.example.digital_diary.presentation.profile

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.digital_diary.R
import com.example.digital_diary.presentation.sign_in.UserData
import com.example.digital_diary.ui.theme.BackgroundColor
import com.example.digital_diary.ui.theme.ButtonColor

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 220.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (userData?.username != null) {
                Text(
                    text = userData.username,
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
            Button(
                onClick = onSignOut,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor,
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.log_out_button),
                    style = MaterialTheme.typography.labelMedium
                )
            }
    }
}