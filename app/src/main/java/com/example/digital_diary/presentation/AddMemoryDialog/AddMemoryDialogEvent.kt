package com.example.digital_diary.presentation.AddMemoryDialog

sealed interface AddMemoryDialogEvent {
    object ShowPhotoPicker: AddMemoryDialogEvent
}