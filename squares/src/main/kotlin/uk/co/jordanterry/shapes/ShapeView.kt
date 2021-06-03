package uk.co.jordanterry.shapes

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import uk.co.jordanterry.shapes.squares.impl.R

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

    private val ivShape: ImageView by lazy {
        findViewById<ImageView>(R.id.ivShape)
    }

    init {
        View.inflate(context, R.layout.view_shape, this)
    }

    fun setShape(shape: Shape) {
        if (currentShape == null) {
            currentShape = Shape.Dash
            val dashDrawable = ContextCompat.getDrawable(
                context,
                R.drawable.shape_dash
            )
            ivShape.setImageDrawable(dashDrawable)
        } else if (shape != currentShape) {
            val newTransition = transitions[currentShape!!]!!.first { it.to == shape }
            setTransitionToNextShape(newTransition, shape)
        }
    }

    /**
     * Decide how the [ShapeView] should transition to the next shape.
     *
     * If the current Drawable is an [AnimatedVectorDrawable] and it is currently running we will wait
     * until the end of the current transition and then update the drawble.
     *
     * Any other scenario will immediately update the drawable.
     */
    private fun setTransitionToNextShape(
        transition: Transition,
        nextShape: Shape
    ) {
        val currentDrawable = ivShape.drawable
        if (currentDrawable is AnimatedVectorDrawable) {
            currentDrawable.reset()
            if (currentDrawable.isRunning) {
                currentDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        startTransitionToNextShape(transition, nextShape)
                    }
                })
            } else {
                startTransitionToNextShape(transition, nextShape)
            }
        } else {
            startTransitionToNextShape(transition, nextShape)
        }
    }

    /**
     * Start the [transition] to the [nextShape].
     */
    private fun startTransitionToNextShape(transition: Transition, nextShape: Shape) {
        val newDrawable = getAnimatedVectorDrawableFromTransition(transition)
        ivShape.setImageDrawable(newDrawable)
        newDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                currentShape = nextShape
            }
        })
        newDrawable.start()
    }

    /**
     * Convert the drawable defined withing [Transition] to an [AnimatedVectorDrawable].
     */
    private fun getAnimatedVectorDrawableFromTransition(transition: Transition): AnimatedVectorDrawable {
        return ContextCompat.getDrawable(context, transition.drawable) as AnimatedVectorDrawable
    }
}