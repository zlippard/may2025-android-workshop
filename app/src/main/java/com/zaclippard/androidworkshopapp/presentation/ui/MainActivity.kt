package com.zaclippard.androidworkshopapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zaclippard.androidworkshopapp.presentation.ui.nav.AppNavHost
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        enableEdgeToEdge()
        setContent {
            AndroidWorkshopAppTheme {
                AppNavHost()
            }
        }
    }
}
