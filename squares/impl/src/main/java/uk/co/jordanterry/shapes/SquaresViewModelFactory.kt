package uk.co.jordanterry.shapes

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SquaresViewModelFactory(
    private val getCurrentTime: GetCurrentTime
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(getCurrentTime, Handler(Looper.getMainLooper())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}