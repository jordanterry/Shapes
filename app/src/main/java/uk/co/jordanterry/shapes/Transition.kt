package uk.co.jordanterry.shapes

import androidx.annotation.DrawableRes

data class Transition(
    val to: Shape,
    @DrawableRes val drawable: Int
)
