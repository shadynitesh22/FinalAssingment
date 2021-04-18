package com.example.finalassingment20.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.finalassingment20.R

class DashboardActivity : AppCompatActivity() {
    private lateinit var btnAddStudent: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        btnAddStudent = findViewById(R.id.addpost)

        btnAddStudent.setOnClickListener {
            startActivity(Intent(this,AddPostActivity::class.java))
        }

          }
    }
