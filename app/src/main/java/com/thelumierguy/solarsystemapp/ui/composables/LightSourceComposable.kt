package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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

        val halfHeight = constraints.maxHeight / 2f
        val halfWidth = constraints.maxWidth / 2f

        val radius = minOf(halfWidth, halfHeight) / 6

        Canvas(modifier = modifier) {

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        Color.hsl(
                            35f,
                            1f,
                            0.9f
                        ),
                        Color.hsl(
                            35f,
                            0.9f,
                            0.8f,
                            0.9f
                        ),
                        Color.hsl(
                            35f,
                            0.4f,
                            0.5f,
                            0.99f
                        ),
                        Color.hsl(
                            40f,
                            1f,
                            0.4f,
                            0.5f
                        ),
                        Color.Transparent,
                    ),
                    radius = radius,
                ),
                radius = radius,
                Offset(halfWidth, halfHeight)
            )
        }
    }
}

@Preview
@Composable
fun LightComposablePreview() {
    LightSourceComposable(modifier = Modifier.fillMaxSize())
}