package com.example.digital_diary.presentation.landing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LandingViewModel: ViewModel() {
    private val _state = MutableStateFlow(LandingState())
    val state = _state.asStateFlow()

    fun onSearchBarInputChange(input: String){
        _state.update { it.copy(searchBarInput = input)}
    }
}