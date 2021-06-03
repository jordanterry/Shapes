package uk.co.jordanterry.shapes

import android.os.Bundle

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import uk.co.jordanterry.shapes.squares.impl.R
import javax.inject.Inject

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private val shapes: List<ShapeView> by lazy {
        requireView().findViewById<Group>(R.id.gShapes).referencedIds.map { requireView().findViewById(it) }
    }

    private val viewModel: GameViewModel by viewModels()

    private val shapeClickListener = View.OnClickListener { view ->
        val position = view.tag
        if (position is Int) {
            viewModel.shapeSelected(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseShapes()
        initialiseViewModel()
    }

    private fun initialiseViewModel() {
        viewModel.uiModel.observe(requireActivity(), { uiModel -> handleNewUiModel(uiModel) })
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
        val tvScore = requireView().findViewById<TextView>(R.id.tvScore)
        val svSelect = requireView().findViewById<ShapeView>(R.id.svSelect)
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