package uk.co.jordanterry.shapes.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import uk.co.jordanterry.game.GameActivity
import uk.co.jordanterry.shapes.R

class MenuActivity : AppCompatActivity() {

    private val bStartGame: Button by lazy {
        findViewById<Button>(R.id.bStartGame)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bStartGame.setOnClickListener {
            startActivity(
                GameActivity.newIntent(
                    this
                )
            )
        }
    }
}