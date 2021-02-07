package uk.co.jordanterry.menu

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import uk.co.jordanterry.shapes.StartGame
import uk.co.jordanterry.shapes.metrics.Metrics
import javax.inject.Inject


class MenuActivity : AppCompatActivity() {

    @Inject
    lateinit var startGame: StartGame

    @Inject
    lateinit var metrics: Metrics

    private val bStartGame: Button by lazy {
        findViewById<Button>(R.id.bStartGame)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bStartGame.setOnClickListener {
            metrics.sendEvent("game_started")
            startGame.start(this)
        }
    }
}
