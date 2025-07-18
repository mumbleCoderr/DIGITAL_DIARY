package com.example.digital_diary.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.digital_diary.R

val HandWriteFontFamily = FontFamily(
    Font(R.font.architects_daughter, FontWeight.Black)
)

val Typography = Typography(
    bodyLarge = TextStyle(      // head title
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 70.sp,
        color = ButtonColor,
        textAlign = TextAlign.Center,
    ),
    bodySmall = TextStyle(      // body text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        color = AdditionalColor,
        textAlign = TextAlign.Center,
    ),
    titleLarge = TextStyle(      // body text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        color = ButtonColor,
        textAlign = TextAlign.Start,
    ),
    titleSmall = TextStyle(      // body text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        color = AdditionalColor,
        textAlign = TextAlign.Start,
    ),
    labelMedium = TextStyle(      // button text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        color = BackgroundColor,
        textAlign = TextAlign.Center,
    ),
     labelSmall = TextStyle(      // input text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = AdditionalColor,
        textAlign = TextAlign.Start,
    ),
    displaySmall = TextStyle(      // description text
        fontFamily = HandWriteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = ButtonColor,
        textAlign = TextAlign.Start,
    ),
)