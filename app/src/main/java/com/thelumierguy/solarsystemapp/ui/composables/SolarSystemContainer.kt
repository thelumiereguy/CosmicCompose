package com.thelumierguy.solarsystemapp.ui.composables

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import com.thelumierguy.solarsystemapp.ui.composables.light_source.LightSourceComposable
import com.thelumierguy.solarsystemapp.ui.composables.planet.PlanetComposable
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.PlanetsLayout
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.data.planetDetailsList
import com.thelumierguy.solarsystemapp.ui.composables.stars.StarsComposable
import dev.romainguy.kotlin.math.Float2

// Container Composable hosting multiple layers
@OptIn(ExperimentalGraphicsApi::class)
@ExperimentalComposeUiApi
@Composable
fun SolarSystemContainer(modifier: Modifier) {

    BoxWithConstraints(modifier = modifier.background(Color.Black)) {

        val screenMid = remember(
            key1 = constraints.maxWidth,
            key2 = constraints.maxHeight
        ) {
            Float2(
                constraints.maxWidth / 2f,
                constraints.maxHeight / 2f
            )
        }

        //Coordinates of light source with list saver
        var lightSourceLocation by remember {
            mutableStateOf(screenMid)
        }

        val lightSourceRadius = remember {
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
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            lightSourceLocation = lightSourceLocation.copy(
                                event.x,
                                event.y
                            )
                        }
                        MotionEvent.ACTION_MOVE -> {
                            lightSourceLocation = lightSourceLocation.copy(
                                event.x,
                                event.y
                            )
                        }
                    }
                    true
                },
            location = lightSourceLocation,
            radius = lightSourceRadius
        )

        PlanetsLayout(
            planetDetailsList = planetDetailsList,
            lightSourceRadius = lightSourceRadius,
            modifier = Modifier.graphicsLayer {
                translationX = screenMid.x
                translationY = screenMid.y
            },
        ) {
            PlanetComposable(
                modifier = Modifier,
                lightSourceRadius = lightSourceRadius,
                lightSourceLocation = lightSourceLocation - screenMid
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
