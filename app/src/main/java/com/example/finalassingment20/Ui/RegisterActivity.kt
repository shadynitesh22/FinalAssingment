package com.example.finalassingment20.Ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.UserRepository
import com.example.finalassingment20.entity.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity(), SensorEventListener {

    private var REQUEST_GALLERY_CODE = 3
    private var REQUEST_CAMERA_CODE = 2
    private var imageURL: String? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var spinner: Spinner
    private var sensor: Sensor? = null
    private var selectedI =""
    private lateinit var btnAddStudent: Button
    private lateinit var login: Button
    private val category = arrayListOf("Owner", "Renter")
    private lateinit var imgProfile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnAddStudent = findViewById(R.id.btnAddStudent)
        login=findViewById(R.id.login)
        spinner = findViewById(R.id.spinner)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        imgProfile=findViewById(R.id.userimg)
        imgProfile.setOnClickListener {
            loadPopupMenu()
        }
        btnAddStudent.setOnClickListener {
           registerUser()
        }
        val adapter
                = ArrayAdapter(this, android.R.layout.simple_list_item_1, category)
        spinner.adapter=adapter
        spinner.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                selectedI = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@RegisterActivity,"$selectedI",Toast.LENGTH_SHORT).show()


            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }



    }
    private fun registerUser(){
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (password != confirmPassword) {
            etPassword.error = "Password does not match"
            etPassword.requestFocus()
            return
        } else {
            val User = User(
                    username = username,
                    email = email,
                    phone = phone,
                    password = password,
                    status = selectedI
            )
            val user =
                    User(
                            username = username,
                            email = email,
                            phone = phone,
                            password = password,
                            status = selectedI
                    )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepo = UserRepository()
                    val response = userRepo.registerUser(user)
                    if (response.success == true) {
                        if (imageURL != null) {
                            uploadImage(response.data!!._id!!)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                    this@RegisterActivity,
                                    "User Registered", Toast.LENGTH_SHORT
                            ).show()
                            etEmail.setText("")
                            etPhone.setText("")
                            etUsername.setText("")
                            etPassword.setText("")
                            etConfirmPassword.setText("")
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                                this@RegisterActivity,
                                "Username cannot be duplicate", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            // Api code goes here

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
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    private fun loadPopupMenu() {
        val popupMenu = PopupMenu(this, imgProfile)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    private fun uploadImage(userId: String) {
        if (imageURL != null) {
            val imageFile = File(imageURL!!)
            val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            val body =
                    MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepo = UserRepository()
                    val response = userRepo.userImageUpload(userId, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegisterActivity, "Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("My Error ", ex.localizedMessage)
                        Toast.makeText(
                                this@RegisterActivity,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageURL = cursor.getString(columnIndex)
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageURL))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageURL = file!!.absolutePath
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageURL))
            }
        }
    }

    private fun bitmapToFile(
            bitmap: Bitmap,
            fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }


}
