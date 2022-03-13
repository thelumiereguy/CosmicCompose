package com.thelumierguy.solarsystemapp.ui.composables.light_source

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import dev.romainguy.kotlin.math.Float2

/**
 * Composable to draw light source - the sun, in the context.
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun LightSourceComposable(
    modifier: Modifier,
    radius: Float,
    location: Float2
) {

    Canvas(modifier = modifier) {

        if (radius <= 0) return@Canvas

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
                radius = radius
            ),
            radius = radius,
            Offset(location.x, location.y)
        )
    }
}

@Preview
@Composable
fun LightComposablePreview() {
    LightSourceComposable(
        modifier = Modifier.fillMaxSize(),
        radius = 40f,
        location = Float2(
            300f,
            300f
        )
    )
}