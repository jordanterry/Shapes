package uk.co.jordanterry.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview

class ComposeGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Something(text = "Hello")
        }
    }
}

@Composable
fun Something(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun previewSomething() {
    Something(text = "Testing")
}