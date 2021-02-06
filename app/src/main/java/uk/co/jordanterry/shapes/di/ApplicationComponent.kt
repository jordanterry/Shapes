package uk.co.jordanterry.shapes.di

import dagger.android.AndroidInjectionModule
import uk.co.jordanterry.shapes.ShapesActivityModule
import uk.co.jordanterry.shapes.ShapesApplication
import uk.co.jordanterry.shapes.ShapesModule

@dagger.Component(
    modules = [
        AndroidInjectionModule::class,
        MenuModule::class,
        ShapesActivityModule::class,
        ShapesModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: ShapesApplication)
}