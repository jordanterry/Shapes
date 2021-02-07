package uk.co.jordanterry.shapes

import androidx.lifecycle.ViewModelProvider
import dagger.Provides
import uk.co.jordanterry.shapes.metrics.Metrics

@dagger.Module
class ShapesModule {

    @Provides
    fun providesGetCurrentTime(): GetCurrentTime {
        return GetCurrentTimeImpl()
    }

    @Provides
    fun providesSquaresViewModelFactory(
        getCurrentTime: GetCurrentTime,
        metrics: Metrics
    ): ViewModelProvider.Factory {
        return SquaresViewModelFactory(getCurrentTime, metrics)
    }

    @Provides
    fun providesStartGame(): StartGame {
        return StartGameImpl()
    }
}