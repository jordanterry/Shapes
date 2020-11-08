package uk.co.jordanterry.shapes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class GameActivity : AppCompatActivity() {

    private val shapeIds: List<Int> = listOf(
        R.id.one,
        R.id.two,
        R.id.three,
        R.id.four,
        R.id.five,
        R.id.six,
        R.id.seven,
        R.id.eight,
        R.id.nine,
        R.id.ten,
        R.id.eleven,
        R.id.twelve,
        R.id.thirteen,
        R.id.fourteen,
        R.id.fifteen,
        R.id.sixteen
    )

    private val shapes: List<ShapeView> by lazy {
        shapeIds.map { findViewById<ShapeView>(it) }
    }

    private val shapeToSelect: ShapeView by lazy { findViewById<ShapeView>(R.id.svSelect) }
    private val score: TextView by lazy { findViewById<TextView>(R.id.tvScore) }

    private val viewModel: GameViewModel by lazy {
        GameViewModel(GetCurrentTimeImpl(), Handler(Looper.getMainLooper()))
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
                    score.text = uiModel.score.toString()
                    shapeToSelect.updateShape(uiModel.shapeToSelect.shape)
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