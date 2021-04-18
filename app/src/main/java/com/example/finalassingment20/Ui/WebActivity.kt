package com.example.finalassingment20.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.finalassingment20.R

class WebActivity : AppCompatActivity() {
    private lateinit var web:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        web= findViewById(R.id.webview)
        web.loadUrl("https://www.facebook.com/Gharbethi/")
    }
}