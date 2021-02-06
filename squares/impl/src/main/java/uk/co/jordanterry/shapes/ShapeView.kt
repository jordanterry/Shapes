package uk.co.jordanterry.game

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class ShapeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var currentShape: Shape? = null

    private val transitions = mapOf(
        Shape.Square to listOf(
            Transition(
                Shape.Hexagon,
                R.drawable.avd_square_to_hexagon
            ),
            Transition(
                Shape.Triangle,
                R.drawable.avd_square_to_triangle
            ),
            Transition(
                Shape.Rhombus,
                R.drawable.avd_square_to_rhombus
            ),
            Transition(
                Shape.Dash,
                R.drawable.avd_square_to_dash
            )
        ),
        Shape.Triangle to listOf(
            Transition(
                Shape.Square,
                R.drawable.avd_triangle_to_square
            ),
            Transition(
                Shape.Hexagon,
                R.drawable.avd_triangle_to_hexagon
            ),
            Transition(
                Shape.Rhombus,
                R.drawable.avd_triangle_to_rhombus
            ),
            Transition(
                Shape.Dash,
                R.drawable.avd_triangle_to_dash
            )
        ),
        Shape.Hexagon to listOf(
            Transition(
                Shape.Square,
                R.drawable.avd_hexagon_to_square
            ),
            Transition(
                Shape.Triangle,
                R.drawable.avd_hexagon_to_triangle
            ),
            Transition(
                Shape.Rhombus,
                R.drawable.avd_hexagon_to_rhombus
            ),
            Transition(
                Shape.Dash,
                R.drawable.avd_hexagon_to_dash
            )
        ),
        Shape.Rhombus to listOf(
            Transition(
                Shape.Square,
                R.drawable.avd_rhombus_to_square
            ),
            Transition(
                Shape.Triangle,
                R.drawable.avd_rhombus_to_triangle
            ),
            Transition(
                Shape.Hexagon,
                R.drawable.avd_rhombus_to_hexagon
            ),
            Transition(
                Shape.Dash,
                R.drawable.avd_rhombus_to_dash
            )
        ),
        Shape.Dash to listOf(
            Transition(
                Shape.Square,
                R.drawable.avd_dash_to_square
            ),
            Transition(
                Shape.Triangle,
                R.drawable.avd_dash_to_triangle
            ),
            Transition(
                Shape.Hexagon,
                R.drawable.avd_dash_to_hexagon
            ),
            Transition(
                Shape.Rhombus,
                R.drawable.avd_dash_to_rhombus
            )
        )
    )

    private val ivShape: ImageView

    init {
        View.inflate(context, R.layout.view_shape, this)
        ivShape = findViewById(R.id.ivShape)
    }

    fun updateShape(shape: Shape) {
        if (currentShape == null) {
            currentShape = Shape.Dash
            ivShape.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.shape_dash
                )
            )
        } else if (shape != currentShape && !isTransitioning()) {
            val newTransition = transitions[currentShape!!]!!.first { it.to == shape }
            setNextTransition(newTransition, shape)
        }
    }

    private fun isTransitioning(): Boolean {
        val currentDrawable = ivShape.drawable
        return if (currentDrawable is AnimatedVectorDrawable) {
            currentDrawable.isRunning
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setNextTransition(transition: Transition, nextShape: Shape) {
        val currentDrawable = ivShape.drawable
        if (currentDrawable is AnimatedVectorDrawable) {
            currentDrawable.reset()
        }
        val newDrawable = getDrawableFromTransition(transition)
        ivShape.setImageDrawable(newDrawable)
        newDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                currentShape = nextShape
            }
        })
        newDrawable.start()
    }

    private fun getDrawableFromTransition(transition: Transition): AnimatedVectorDrawable {
        return ContextCompat.getDrawable(context, transition.drawable) as AnimatedVectorDrawable
    }
}