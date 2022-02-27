package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import dev.romainguy.kotlin.math.*
import kotlin.math.cos


/**
 *Composable to draw single planet
 * Currently, the light source is centered in the screen,
 * making the calculation easier
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun PlanetComposable(
    modifier: Modifier,
    index: Int,
    planetDetails: PlanetDetails,
) {

    BoxWithConstraints(modifier = modifier) {

        var lightSource = remember {
            Float3(0f, 0f, -50f)
        }

        val animationFrame by rememberInfiniteTransition().animateFloat(
            initialValue = 1f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                tween(
                    planetDetails.revolutionDurationDays * 100,
                    easing = LinearEasing
                ),
                RepeatMode.Restart
            ),
        )

        val halfHeight = constraints.maxHeight / 2
        val halfWidth = constraints.maxWidth / 2


        val pointsList = remember {
            (-halfWidth..halfWidth step pixelsDivisions).flatMap { x ->
                (-halfHeight..halfHeight step pixelsDivisions).map { y ->
                    val pos = Float2(x.toFloat(), y.toFloat())
                    val z = length(pos) - planetDetails.radius
                    Float3(pos, z)
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {

            val angle = (90f * animationFrame) * index
            val angleRadians = radians(angle)

            // Calculate position of light source
            lightSource = lightSource.copy(
                x = cos(angleRadians) + planetDetails.radius,
                y = halfHeight + planetDetails.radius
            )

            rotate(
                angle,
                pivot = Offset(halfWidth.toFloat(), halfHeight.toFloat())
            ) {

                translate(
                    minOf(halfWidth, halfHeight) - planetDetails.radius,
                    0f,
                ) {


                    pointsList.filter {
                        length(it) < planetDetails.radius
                    }.forEach { coord ->

                        val normalizedCoord = normalize(coord)

                        val normalizedLightSource = normalize(lightSource)

                        // factor to reduce glow as distance grows
                        val lightDistanceFactor = 180 / distance(
                            lightSource,
                            coord,
                        )

                        var dotProduct = maxOf(dot(
                            normalizedCoord,
                            normalizedLightSource,
                        ) * lightDistanceFactor, 0f)

                        if (dotProduct.isNaN()) {
                            dotProduct = 0f
                        }

                        drawCircle(
                            Color.hsl(
                                planetDetails.hue,
                                planetDetails.saturation,
                                dotProduct
                            ),
                            radius = 10f,
                            Offset(coord.x, coord.y)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlanetComposablePreview() {
    PlanetComposable(
        modifier = Modifier.fillMaxSize(),
        1,
        PlanetDetails(
            20f,
            0.5f,
            40f,
            10
        )
    )
}