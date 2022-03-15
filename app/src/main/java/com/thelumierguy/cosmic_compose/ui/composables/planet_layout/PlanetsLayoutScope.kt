package com.thelumierguy.cosmic_compose.ui.composables.planet_layout

import androidx.compose.runtime.Stable
import com.thelumierguy.cosmic_compose.ui.composables.planet_layout.data.PlanetDetails

@Stable
interface PlanetsLayoutScope {

    val index: Int

    val planetDetails: PlanetDetails
}

data class PlanetsLayoutScopeImpl(
    override val index: Int,
    override val planetDetails: PlanetDetails
) : PlanetsLayoutScope