package com.example.finalassingment20.Ui

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity() ,SensorEventListener {
    private lateinit var linearLayout: LinearLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tvRegister: TextView
    private lateinit var btnLogin: Button
    private lateinit var spinner: Spinner
    private var selectedI =""
    private lateinit var chkRememberMe: CheckBox
    private lateinit var sensorManager: SensorManager
    private val category = arrayListOf("Owner", "Renter")
    private var sensor: Sensor? = null
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        spinner = findViewById(R.id.spinner)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        chkRememberMe = findViewById(R.id.chkRememberMe)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        checkRunTimePermission()

        btnLogin.setOnClickListener {
                login()


        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this@LogInActivity, RegisterActivity::class.java))
        }


        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val adapter
                = ArrayAdapter(this, android.R.layout.simple_list_item_1, category)
        spinner.adapter=adapter
        spinner.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                selectedI = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@LogInActivity,"$selectedI",Toast.LENGTH_SHORT).show()


            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }
    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            flag = false
        }
        return flag
    }
    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@LogInActivity, permissions, 1)
    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val status= selectedI
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.loginUser(username, password,status)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer " + response.token
                    if (status=="Owner") {
                        startActivity(
                            Intent(
                                this@LogInActivity,
                                DashboardActivity::class.java
                            )

                        )


                    finish()
                    }else{
                        if (status=="Renter"){
                            startActivity(
                                Intent(
                                    this@LogInActivity,
                                    HomeActivity::class.java
                                )

                            )

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK", View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LogInActivity,
                        "Login error", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]


        if(values<=4)
            login()
            //testing level




        else
            return;
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
