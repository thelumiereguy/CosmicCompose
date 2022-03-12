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

    val planetDetails = remember {
        listOf(
            PlanetDetails(
                hue = 200f,
                saturation = 0.2f,
                radius = 30f,
                revolutionDurationDays = 88
            ),
            PlanetDetails(
                hue = 30f,
                saturation = 0.3f,
                radius = 35f,
                revolutionDurationDays = 224
            ),
            PlanetDetails(
                hue = 220f,
                saturation = 0.4f,
                radius = 40f,
                revolutionDurationDays = 365
            ),
            PlanetDetails(
                hue = 10f,
                saturation = 0.6f,
                radius = 35f,
                revolutionDurationDays = 687
            ),
            PlanetDetails(
                hue = 30f,
                saturation = 0.4f,
                radius = 60f,
                revolutionDurationDays = 4346
            )
        )
    }

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

        PlanetsLayout(
            planetDetails
        )
    }
}

data class PlanetDetails(
    val hue: Float,
    val saturation: Float,
    val radius: Float,
    val revolutionDurationDays: Int,
)


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SolarSystemComposablePreview() {
    SolarSystemComposable(
        modifier = Modifier.fillMaxSize()
    )
}
