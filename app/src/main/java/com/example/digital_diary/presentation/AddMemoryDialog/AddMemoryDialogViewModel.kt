package com.example.digital_diary.presentation.AddMemoryDialog

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMemoryDialogViewModel(): ViewModel() {
    private val _state = MutableStateFlow(AddMemoryDialogState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddMemoryDialogEvent){
        when(event){
            is AddMemoryDialogEvent.SetCurrentAudioPath -> {
                _state.update {
                    it.copy(
                        currentAudioPath = event.currentAudioPath
                    )
                }
            }
            AddMemoryDialogEvent.StartRecording -> {
                _state.update {
                    it.copy(
                        isRecording = true
                    )
                }
            }
            AddMemoryDialogEvent.StopRecording -> {
                _state.update {
                    it.copy(
                        isRecording = false
                    )
                }
            }
            AddMemoryDialogEvent.StartPlaying -> {
                _state.update {
                    it.copy(
                        isPlaying = true
                    )
                }
            }
            AddMemoryDialogEvent.StopPlaying -> {
                _state.update {
                    it.copy(
                        isPlaying = false
                    )
                }
            }

            AddMemoryDialogEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingMood = false
                    )
                }
            }
            AddMemoryDialogEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingMood = true
                    )
                }
            }

            AddMemoryDialogEvent.StartPainting -> {
                _state.update {
                    it.copy(
                        isPainting = true
                    )
                }
            }
            AddMemoryDialogEvent.StopPainting -> {
                _state.update {
                    it.copy(
                        isPainting = false
                    )
                }
            }
            is AddMemoryDialogEvent.InputTextOnPhoto -> {
                _state.update {
                    it.copy(
                        paintedText = event.textOnPhoto
                    )
                }
            }
        }
    }
}