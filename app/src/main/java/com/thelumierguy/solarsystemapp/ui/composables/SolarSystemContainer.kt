package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import com.thelumierguy.solarsystemapp.ui.composables.light_source.LightSourceComposable
import com.thelumierguy.solarsystemapp.ui.composables.planet.PlanetComposable
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.PlanetsLayout
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.data.planetDetailsList
import com.thelumierguy.solarsystemapp.ui.composables.stars.StarsComposable
import com.thelumierguy.solarsystemapp.ui.modifier.recomposeHighlighter
import dev.romainguy.kotlin.math.Float2

// Container Composable hosting multiple layers
@OptIn(ExperimentalGraphicsApi::class)
@ExperimentalComposeUiApi
@Composable
fun SolarSystemContainer(modifier: Modifier) {

    BoxWithConstraints(modifier = modifier.background(Color.Black)) {

        val lightSourceLocation by remember(
            key1 = constraints.maxWidth,
            key2 = constraints.maxHeight
        ) {
            mutableStateOf(
                Float2(
                    constraints.maxWidth / 2f,
                    constraints.maxHeight / 2f
                )
            )
        }

        val lightSourceRadius = derivedStateOf {
            minOf(
                constraints.maxWidth,
                constraints.maxHeight
            ) / 12f
        }

        StarsComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        LightSourceComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            location = lightSourceLocation,
            radius = lightSourceRadius.value
        )

        PlanetsLayout(
            planetDetailsList = planetDetailsList,
            lightSourceLocation = lightSourceLocation
        ) {
            PlanetComposable(
                modifier = Modifier.recomposeHighlighter(),
                lightSourceRadius = lightSourceRadius.value,
                lightSourceLocationProvider = { lightSourceLocation }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SolarSystemComposablePreview() {
    SolarSystemContainer(
        modifier = Modifier.fillMaxSize()
    )
}
