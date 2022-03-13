package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.PlanetsLayout

// Container Composable hosting multiple layers
@OptIn(ExperimentalGraphicsApi::class)
@ExperimentalComposeUiApi
@Composable
fun SolarSystemComposable(modifier: Modifier) {

    Box(modifier = modifier.background(Color.Black)) {

        StarsComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        LightSourceComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        PlanetsLayout()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SolarSystemComposablePreview() {
    SolarSystemComposable(
        modifier = Modifier.fillMaxSize()
    )
}
