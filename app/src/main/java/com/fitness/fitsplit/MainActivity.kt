package com.fitness.fitsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fitness.fitsplit.ui.MainApp
import com.fitness.fitsplit.ui.theme.FitSplitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
//        installSplashScreen()
        installSplashScreen().setKeepOnScreenCondition { false }
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitSplitTheme {
                MainApp()
            }
        }
    }
}
