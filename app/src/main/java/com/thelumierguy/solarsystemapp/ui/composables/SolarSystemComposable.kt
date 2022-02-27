package com.thelumierguy.solarsystemapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// lower means more quality
internal const val pixelsDivisions = 5

// Container Composable hosting multiple layers
@OptIn(ExperimentalGraphicsApi::class)
@ExperimentalComposeUiApi
@Composable
fun SolarSystemComposable(modifier: Modifier) {

    val planetDetails = remember {
        listOf(
            PlanetDetails(
                200f,
                0.2f,
                30f, //20
                88
            ),
            PlanetDetails(
                30f,
                0.3f,
                35f, //25
                224
            ),
            PlanetDetails(
                220f,
                0.4f,
                40f, //30
                365
            ),
            PlanetDetails(
                10f,
                0.6f,
                35f, //25
                687
            ),
            PlanetDetails(
                30f,
                0.4f,
                60f, //50
                4346
            ),
        )
    }

    Box(modifier = modifier.background(Color.Black)) {

        StarsComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        LightSourceComposable(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        planetDetails.indices.forEach { index ->
            PlanetComposable(
                Modifier
                    .align(Alignment.Center)
                    .height(140.dp * (index + 1))
                    .width(140.dp * (index + 1)),
                index = index + 1,
                planetDetails = planetDetails[index],
            )
        }
    }
}

data class PlanetDetails(
    val hue: Float,
    val saturation: Float,
    val radius: Float,
    val revolutionDurationDays: Int,
)


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SolarSystemComposablePreview() {
    SolarSystemComposable(
        modifier = Modifier.fillMaxSize()
    )
}
