package com.example.digital_diary.presentation.AddMemoryDialog

import com.example.digital_diary.data.MemoryEvent

data class AddMemoryDialogState(
    val isRecording: Boolean = false,
    val currentAudioPath: String? = null,
    val isPlaying: Boolean = false,
    val isAddingMood: Boolean = false,
)