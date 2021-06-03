package uk.co.jordanterry.shapes

import android.content.Context
import android.content.Intent

class StartGameImpl : StartGame {
    override fun start(context: Context) {
        val intent = Intent(context, GameFragment::class.java)
        context.startActivity(intent)
    }
}