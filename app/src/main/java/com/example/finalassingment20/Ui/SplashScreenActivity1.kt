package com.example.finalassingment20.Ui

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.example.finalassingment20.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity1 : AppCompatActivity() , SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var next: ImageButton
    private var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen1)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            startActivity(
                Intent(
                    this@SplashScreenActivity1,
                    SplashScreenActivity::class.java
                )
            )
            finish()
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


    }
    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[1]
        //if (values < 0)//left
           // startActivity(Intent(this,LogInActivity::class.java))


        //right
      //  else if (values >0)


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}