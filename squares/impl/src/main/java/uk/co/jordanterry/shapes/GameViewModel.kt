package uk.co.jordanterry.shapes

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.jordanterry.shapes.metrics.Metrics

class GameViewModel(
    private val getCurrentTime: GetCurrentTime,
    private val metrics: Metrics,
    private val handler: Handler
) : ViewModel() {
    private val updateRunnable: Runnable = Runnable {
        updateShapes()
    }
    private val _uiModel: MutableLiveData<UiModel> = MutableLiveData()
    val uiModel: LiveData<UiModel> = _uiModel

    init {
        _uiModel.value =
            UiModel.Loading
    }

    fun init() {
        metrics.sendEvent("game_loaded")
        val shapeToSelect =
            UiModel.Loaded.UiShape(
                shape = Shape.values()
                    .filter { it != Shape.Dash }
                    .random(),
                changeTime = getCurrentTime() + getUpdateTime()
            )
        val shapes = (1..16).map {
            Shape.Dash
        }.map { shape ->
            UiModel.Loaded.UiShape(
                shape,
                getCurrentTime() + (500..800).random()
            )
        }

        _uiModel.postValue(
            UiModel.Loaded(
                0, shapeToSelect, shapes
            )
        )
        updateShapes()
    }

    private fun updateShapes() {
        val currentUiModel = uiModel.value
        if (currentUiModel is UiModel.Loaded) {
            val shapes = currentUiModel.shapes
            val newShapes = shapes.map(::updateShapeIfRequired)
            _uiModel.postValue(
                currentUiModel.copy(
                    shapeToSelect = updateShapeIfRequired(currentUiModel.shapeToSelect),
                    shapes = newShapes
                )
            )
        }
        handler.postDelayed(updateRunnable, 100)
    }

    private fun updateShapeIfRequired(uiShape: UiModel.Loaded.UiShape): UiModel.Loaded.UiShape {
        return if (getCurrentTime() > uiShape.changeTime) {
            updateShape(uiShape)
        } else {
            uiShape
        }
    }

    private fun updateShape(uiShape: UiModel.Loaded.UiShape): UiModel.Loaded.UiShape {
        return UiModel.Loaded.UiShape(
            Shape.values()
                .filter { it != uiShape.shape && it != Shape.Dash }
                .random(),
            getCurrentTime() + getUpdateTime()
        )
    }

    private fun getUpdateTime(): Long {
        return (1500L..2500L).random()
    }

    fun shapeSelected(position: Int) {
        val currentUiModel = uiModel.value
        if (currentUiModel is UiModel.Loaded) {
            if (currentUiModel.shapes[position].shape == currentUiModel.shapeToSelect.shape) {
                _uiModel.postValue(
                    currentUiModel.copy(
                        score = currentUiModel.score + 1,
                        shapes = currentUiModel.shapes.mapIndexed { index, uiShape ->
                            if (index == position) {
                                UiModel.Loaded.UiShape(
                                    Shape.Dash,
                                    getCurrentTime() + 1000
                                )
                            } else {
                                uiShape
                            }
                        })
                )
            } else {
                stopUpdates()
                _uiModel.postValue(currentUiModel.copy(
                    shapes = currentUiModel.shapes.mapIndexed { index, uiShape ->
                        if (index != position) {
                            UiModel.Loaded.UiShape(
                                Shape.Dash,
                                getCurrentTime()
                            )
                        } else {
                            uiShape
                        }
                    }
                ))
            }
        }
    }

    private fun stopUpdates() {
        handler.removeCallbacks(updateRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdates()
    }

    sealed class UiModel {
        object Loading : UiModel()
        data class Loaded(
            val score: Int,
            val shapeToSelect: UiShape,
            val shapes: List<UiShape>
        ) : UiModel() {
            data class UiShape(
                val shape: Shape,
                val changeTime: Long
            )
        }
    }
}