package com.thelumierguy.solarsystemapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.thelumierguy.solarsystemapp.ui.composables.SolarSystemContainer
import com.thelumierguy.solarsystemapp.ui.theme.SolarSystemAppTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolarSystemAppTheme {
                SolarSystemContainer(
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}