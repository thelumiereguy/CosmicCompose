package com.thelumierguy.solarsystemapp.ui.composables.planet_layout

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.data.PlanetDetails
import com.thelumierguy.solarsystemapp.ui.modifier.recomposeHighlighter
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.PI
import dev.romainguy.kotlin.math.TWO_PI
import dev.romainguy.kotlin.math.radians
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PlanetsLayout(
    planetDetailsList: List<PlanetDetails>,
    lightSourceLocation: Float2,
    content: @Composable (PlanetsLayoutScope.() -> Unit)
) {

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


    Layout(
        content = {
            planetDetailsList.forEachIndexed { index, planetDetails ->
                PlanetsLayoutScopeImpl(index, planetDetails).content()
            }
        },
        measurePolicy = { measurables, constraints ->

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
                        sin(angle) * (indexF + 1) * 30
                    ) + lightSourceLocation

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
                            zIndex = if (isBehindLightSource) { // invert zIndex if behind the lightSource
                                -indexF
                            } else indexF
                        )
                }
            }
        }
    )
}