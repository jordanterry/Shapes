package uk.co.jordanterry.shapes

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import uk.co.jordanterry.shapes.di.DaggerApplicationComponent
import javax.inject.Inject

class ShapesApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent
            .builder()
            .build()
            .inject(this)

    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}