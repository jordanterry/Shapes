package uk.co.jordanterry.game

import androidx.annotation.DrawableRes

data class Transition(
    val to: Shape,
    @DrawableRes val drawable: Int
)
