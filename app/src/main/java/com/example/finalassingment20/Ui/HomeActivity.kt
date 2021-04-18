package com.example.finalassingment20.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageButton
import android.widget.ImageView
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

class HomeActivity : AppCompatActivity() {

    private lateinit var profile:ImageButton
    private lateinit var enquiry:ImageButton
    private lateinit var addtocart:ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var userimg:ImageView
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
          //  val intent = Intent(this@HomeActivity, EnquiryActivity::class.java)
            //var bundle = Bundle()
           // bundle.putParcelable("item", )
            //intent.putExtra("myBundle", bundle)
           // Toast.makeText(context, "${item.itemName} added to Cart!!", Toast.LENGTH_SHORT).show()
            //context.startActivity(intent)
        }

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
}