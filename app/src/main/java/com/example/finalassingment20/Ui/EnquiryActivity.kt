package com.example.finalassingment20.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.EnquiryRepository
import com.example.finalassingment20.adapter.enquiryadapter
import com.example.finalassingment20.entity.Enquiry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EnquiryActivity : AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enquiry)
        recyclerViewCart=findViewById(R.id.EnquiryRecyclerView)

        loadCartItems()
    }

    private fun loadCartItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val erepo = EnquiryRepository()
                val response = erepo.getCartItems()
                if (response.success == true){
                    val lstItems = response.data
                    withContext(Dispatchers.Main){
                        val adapter = enquiryadapter(lstItems as ArrayList<Enquiry>, this@EnquiryActivity)
                        recyclerViewCart.layoutManager = LinearLayoutManager(this@EnquiryActivity)
                        recyclerViewCart.adapter = adapter
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EnquiryActivity,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}