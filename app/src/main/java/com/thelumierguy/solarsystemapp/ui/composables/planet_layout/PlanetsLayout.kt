package com.thelumierguy.solarsystemapp.ui.composables.planet_layout

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.thelumierguy.solarsystemapp.ui.composables.PlanetComposable
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.PI
import dev.romainguy.kotlin.math.TWO_PI
import dev.romainguy.kotlin.math.radians
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PlanetsLayout() {
    val infiniteTransition = rememberInfiniteTransition()

    val planetRotationAnimations = planetDetailsList.mapIndexed { index, planetDetails ->
        val startOffset = radians(0f * index)
        infiniteTransition.animateFloat(
            initialValue = startOffset,
            targetValue = TWO_PI + startOffset,
            animationSpec = infiniteRepeatable(
                tween(
                    planetDetails.revolutionDurationDays * 50,
                    easing = LinearEasing
                ),
                RepeatMode.Restart
            ),
        )
    }

    var lightSourceLocation = remember {
        Float2(0f, 0f)
    }

    Layout(
        content = {
            planetDetailsList.forEachIndexed { index, planetDetails ->
                PlanetComposable(
                    planetDetails = planetDetails,
                    modifier = Modifier,
                    lightSourceLocationProvider = { lightSourceLocation },
                    index = index
                )
            }
        },
        measurePolicy = { measurables, constraints ->

            val screenCenter = Float2(
                constraints.maxWidth / 2f,
                constraints.maxHeight / 2f,
            )

            lightSourceLocation = screenCenter

            val placeables = measurables.mapIndexed { index, measurable ->

                val radius = planetDetailsList[index].radius.roundToInt()

                measurable.measure(
                    constraints.copy(
                        maxWidth = radius,
                        maxHeight = radius
                    )
                )
            }

            layout(
                constraints.maxWidth,
                constraints.maxHeight
            ) {


                placeables.forEachIndexed { index, placeable ->

                    val angle = planetRotationAnimations[index].value

                    val indexF = index.toFloat()

                    // Placed along an elliptical orbit
                    val coordinate = Float2(
                        cos(angle) * (indexF + 1) * 250,
                        sin(angle) * (indexF + 1) * 10
                    ) + screenCenter

                    val radius = planetDetailsList[index].radius

                    val isBehindLightSource = angle > PI

                    // Don't place if out of screen bounds
                    if (
                        coordinate.x in (-radius..constraints.maxWidth.toFloat() + radius) &&
                        coordinate.y in (-radius..constraints.maxHeight.toFloat() + radius)
                    )
                        placeable.placeRelative(
                            coordinate.x.roundToInt(),
                            coordinate.y.roundToInt(),
                            zIndex = if (isBehindLightSource) {
                                -indexF
                            } else indexF // invert zIndex if behind the lightSource
                        )
                }
            }
        }
    )
}

val planetDetailsList = listOf(
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