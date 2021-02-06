package uk.co.jordanterry.shapes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_game.*
import uk.co.jordanterry.menu.R
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val shapes: List<ShapeView> by lazy {
        gShapes.referencedIds.map { findViewById<ShapeView>(it) }
    }

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
    }

    private val shapeClickListener = View.OnClickListener { view ->
        val position = view.tag
        if (position is Int) {
            viewModel.shapeSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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