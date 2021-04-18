package com.example.finalassingment20.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.finalassingment20.MainActivity
import com.example.finalassingment20.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
               CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            startActivity(
                    Intent(
                            this@SplashScreenActivity,
                            LogInActivity::class.java
                    )
            )
            finish()
        }
    }
}