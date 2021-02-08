package uk.co.jordanterry.shapes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_game.*
import uk.co.jordanterry.shapes.squares.impl.R
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
        initialiseShapes()
        initialiseViewModel()
    }

    private fun initialiseViewModel() {
        viewModel.uiModel.observe(this, Observer { uiModel -> handleNewUiModel(uiModel) })
        viewModel.init()
    }

    private fun initialiseShapes() {
        shapes.forEachIndexed { index, shapeView ->
            shapeView.tag = index
            shapeView.setOnClickListener(shapeClickListener)
        }
    }

    private fun handleNewUiModel(uiModel: GameViewModel.UiModel) {
        when (uiModel) {
            is GameViewModel.UiModel.Loading -> handleLoadingUiModel(uiModel)
            is GameViewModel.UiModel.Loaded -> handleLoadedUiModel(uiModel)
        }
    }

    private fun handleLoadingUiModel(uiModel: GameViewModel.UiModel.Loading) {

    }

    private fun handleLoadedUiModel(uiModel: GameViewModel.UiModel.Loaded) {
        tvScore.text = uiModel.score.toString()
        val shapeToSelect = uiModel.shapeToSelect
        if (shapeToSelect.changed) {
            svSelect.setShape(shapeToSelect.shape)
        }
        uiModel.shapes.forEachIndexed { index, uiShape ->
            if (uiShape.changed) {
                shapes[index].setShape(uiShape.shape)
            }
        }
    }
}