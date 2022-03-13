package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import com.thelumierguy.solarsystemapp.ui.composables.planet_layout.PlanetDetails
import dev.romainguy.kotlin.math.*
import kotlin.math.roundToInt

// lower means more quality
private const val pixelsDivisions = 2

/**
 *Composable to draw single planet
 * Currently, the light source is centered in the screen,
 * making the calculations easier
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun PlanetComposable(
    modifier: Modifier,
    planetDetails: PlanetDetails,
    lightSourceLocationProvider: () -> Float2,
    index: Int
) {

    val lightSourceLocation = lightSourceLocationProvider()

    var worldPosition by remember {
        mutableStateOf(Float2(0f, 0f))
    }

    val downVectorNormalized = remember {
        Float2(
            0f,
            -1f
        )
    }


    val lightSource3d = remember(
        key1 = worldPosition
    ) {
        // Translate around origin
        val lightSourceTranslated = lightSourceLocation - worldPosition

        Float3(
            lightSourceTranslated,
            dot(
                downVectorNormalized,
                normalize(lightSourceTranslated)
            ) * 500f
        )
    }


    val pointsList = remember {

        val bounds = planetDetails.radius.roundToInt()
        (-bounds..bounds step pixelsDivisions).flatMap { x ->
            (-bounds..bounds step pixelsDivisions).mapNotNull { y ->
                val pos = Float2(
                    x.toFloat(),
                    y.toFloat()
                )
                val length = length(pos)
                if (length <= bounds) {
                    val z = length - planetDetails.radius
                    Float3(pos, z)
                } else null
            }
        }
    }

    Canvas(modifier = modifier.onGloballyPositioned { layoutCoordinates ->
        val position = layoutCoordinates.positionInParent()
        worldPosition = Float2(position.x, position.y)
    }) {

        if (lightSourceLocation.x != 0f && lightSourceLocation.y != 0f) {
            pointsList.forEach { coord ->

                val normalizedCoord = normalize(
                    coord
                )

                val normalizedLightSource3D = normalize(
                    lightSource3d
                )

                val lightFalloffFactor = 0.5f / (index + 1) // Inverse of index

                var dotProduct = dot(
                    normalizedCoord,
                    normalizedLightSource3D
                ) * lightFalloffFactor


                if (dotProduct.isNaN()) {
                    dotProduct = 0f
                }

                drawCircle(
                    color = Color.hsl(
                        hue = planetDetails.hue,
                        saturation = planetDetails.saturation,
                        lightness = dotProduct.coerceIn(0f..1f),
                        alpha = if (isBehindLightSource(coord, lightSource3d)) {
                            0f
                        } else {
                            1f
                        }
                    ),
                    radius = 2f,
                    center = Offset(coord.x, coord.y)
                )
            }
        }
    }
}

private fun isBehindLightSource(
    planetLocation: Float3,
    lightSourceLocation: Float3
): Boolean {
    return planetLocation.z < lightSourceLocation.z &&
            (planetLocation.x !in ((lightSourceLocation.x - 100)..lightSourceLocation.x + 100) &&
                    planetLocation.y !in ((lightSourceLocation.y - 100)..lightSourceLocation.y + 100))
}