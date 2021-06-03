package uk.co.jordanterry.shapes

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ShapesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}