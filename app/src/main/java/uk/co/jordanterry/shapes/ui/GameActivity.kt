package uk.co.jordanterry.shapes.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_game.*
import uk.co.jordanterry.shapes.R
import uk.co.jordanterry.squares.logic.usecases.GetCurrentTime

class GameActivity : AppCompatActivity() {

    private val shapes: List<ShapeView> by lazy {
        gShapes.referencedIds.map { findViewById<ShapeView>(it) }
    }

    private val viewModel: GameViewModel by lazy {
        GameViewModel(
            GetCurrentTime.create(),
            Handler(Looper.getMainLooper())
        )
    }

    private val shapeClickListener = View.OnClickListener { view ->
        val position = view.tag
        if (position is Int) {
            viewModel.shapeSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        viewModel.uiModel.observe(this, Observer { uiModel ->
            when (uiModel) {
                is GameViewModel.UiModel.Loading -> {
                }
                is GameViewModel.UiModel.Loaded -> {
                    tvScore.text = uiModel.score.toString()
                    svSelect.updateShape(uiModel.shapeToSelect.shape)
                    uiModel.shapes.forEachIndexed { index, uiShape ->
                        shapes[index].updateShape(uiShape.shape)
                    }
                }
            }
        })

        shapes.forEachIndexed { index, shapeView ->
            shapeView.tag = index
            shapeView.setOnClickListener(shapeClickListener)
        }
        viewModel.init()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GameActivity::class.java)
        }
    }
}