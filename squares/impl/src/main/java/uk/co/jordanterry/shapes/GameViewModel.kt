package uk.co.jordanterry.shapes

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.jordanterry.shapes.metrics.Metrics

class GameViewModelImpl(
    private val getCurrentTime: GetCurrentTime,
    private val metrics: Metrics,
    private val handler: Handler
) : GameViewModel() {
    private val updateRunnable: Runnable = Runnable {
        updateShapes()
    }
    override val uiModel: MutableLiveData<UiModel> =
        MutableLiveData(UiModel.Loading)

    init {
        uiModel.value =
            UiModel.Loading
    }

    override fun init() {
        metrics.sendEvent("game_loaded")
        val shapeToSelect =
            UiModel.Loaded.UiShape(
                shape = Shape.values()
                    .filter { it != Shape.Dash }
                    .random(),
                changeTime = getCurrentTime() + getUpdateTime(),
                changed = true
            )
        val shapes = (1..16).map {
            Shape.Dash
        }.map { shape ->
            UiModel.Loaded.UiShape(
                shape,
                getCurrentTime() + (500..1000).random(),
                true
            )
        }

        uiModel.value = UiModel.Loaded(
            0, shapeToSelect, shapes
        )
        updateShapes()
    }

    private fun updateShapes() {
        val currentUiModel = uiModel.value
        if (currentUiModel is UiModel.Loaded) {
            val shapes = currentUiModel.shapes
            val newShapes = shapes.map(::updateShapeIfRequired)
            uiModel.value = currentUiModel.copy(
                shapeToSelect = updateShapeIfRequired(currentUiModel.shapeToSelect),
                shapes = newShapes
            )
        }
        handler.postDelayed(updateRunnable, 100)
    }

    private fun updateShapeIfRequired(uiShape: UiModel.Loaded.UiShape): UiModel.Loaded.UiShape {
        return if (getCurrentTime() > uiShape.changeTime) {
            updateShape(uiShape)
        } else {
            uiShape.copy(changed = false)
        }
    }

    private fun updateShape(uiShape: UiModel.Loaded.UiShape): UiModel.Loaded.UiShape {
        return UiModel.Loaded.UiShape(
            Shape.values()
                .filter { it != uiShape.shape && it != Shape.Dash }
                .random(),
            getCurrentTime() + getUpdateTime(),
            true
        )
    }

    private fun getUpdateTime(): Long {
        return (1500L..2500L).random()
    }

    override fun shapeSelected(position: Int) {
        val currentUiModel = uiModel.value
        if (currentUiModel is UiModel.Loaded) {
            if (currentUiModel.shapes[position].shape == currentUiModel.shapeToSelect.shape) {
                validSelection(currentUiModel, position)
            } else {
                invalidSelection(currentUiModel, position)
            }
        }
    }

    private fun validSelection(currentUiModel: UiModel.Loaded, position: Int) {
        uiModel.value =
            currentUiModel.copy(
                score = currentUiModel.score + 1,
                shapes = currentUiModel.shapes.mapIndexed { index, uiShape ->
                    if (index == position) {
                        UiModel.Loaded.UiShape(
                            Shape.Dash,
                            getCurrentTime() + 1000,
                            true
                        )
                    } else {
                        uiShape.copy(changed = false)
                    }
                })

    }

    private fun invalidSelection(currentUiModel: UiModel.Loaded, position: Int) {
        stopUpdates()
        uiModel.value = currentUiModel.copy(
            shapes = currentUiModel.shapes.mapIndexed { index, uiShape ->
                if (index != position) {
                    UiModel.Loaded.UiShape(
                        Shape.Dash,
                        getCurrentTime(),
                        true
                    )
                } else {
                    uiShape.copy(changed = false)
                }
            }
        )

    }

    private fun stopUpdates() {
        handler.removeCallbacks(updateRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdates()
    }
}

abstract class GameViewModel : ViewModel() {
    abstract val uiModel: LiveData<UiModel>

    abstract fun init()

    abstract fun shapeSelected(position: Int)

    sealed class UiModel {
        object Loading : UiModel()
        data class Loaded(
            val score: Int,
            val shapeToSelect: UiShape,
            val shapes: List<UiShape>
        ) : UiModel() {
            data class UiShape(
                val shape: Shape,
                val changeTime: Long,
                val changed: Boolean
            )
        }
    }
}