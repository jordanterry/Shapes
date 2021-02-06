package uk.co.jordanterry.menu

import dagger.Provides
import uk.co.jordanterry.shapes.StartGame

@dagger.Module
class MenuModule {

    @Provides
    fun providesStartMenu(): StartMenu {
        return StartMenuImpl()
    }
}