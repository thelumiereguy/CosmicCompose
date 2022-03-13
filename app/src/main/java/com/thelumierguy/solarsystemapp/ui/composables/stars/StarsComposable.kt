package com.thelumierguy.solarsystemapp.ui.composables.stars

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random


/**
 * Composable to draw stars at random positions on the screens
 */
@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun StarsComposable(modifier: Modifier) {

    BoxWithConstraints(modifier = modifier) {

        val stars = remember {
            List(800) {
                StarDetails(
                    Offset(
                        x = Random.nextInt(0, constraints.maxWidth).toFloat(),
                        y = Random.nextInt(0, constraints.maxHeight).toFloat(),
                    ),
                    StarDistance.values().random()
                )

            }
        }

        Canvas(modifier = modifier) {
            stars.forEach { star ->
                val radius = when (star.distance) {
                    StarDistance.Far -> 3f
                    StarDistance.Farther -> 2f
                    StarDistance.Farthest -> 1f
                }

                drawCircle(
                    Color.DarkGray,
                    radius,
                    star.coordinate,
                )
            }
        }
    }
}

@Preview
@Composable
fun StarsComposablePreview() {
    StarsComposable(modifier = Modifier.fillMaxSize())
}

private data class StarDetails(
    val coordinate: Offset,
    val distance: StarDistance,
)

private enum class StarDistance {
    Far,
    Farther,
    Farthest
}