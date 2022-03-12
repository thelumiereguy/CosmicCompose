package com.thelumierguy.solarsystemapp.ui.composables.planet_layout

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.thelumierguy.solarsystemapp.ui.composables.PlanetComposable
import com.thelumierguy.solarsystemapp.ui.composables.PlanetDetails
import dev.romainguy.kotlin.math.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PlanetsLayout(
    planets: List<PlanetDetails>
) {

    val infiniteTransition = rememberInfiniteTransition()

    val planetRotationAnimations = planets.mapIndexed { index, planetDetails ->
        val startOffset = PI / 4 + radians(137.5f * index)

        infiniteTransition.animateFloat(
            initialValue = startOffset,
            targetValue = TWO_PI + startOffset,
            animationSpec = infiniteRepeatable(
                tween(
                    planetDetails.revolutionDurationDays * 100,
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
            planets.forEach { planetDetails ->
                PlanetComposable(
                    planetDetails = planetDetails,
                    modifier = Modifier,
                    lightSourceLocationProvider = { lightSourceLocation },
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

                val radius = planets[index].radius.roundToInt()

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

                    // Placed along an elliptical orbit
                    val coordinate = Float2(
                        cos(angle) * (index + 1) * 250,
                        sin(angle) * (index + 1) * 130
                    ) + screenCenter

                    val radius = planets[index].radius

                    // Don't place if out of screen bounds
                    if (
                        coordinate.x in (-radius..constraints.maxWidth.toFloat() + radius) &&
                        coordinate.y in (-radius..constraints.maxHeight.toFloat() + radius)
                    )
                        placeable.placeRelative(
                            coordinate.x.roundToInt(),
                            coordinate.y.roundToInt(),
                        )
                }
            }
        }
    )
}
