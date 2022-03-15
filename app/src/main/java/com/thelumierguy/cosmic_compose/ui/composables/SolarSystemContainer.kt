package com.thelumierguy.cosmic_compose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import com.thelumierguy.cosmic_compose.ui.composables.light_source.LightSourceComposable
import com.thelumierguy.cosmic_compose.ui.composables.planet.PlanetComposable
import com.thelumierguy.cosmic_compose.ui.composables.planet_layout.PlanetsLayout
import com.thelumierguy.cosmic_compose.ui.composables.planet_layout.data.planetDetailsList
import com.thelumierguy.cosmic_compose.ui.composables.stars.StarsComposable
import dev.romainguy.kotlin.math.Float2

// Container Composable hosting multiple layers
@OptIn(ExperimentalGraphicsApi::class)
@ExperimentalComposeUiApi
@Composable
fun SolarSystemContainer(modifier: Modifier) {

    BoxWithConstraints(modifier = modifier.background(Color.Black)) {

        val screenCenter = remember(
            key1 = constraints.maxWidth,
            key2 = constraints.maxHeight
        ) {
            Float2(
                constraints.maxWidth / 2f,
                constraints.maxHeight / 2f
            )
        }

        //Coordinates of light source
        val lightSourceLocation by remember {
            mutableStateOf(screenCenter)
        }

        val lightSourceRadius = remember {
            minOf(
                constraints.maxWidth,
                constraints.maxHeight
            ) / 10f
        }

        StarsComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            maxOf(constraints.maxWidth, constraints.maxHeight)
        )

        LightSourceComposable(
            Modifier
                .fillMaxSize(),
            location = lightSourceLocation,
            radius = lightSourceRadius
        )

        PlanetsLayout(
            planetDetailsList = planetDetailsList,
            lightSourceRadius = lightSourceRadius,
            screenCenter = screenCenter,
        ) {
            PlanetComposable(
                modifier = Modifier,
                lightSourceRadius = lightSourceRadius,
                lightSourceLocation = lightSourceLocation
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
