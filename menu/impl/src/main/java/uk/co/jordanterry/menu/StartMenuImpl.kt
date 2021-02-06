package uk.co.jordanterry.menu

import android.content.Context
import android.content.Intent

class StartMenuImpl : StartMenu {
    override fun start(context: Context) {
        val intent = Intent(context, MenuActivity::class.java)
        context.startActivity(intent)
    }

}