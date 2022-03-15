package com.thelumierguy.cosmic_compose.ui.composables.stars

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
fun StarsComposable(
    modifier: Modifier,
    maxConstraints: Int
) {
    val stars = remember {
        List(800) {
            StarDetails(
                Offset(
                    x = Random.nextInt(0, maxConstraints).toFloat(),
                    y = Random.nextInt(0, maxConstraints).toFloat(),
                ),
                StarDistance.values().random()
            )

        }
    }

    val infiniteTransition = rememberInfiniteTransition()

    val starBlinkingAnimationList = stars.mapIndexed { index, _ ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(
                    (index + 1) * 25,
                    easing = LinearEasing
                ),
                RepeatMode.Reverse
            ),
        )
    }

    Canvas(modifier = modifier) {
        stars.forEachIndexed { index, star ->
            val radius = when (star.distance) {
                StarDistance.Far -> 3f
                StarDistance.Farther -> 2f
                StarDistance.Farthest -> 1f
            }

            drawCircle(
                Color.DarkGray.copy(
                    alpha = starBlinkingAnimationList[index].value
                ),
                radius,
                star.coordinate,
            )
        }
    }
}

@Preview
@Composable
fun StarsComposablePreview() {
    StarsComposable(modifier = Modifier.fillMaxSize(), 600)
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