package com.thelumierguy.cosmic_compose.ui.composables

import android.view.MotionEvent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
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

        val lightSourceRadius = remember {
            minOf(
                constraints.maxWidth,
                constraints.maxHeight
            ) / 10f
        }


        var touchLocation by remember {
            mutableStateOf(screenCenter)
        }

        //Coordinates of light source
        val lightSourceLocation = animateValueAsState(
            targetValue = touchLocation,
            typeConverter = TwoWayConverter(
                convertToVector = { AnimationVector2D(it.x, it.y) },
                convertFromVector = { Float2(it.v1, it.v2) }
            ),
            animationSpec = tween(
                1000,
            )
        )



        StarsComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            maxOf(constraints.maxWidth, constraints.maxHeight)
        )

        LightSourceComposable(
            Modifier
                .fillMaxSize()
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            touchLocation = touchLocation.copy(
                                event.x,
                                event.y
                            )
                        }
                        MotionEvent.ACTION_MOVE -> {
                            touchLocation = touchLocation.copy(
                                event.x,
                                event.y
                            )
                        }
                    }
                    true
                },
            location = lightSourceLocation.value,
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
                lightSourceLocationProvider = { lightSourceLocation.value }
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
