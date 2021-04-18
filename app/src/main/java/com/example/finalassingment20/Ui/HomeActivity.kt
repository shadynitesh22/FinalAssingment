package com.example.finalassingment20.Ui

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.EnquiryRepository
import com.example.finalassingment20.Repository.RepoAddPost
import com.example.finalassingment20.Repository.UserRepository
import com.example.finalassingment20.adapter.postadapter
import com.example.finalassingment20.entity.Enquiry
import com.example.finalassingment20.entity.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sqrt
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var tvLight: TextView
    private lateinit var sensorManager: SensorManager
    private lateinit var profile:ImageButton
    private lateinit var enquiry:ImageButton
    private lateinit var addtocart:ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var userimg:ImageView
    private var sensor: Sensor? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)
        recyclerView = findViewById(R.id.recyclerView)
        profile=findViewById(R.id.profile)
        enquiry=findViewById(R.id.message)
        userimg=findViewById(R.id.userimg)
        addtocart=findViewById(R.id.cart)
            loadProfilepic()
        profile.setOnClickListener {

            startActivity(
                Intent(
                    this@HomeActivity,
                    UpdateProfileActivity::class.java
                )

            )
        }

        loadStudents()
        addtocart.setOnClickListener {
            startActivity(
                    Intent(
                            this@HomeActivity,
                            EnquiryActivity::class.java
                    )

            )

            Toast.makeText(this@HomeActivity, "added to Cart!!", Toast.LENGTH_SHORT).show()

        }

        tvLight = findViewById(R.id.lightText)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }

    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            flag = false
        }
        return flag
    }

    private fun loadStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val studentRepository = RepoAddPost()
                val response = studentRepository.getAllPosts()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    val lstStudents = response.data
                    withContext(Dispatchers.Main){
                        val adapter = postadapter(lstStudents as ArrayList<Post>, this@HomeActivity)
                        recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
                        recyclerView.adapter = adapter
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@HomeActivity,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loadProfilepic() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepo = UserRepository()
                val response = userRepo.getMe()
                if (response.success == true){
                    val imagePath = ServiceBuilder.loadImagePath() + response.data?.photo
                    withContext(Dispatchers.Main){
                        Glide.with(this@HomeActivity)
                                .load(imagePath)
                                .fitCenter()
                                .into(userimg)


//

                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity,
                            "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
                startActivity(
                        Intent(
                                this@HomeActivity,
                                UpdateProfileActivity::class.java
                        )

                )
            }

        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
                Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        tvLight.text = values.toString()



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}