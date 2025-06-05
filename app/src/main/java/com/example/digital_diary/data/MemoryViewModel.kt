package com.example.digital_diary.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoryViewModel(
    private val dao: MemoryDao,

    ) : ViewModel() {
    /*private val _memories = */
    private val _state = MutableStateFlow(MemoryState())
    val state =
        _state.asStateFlow() // jak nie bedzie dzialac wyszukiwanie to TODO combine z _memories

    fun onEvent(event: MemoryEvent) {
        when (event) {
            is MemoryEvent.DeleteMemory -> {
                viewModelScope.launch {
                    dao.deleteMemory(event.memory)
                }
            }

            MemoryEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingMemory = false
                    )
                }
            }

            MemoryEvent.SaveMemory -> {
                val photoPath = _state.value.photoPath
                val audioPath = _state.value.audioPath
                val description = _state.value.description
                val mood = _state.value.mood
                val city = _state.value.city
                val date = _state.value.date

                val memory = Memory(
                    photoPath = photoPath,
                    audioPath = audioPath,
                    description = description,
                    mood = mood,
                    city = city,
                    date = date,
                )

                viewModelScope.launch {
                    dao.insertMemory(memory)
                }

                _state.update {
                    it.copy(
                        isAddingMemory = false,
                        photoPath = byteArrayOf(),
                        audioPath = null,
                        description = null,
                        mood = null,
                    )
                }
            }

            is MemoryEvent.SetAudioPath -> {
                _state.update {
                    it.copy(
                        audioPath = event.audioPath
                    )
                }
            }

            is MemoryEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            is MemoryEvent.SetMood -> {
                _state.update {
                    it.copy(
                        mood = event.mood
                    )
                }
            }

            is MemoryEvent.SetPhotoPath -> {
                _state.update {
                    it.copy(
                        photoPath = event.photoPath
                    )
                }
            }

            MemoryEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingMemory = true
                    )
                }
            }

            is MemoryEvent.SetCity -> {
                _state.update {
                    it.copy(
                        city = event.city
                    )
                }
            }

            is MemoryEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }
        }
    }
}