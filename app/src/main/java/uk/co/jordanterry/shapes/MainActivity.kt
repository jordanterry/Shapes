package uk.co.jordanterry.shapes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val updateRunnable: Runnable = Runnable {
        updateShapes()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateShapes()
    }


    private fun updateShapes() {
        findViewById<ShapeView>(R.id.svOne).updateIfRequired()
        findViewById<ShapeView>(R.id.svTwo).updateIfRequired()
        findViewById<ShapeView>(R.id.svThree).updateIfRequired()
        findViewById<ShapeView>(R.id.svFour).updateIfRequired()
        findViewById<ShapeView>(R.id.svFive).updateIfRequired()
        findViewById<ShapeView>(R.id.svSix).updateIfRequired()
        findViewById<ShapeView>(R.id.svSeven).updateIfRequired()
        findViewById<ShapeView>(R.id.svEight).updateIfRequired()
        findViewById<ShapeView>(R.id.nine).updateIfRequired()
        findViewById<ShapeView>(R.id.ten).updateIfRequired()
        findViewById<ShapeView>(R.id.eleven).updateIfRequired()
        findViewById<ShapeView>(R.id.twelve).updateIfRequired()
        findViewById<ShapeView>(R.id.thirteen).updateIfRequired()
        findViewById<ShapeView>(R.id.fourteen).updateIfRequired()
        findViewById<ShapeView>(R.id.fifteen).updateIfRequired()
        findViewById<ShapeView>(R.id.sixteen).updateIfRequired()
        handler.postDelayed(updateRunnable, 100)
    }
}