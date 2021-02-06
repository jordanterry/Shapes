package uk.co.jordanterry.shapes.di

import dagger.android.ContributesAndroidInjector
import uk.co.jordanterry.shapes.ui.MenuActivity

@dagger.Module
abstract class MenuModule {
    @ContributesAndroidInjector
    abstract fun contributesMenuActivity(): MenuActivity
}