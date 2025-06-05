package com.example.digital_diary.presentation.landing

import androidx.lifecycle.ViewModel
import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LandingViewModel: ViewModel() {
    private val _state = MutableStateFlow(LandingState())
    val state = _state.asStateFlow()

    fun onEvent(event: LandingEvent){
        when(event){
            is LandingEvent.SetCurrentAudioPath -> {
                _state.update {
                    it.copy(
                        currentAudioPath = event.currentAudioPath
                    )
                }
            }
            is LandingEvent.SetSearchBarInput -> TODO()
            LandingEvent.StartPlaying -> {
                _state.update {
                    it.copy(
                        isPlaying = true
                    )
                }
            }
            LandingEvent.StopPlaying -> {
                _state.update {
                    it.copy(
                        isPlaying = false
                    )
                }
            }

            is LandingEvent.SetMemoryToEdit -> {
                _state.update {
                    it.copy(
                        memoryToEdit = event.id?.minus(1)
                    )
                }
            }
        }
    }
}