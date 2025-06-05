package com.example.digital_diary.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.digital_diary.ui.theme.ThirdColor
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun byteArrayToBitmap(byteArray: ByteArray) =
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

fun uriToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (stream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            byteArrayOutputStream.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun drawTextOnBottom(
    bitmap: Bitmap,
    text: String,
    textSizeRatio: Float = 0.15f,
    textColor: androidx.compose.ui.graphics.Color = Color.White,
    shadow: Boolean = true,
    shadowColor: androidx.compose.ui.graphics.Color = Color.Black,
    marginBottomRatio: Float = 0.05f
): Bitmap {
    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = android.graphics.Canvas(mutableBitmap)

    val paint = Paint().apply {
        color = textColor.toArgb()
        textSize = bitmap.height * textSizeRatio
        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        if (shadow) {
            setShadowLayer(8f, 0f, 0f, shadowColor.toArgb())
        }
    }

    val x = canvas.width / 2f
    val y = canvas.height - (bitmap.height * marginBottomRatio)

    canvas.drawText(text, x, y, paint)

    return mutableBitmap
}


