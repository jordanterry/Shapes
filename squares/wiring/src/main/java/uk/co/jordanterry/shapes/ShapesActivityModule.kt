package uk.co.jordanterry.shapes

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ShapesModule::class])
abstract class ShapesActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesGameActivity(): GameActivity
}