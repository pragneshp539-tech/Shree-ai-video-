package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.MainStudioScreen
import com.example.ui.theme.SreeVideoStudioTheme
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure App Check before any UI, ViewModels, or Firebase calls
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            SreeVideoStudioTheme {
                MainStudioScreen()
            }
        }
    }
}
