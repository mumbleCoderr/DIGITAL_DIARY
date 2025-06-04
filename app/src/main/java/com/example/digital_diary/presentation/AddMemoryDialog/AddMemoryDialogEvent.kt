package com.example.digital_diary.presentation.AddMemoryDialog

sealed interface AddMemoryDialogEvent {
    object StartRecording: AddMemoryDialogEvent
    object StopRecording: AddMemoryDialogEvent
    data class SetCurrentAudioPath(val currentAudioPath: String): AddMemoryDialogEvent
    object StartPlaying: AddMemoryDialogEvent
    object StopPlaying: AddMemoryDialogEvent
}