package com.example.digital_diary.presentation.sign_in

data class SignInState(
    val isSignInIsSuccessful: Boolean = false,
    val signInError: String? = null,
)