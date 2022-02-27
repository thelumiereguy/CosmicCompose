package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.length
import kotlin.math.abs

/**
 * Composable to draw light source - the sun, in the context.
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun LightSourceComposable(modifier: Modifier) {

    BoxWithConstraints(modifier = modifier) {

        val halfHeight = constraints.maxHeight / 2
        val halfWidth = constraints.maxWidth / 2

        val radius = minOf(halfWidth, halfHeight) / 5f

        val pointsList = remember {
            (-halfWidth..halfWidth step pixelsDivisions).flatMap { x ->
                (-halfHeight..halfHeight step pixelsDivisions).map { y ->
                    Float2(x.toFloat(), y.toFloat())
                }
            }
        }

        Canvas(modifier = modifier) {
            translate(
                halfWidth.toFloat(),
                halfHeight.toFloat(),
            ) {

                pointsList.filter {
                    length(it) < radius
                }.forEach { coord ->

                    // gradient according to length
                    val lightness = abs(1 - length(
                        coord
                    ) / radius)

                    drawCircle(
                        Color.hsl(
                            40f,
                            0.5f,
                            lightness
                        ),
                        radius = 10f,
                        Offset(coord.x, coord.y)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LightComposablePreview() {
    LightSourceComposable(modifier = Modifier.fillMaxSize())
}