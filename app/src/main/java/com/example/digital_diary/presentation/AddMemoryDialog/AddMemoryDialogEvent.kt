package com.example.digital_diary.presentation.AddMemoryDialog

sealed interface AddMemoryDialogEvent {
    object StartRecording: AddMemoryDialogEvent
    object StopRecording: AddMemoryDialogEvent
    data class SetCurrentAudioPath(val currentAudioPath: String): AddMemoryDialogEvent
    data object StartPlaying: AddMemoryDialogEvent
    object StopPlaying: AddMemoryDialogEvent
    object ShowDialog: AddMemoryDialogEvent
    object HideDialog: AddMemoryDialogEvent

    object StartPainting: AddMemoryDialogEvent
    object StopPainting: AddMemoryDialogEvent
    data class InputTextOnPhoto(val textOnPhoto: String): AddMemoryDialogEvent
}