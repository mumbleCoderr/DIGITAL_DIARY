package com.example.digital_diary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.digital_diary.data.MemoryDatabase
import com.example.digital_diary.data.MemoryViewModel
import com.example.digital_diary.presentation.landing.LandingScreen
import com.example.digital_diary.presentation.profile.ProfileScreen
import com.example.digital_diary.presentation.sign_in.GoogleAuthUiClient
import com.example.digital_diary.presentation.sign_in.SignInScreen
import com.example.digital_diary.presentation.sign_in.SignInViewModel
import com.example.digital_diary.ui.theme.Digital_DiaryTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MemoryDatabase::class.java,
            "memories.db"
        ).build()
    }

    private val memoryViewModel by viewModels<MemoryViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MemoryViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Digital_DiaryTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "sign_in") {
                    composable("sign_in") {
                        val landingViewModel = viewModel<SignInViewModel>()
                        val state by landingViewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1= Unit) {
                            if(googleAuthUiClient.getSignedInUser() != null){
                                navController.navigate("landing")
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        landingViewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )
                        LaunchedEffect(key1 = state.isSignInIsSuccessful) {
                            if (state.isSignInIsSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    context.getString(R.string.sign_in_toast),
                                    Toast.LENGTH_LONG,
                                ).show()
                                navController.navigate("landing")
                                landingViewModel.resetState()
                            }
                        }

                        SignInScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }
                    composable(route = "landing") {
                        LandingScreen(
                            userData = googleAuthUiClient.getSignedInUser(),
                            navController = navController,
                            memoryViewModel = memoryViewModel,
                            onEvent = memoryViewModel::onEvent
                        )
                    }
                    composable(route = "profile") {
                        ProfileScreen(
                            userData = googleAuthUiClient.getSignedInUser(),
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        context.getString(R.string.sign_out_toast),
                                        Toast.LENGTH_LONG,
                                    ).show()
                                    navController.navigate("sign_in"){
                                        popUpTo("sign_in") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

