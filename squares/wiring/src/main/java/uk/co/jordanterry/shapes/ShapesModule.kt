package uk.co.jordanterry.shapes

import androidx.lifecycle.ViewModelProvider
import dagger.Provides

@dagger.Module
class ShapesModule {

    @Provides
    fun providesGetCurrentTime(): GetCurrentTime {
        return GetCurrentTimeImpl()
    }

    @Provides
    fun providesSquaresViewModelFactory(getCurrentTime: GetCurrentTime): ViewModelProvider.Factory {
        return SquaresViewModelFactory(getCurrentTime)
    }

    @Provides
    fun providesStartGame(): StartGame {
        return StartGameImpl()
    }
}