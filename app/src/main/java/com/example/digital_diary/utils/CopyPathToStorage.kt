package com.example.digital_diary.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

fun copyImageToAppStorage(context: Context, originalUri: Uri): String {
    val filename = "memory_${System.currentTimeMillis()}.jpg"
    val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val outputFile = File(outputDir, filename)

    context.contentResolver.openInputStream(originalUri)?.use { input ->
        FileOutputStream(outputFile).use { output ->
            input.copyTo(output)
        }
    }

    return outputFile.absolutePath
}

fun copyAudioFileToAppStorage(context: Context, originalAudioUri: Uri): String? {
    val fileExtension = context.contentResolver.getType(originalAudioUri)
        ?.substringAfterLast("/")
        ?: "mp3"

    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        ?: context.filesDir
    val outputFile = File(storageDir, "audio_${System.currentTimeMillis()}.$fileExtension")

    return try {
        context.contentResolver.openInputStream(originalAudioUri)?.use { inputStream ->
            FileOutputStream(outputFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        outputFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}