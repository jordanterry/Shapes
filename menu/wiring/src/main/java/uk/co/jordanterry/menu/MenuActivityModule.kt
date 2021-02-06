package uk.co.jordanterry.menu

import dagger.android.ContributesAndroidInjector

@dagger.Module
abstract class MenuActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesMenuActivity(): MenuActivity
}