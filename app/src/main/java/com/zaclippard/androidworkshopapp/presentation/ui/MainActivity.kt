package com.zaclippard.androidworkshopapp.presentation.ui

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.zaclippard.androidworkshopapp.AndroidWorkshopApp
import com.zaclippard.androidworkshopapp.presentation.ui.nav.AppNavHost
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        lifecycleScope.launch {
            (application as AndroidWorkshopApp).prefs.rotationEnabledStream
                .collect { rotationEnabled ->
                    requestedOrientation = if (rotationEnabled) {
                        SCREEN_ORIENTATION_SENSOR
                    } else {
                        SCREEN_ORIENTATION_PORTRAIT
                    }
                }
        }

        enableEdgeToEdge()
        setContent {
            AndroidWorkshopAppTheme {
                AppNavHost()
            }
        }
    }
}
