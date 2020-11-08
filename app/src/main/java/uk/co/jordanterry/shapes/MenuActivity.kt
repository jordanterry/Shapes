package uk.co.jordanterry.shapes

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private val bStartGame: Button by lazy {
        findViewById<Button>(R.id.bStartGame)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bStartGame.setOnClickListener {
            startActivity(GameActivity.newIntent(this))
        }
    }
}