package com.example.finalassingment20.Ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.Api.ServiceBuilder.id
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.RepoAddPost
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

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var profilePic: CircleImageView
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var update: Button
    private lateinit var logout: Button
    private lateinit var sharedPref: SharedPreferences

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        profilePic=findViewById(R.id.profilePic)
        username=findViewById(R.id.username)
        email=findViewById(R.id.email)
        phone=findViewById(R.id.phone)
        update=findViewById(R.id.btnUpdate)
        logout=findViewById(R.id.btnlogout)
            loadAllUserDetails()
            profilePic.setOnClickListener {
                loadPopUpMenu()
            }

            update.setOnClickListener {
                Toast.makeText(this@UpdateProfileActivity, "updated", Toast.LENGTH_SHORT).show()
                updateDetails()
            }
        logout.setOnClickListener {
            sharedPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this,LogInActivity::class.java))
        }

    }
    private fun loadAllUserDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepo = UserRepository()
                val response = userRepo.getMe()
                if (response.success == true){
                  val imagePath = ServiceBuilder.loadImagePath() + response.data?.photo
                    withContext(Dispatchers.Main){
                        Glide.with(this@UpdateProfileActivity)
                                .load(imagePath)
                                .fitCenter()
                               .into(profilePic)

                        username.setText(response.data?.username)
                        email.setText(response.data?.email)
                          phone.setText(response.data?.phone)
//                        password.setText(response.data?.password)

                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateProfileActivity,
                            "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun updateDetails() {

        val username = username.text.toString()
        val email = email.text.toString()
        val phone = phone.text.toString()


        val user =
                User(username = username, email = email,phone = phone)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepo = UserRepository()
                val response = userRepo.updateUser(id.toString(),user)
                if (response.success == true){
                    if (imageUrl != null){
                        uploadImage(response.data!!._id!!)
                    }
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@UpdateProfileActivity,
                                    "Success", Toast.LENGTH_SHORT).show()


                        }
                    }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateProfileActivity,
                            "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun uploadImage(userId: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                    MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userepo = UserRepository()
                    val response = userepo.userImageUpload(userId, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@UpdateProfileActivity, "Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mero Error ", ex.localizedMessage)
                        Toast.makeText(
                                this@UpdateProfileActivity,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }
    }
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this@UpdateProfileActivity, profilePic)
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
                imageUrl = cursor.getString(columnIndex)
                profilePic.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()


            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                profilePic.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
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