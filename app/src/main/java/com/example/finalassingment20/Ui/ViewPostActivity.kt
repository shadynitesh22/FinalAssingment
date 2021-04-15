package com.example.finalassingment20.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.RepoAddPost
import com.example.finalassingment20.adapter.postadapter
import com.example.finalassingment20.entity.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPostActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)
        recyclerView = findViewById(R.id.recyclerView)

        loadStudents()
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
                        val adapter = postadapter(lstStudents as ArrayList<Post>, this@ViewPostActivity)
                        recyclerView.layoutManager = LinearLayoutManager(this@ViewPostActivity)
                        recyclerView.adapter = adapter
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ViewPostActivity,
                            "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}