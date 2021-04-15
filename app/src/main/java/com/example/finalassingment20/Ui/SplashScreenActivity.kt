package com.example.finalassingment20.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.finalassingment20.MainActivity
import com.example.finalassingment20.R

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var next: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        next=findViewById(R.id.next)
        next.setOnClickListener {
            startActivity(Intent(this,LogInActivity::class.java))
        }
    }
}