package com.thelumierguy.solarsystemapp.ui.composables.planet_layout

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.data.PlanetDetails
import dev.romainguy.kotlin.math.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * A Layout composable that lays out all the planets
 * @param lightSourceRadius is used to determine the orbit sizes
 * @param screenCenter is used to set the origin for orbit
 **/
@Composable
fun PlanetsLayout(
    planetDetailsList: List<PlanetDetails>,
    lightSourceRadius: Float,
    screenCenter: Float2,
    planetComposable: @Composable (PlanetsLayoutScope.() -> Unit),
) {

    val infiniteTransition = rememberInfiniteTransition()

    /**
     * List of animations which determine each planet's turn angle
     */
    val planetRotationAnimations = planetDetailsList.mapIndexed { index, planetDetails ->
        val startOffset = radians(1f * index)
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
                PlanetsLayoutScopeImpl(index, planetDetails).planetComposable()
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
                        cos(angle) * (indexF + 1) * (lightSourceRadius * 2),
                        sin(angle) * (indexF + 1) * 30
                    ) + screenCenter

                    val zIndex = if (angle > PI) { // invert zIndex if behind the lightSource
                        -indexF
                    } else indexF

                    placeable.placeRelative(
                        coordinate.x.roundToInt(),
                        coordinate.y.roundToInt(),
                        zIndex = zIndex
                    )
                }
            }
        }
    )
}