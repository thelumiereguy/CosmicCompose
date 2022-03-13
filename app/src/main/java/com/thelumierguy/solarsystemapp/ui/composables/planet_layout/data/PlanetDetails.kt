package com.thelumierguy.solarsystemapp.ui.composables.planet_layout.data

import androidx.compose.runtime.Immutable

@Immutable
data class PlanetDetails(
    val hue: Float,
    val saturation: Float,
    val radius: Float,
    val revolutionDurationDays: Int,
)
