package com.thelumierguy.cosmic_compose.ui.composables.planet

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import com.thelumierguy.cosmic_compose.ui.composables.planet_layout.PlanetsLayoutScope
import dev.romainguy.kotlin.math.*
import kotlin.math.roundToInt

/**
 * Number of pixel subdivisions.
 * Less would mean better quality but poor performance
 **/
private const val pixelsDivisions = 1

/**
 *Composable to draw single planet
 *
 * The shading is hacked here by keeping the planet at origin,
 * and the sun rotating around it
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun PlanetsLayoutScope.PlanetComposable(
    modifier: Modifier,
    lightSourceRadius: Float,
    lightSourceLocationProvider: () -> Float2
) {

    var worldPlanetPosition by remember {
        mutableStateOf(
            Float2(0f, 0f)
        )
    }

    val lightSource3d = remember(
        key1 = worldPlanetPosition,
    ) {
        val downVectorNormalized = Float2(
            0f,
            -1f
        )

        // Translate light source, and rotate around the origin
        val lightSourceTranslated = lightSourceLocationProvider() - worldPlanetPosition

        Float3(
            lightSourceTranslated,
            dot( // Just some extra FX to change how light effects the planet when behind/front of light source
                downVectorNormalized,
                normalize(lightSourceTranslated)
            ) * 250f
        )
    }


    /**
     * (radius * radius) points which are then rendered with each point
     * having distinct lightness, alpha, etc
     */
    val pointsList = remember {

        val bounds = planetDetails.radius.roundToInt()

        (-bounds..bounds step pixelsDivisions).flatMap { x ->
            (-bounds..bounds step pixelsDivisions).mapNotNull { y ->
                val pos = Float2(
                    x.toFloat(),
                    y.toFloat()
                )
                val length = length(pos)

                // Optimization - Filter out items out of circle bounds
                if (length <= bounds) {
                    val z = (length - planetDetails.radius)
                    Float3(pos, z)
                } else null
            }
        }
    }

    Canvas(modifier = modifier.onGloballyPositioned { layoutCoordinates ->
        val position = layoutCoordinates.positionInParent()
        worldPlanetPosition = Float2(position.x, position.y)
    }) {

        pointsList.forEach { planetPointCoord ->

            val normalizedCoord = normalize(
                planetPointCoord
            )

            val normalizedLightSource3D = normalize(
                lightSource3d
            )

            // Farther planets receive less light
            // Should ideally be done using distance, but oh well
            val lightFalloffFactor = 0.8f / (index + 1)

            val pointLightness = getDotProduct(
                normalizedCoord,
                normalizedLightSource3D
            ) * lightFalloffFactor


            // Don't render points if directly behind the light source
            val pointAlpha = if (
                isPlanetFartherFromLightSource(
                    planetPointCoord,
                    lightSource3d
                ) && isPlanetExactlyBehindLightSource(
                    planetPointCoord,
                    lightSource3d,
                    lightSourceRadius
                )
            ) {
                0f
            } else {
                1f
            }

            drawCircle(
                color = Color.hsl(
                    hue = planetDetails.hue,
                    saturation = planetDetails.saturation,
                    lightness = pointLightness.coerceIn(0f..1f),
                    alpha = pointAlpha
                ),
                radius = 2f,
                center = Offset(planetPointCoord.x, planetPointCoord.y)
            )
        }
    }
}


private fun getDotProduct(
    normalizedPointCoord: Float3,
    normalizedLightSourceCoord: Float3
): Float {

    var dotProduct = dot(
        normalizedPointCoord,
        normalizedLightSourceCoord
    )


    if (dotProduct.isNaN()) {
        dotProduct = 0f
    }

    return dotProduct.coerceIn(0f..1f)
}

private fun isPlanetFartherFromLightSource(
    planetLocation: Float3,
    lightSourceLocation: Float3
): Boolean {
    return planetLocation.z >= lightSourceLocation.z
}

private fun isPlanetExactlyBehindLightSource(
    planetLocation: Float3,
    lightSourceLocation: Float3,
    lightSourceRadius: Float
): Boolean {
    return distance(planetLocation.xy, lightSourceLocation.xy) <= lightSourceRadius
}