package com.example.digital_diary.presentation.landing

import com.example.digital_diary.presentation.AddMemoryDialog.AddMemoryDialogEvent

sealed interface LandingEvent {
    data class SetCurrentAudioPath(val currentAudioPath: String): LandingEvent
    data class SetSearchBarInput(val searchBarInput: String): LandingEvent
    object StartPlaying: LandingEvent
    object StopPlaying: LandingEvent
}