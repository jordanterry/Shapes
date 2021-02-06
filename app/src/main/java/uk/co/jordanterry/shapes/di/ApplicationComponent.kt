package uk.co.jordanterry.shapes.di

import dagger.android.AndroidInjectionModule
import uk.co.jordanterry.menu.MenuActivityModule
import uk.co.jordanterry.menu.MenuModule
import uk.co.jordanterry.shapes.ShapesActivityModule
import uk.co.jordanterry.shapes.ShapesApplication
import uk.co.jordanterry.shapes.ShapesModule

@dagger.Component(
    modules = [
        AndroidInjectionModule::class,
        MenuActivityModule::class,
        MenuModule::class,
        ShapesActivityModule::class,
        ShapesModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: ShapesApplication)
}