package uk.co.jordanterry.shapes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getCurrentTime: GetCurrentTime
) : ViewModel() {

    private var isPlaying = false

    private val mutableUiModel = MutableLiveData<UiModel>(UiModel.Loading)
    val uiModel: LiveData<UiModel> =mutableUiModel

    fun init() {
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

        mutableUiModel.value = UiModel.Loaded(
            0, shapeToSelect, shapes
        )
        viewModelScope.launch {
            isPlaying = true
            updateShapes()
        }
    }

    private suspend fun updateShapes() = withContext(Dispatchers.IO) {
        while (isPlaying) {
            delay(100)
            val currentUiModel = uiModel.value
            if (currentUiModel is UiModel.Loaded) {
                val shapes = currentUiModel.shapes
                val newShapes = shapes.map(::updateShapeIfRequired)
                withContext(Dispatchers.Main) {
                    mutableUiModel.value = currentUiModel.copy(
                        shapeToSelect = updateShapeIfRequired(currentUiModel.shapeToSelect),
                        shapes = newShapes
                    )
                }
            }
        }

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

    fun shapeSelected(position: Int) {
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
        mutableUiModel.value =
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
        mutableUiModel.value = currentUiModel.copy(
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
        isPlaying = false
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
                val changeTime: Long,
                val changed: Boolean
            )
        }
    }
}
