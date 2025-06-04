package com.example.digital_diary.presentation.AddMemoryDialog

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddMemoryDialogViewModel(): ViewModel() {
    private val _state = MutableStateFlow(AddMemoryDialogState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddMemoryDialogEvent){
        when(event){
            is AddMemoryDialogEvent.ShowPhotoPicker -> {
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            }
        }
    }
}