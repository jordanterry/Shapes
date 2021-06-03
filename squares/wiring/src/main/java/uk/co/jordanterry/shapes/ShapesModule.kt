package uk.co.jordanterry.shapes

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@dagger.Module
class ShapesModule {

    @Provides
    fun providesGetCurrentTime(): GetCurrentTime {
        return GetCurrentTime()
    }


    @Provides
    fun providesStartGame(): StartGame {
        return StartGameImpl()
    }
}