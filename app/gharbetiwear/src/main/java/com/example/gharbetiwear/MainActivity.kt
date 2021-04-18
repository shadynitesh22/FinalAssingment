package com.example.gharbetiwear

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : WearableActivity() {
    private lateinit var txt1:EditText
    private lateinit var txt2:EditText
    private lateinit var add:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
        txt1=findViewById(R.id.txt1)
        txt2=findViewById(R.id.txt2)
        add=findViewById(R.id.add)


        add.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = txt1.text.toString()
        val password = txt2.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepositry()
                val response = repository.loginUser(username, password)
                if (response.success == true) {
                    val id = response.data?._id


                    ServiceBuilder.token = "Bearer " + response.token
                    ServiceBuilder.id = id
                    startActivity(
                            Intent(
                                    this@MainActivity,
                                    DashbboardActivity::class.java
                            )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                                Snackbar.make(
                                        linearLayout,
                                        "Invalid credentials",
                                        Snackbar.LENGTH_LONG
                                )
                        snack.setAction("OK") {
                            snack.dismiss()
                        }
                        snack.show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                            this@MainActivity,
                            ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}