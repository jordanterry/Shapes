package uk.co.jordanterry.shapes

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

class ShapeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    private var shape: Shape? = null
    private var transition: Transition? = null
    private var transitionTime: Long = 0


    private val transitions = mapOf(
        Shape.Square to listOf(
            Transition(Shape.Hexagon, R.drawable.avd_square_to_hexagon),
            Transition(Shape.Triangle, R.drawable.avd_square_to_triangle),
            Transition(Shape.Rhombus, R.drawable.avd_square_to_rhombus)
        ),
        Shape.Triangle to listOf(
            Transition(Shape.Square, R.drawable.avd_triangle_to_square),
            Transition(Shape.Hexagon, R.drawable.avd_triangle_to_hexagon),
            Transition(Shape.Rhombus, R.drawable.avd_triangle_to_rhombus)
        ),
        Shape.Hexagon to listOf(
            Transition(Shape.Square, R.drawable.avd_hexagon_to_square),
            Transition(Shape.Triangle, R.drawable.avd_hexagon_to_triangle),
            Transition(Shape.Rhombus, R.drawable.avd_hexagon_to_rhombus)
        ),
        Shape.Rhombus to listOf(
            Transition(Shape.Square, R.drawable.avd_rhombus_to_square),
            Transition(Shape.Triangle, R.drawable.avd_rhombus_to_triangle),
            Transition(Shape.Hexagon, R.drawable.avd_rhombus_to_hexagon)
        )

    )

    init {
        val shape = Shape.values().random()
        transition = transitions.getValue(shape).random()
        setTransitionTime()
        setImageDrawable(getDrawableFromTransition(transition!!))
//        shapeSquare.setOnClickListener {
//            val adrawable = shapeSquare.drawable as AnimatedVectorDrawable
//            if (!adrawable.isRunning) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    adrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
//                        override fun onAnimationEnd(drawable: Drawable?) {
//                            setNextTransition(transition!!, adrawable)
//                        }
//                    })
//                    adrawable.start()
//                }
//            }
//        }
    }

    fun updateIfRequired() {
        val animatedDrawable = drawable as AnimatedVectorDrawable
        if (System.currentTimeMillis() > transitionTime && !animatedDrawable.isRunning) {
            setNextTransition(transition!!, animatedDrawable)
        }
    }

    private fun setTransitionTime() {
        transitionTime = System.currentTimeMillis() + (800..3500).random()
    }

    private fun setNextTransition(oldTransition: Transition, drawable: AnimatedVectorDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.reset()
        }
        // Start with the end point of the last transition
        val nextTransition = transitions[oldTransition.to]!!.random()
        transition = nextTransition
        val adrawable = drawable as AnimatedVectorDrawable
        if (!adrawable.isRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                adrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        setImageDrawable(getDrawableFromTransition(nextTransition))
                        setTransitionTime()
                    }
                })
                adrawable.start()
            }
        }
    }


    private fun getDrawableFromTransition(transition: Transition): AnimatedVectorDrawable {
        return ContextCompat.getDrawable(context, transition.drawable) as AnimatedVectorDrawable
    }

    enum class Shape {
        Square,
        Triangle,
        Hexagon,
        Rhombus
    }

    data class Transition(
        val to: Shape,
        @DrawableRes val drawable: Int
    )

}