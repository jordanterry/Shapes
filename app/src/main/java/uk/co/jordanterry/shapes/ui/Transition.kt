package uk.co.jordanterry.shapes.ui

import androidx.annotation.DrawableRes
import uk.co.jordanterry.shapes.ui.Shape

data class Transition(
    val to: Shape,
    @DrawableRes val drawable: Int
)
