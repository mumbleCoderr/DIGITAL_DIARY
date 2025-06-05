package com.example.digital_diary.presentation.landing

data class LandingState(
    val searchBarInput: String = "",
    val currentAudioPath: String? = null,
    val isPlaying: Boolean = false,
)
