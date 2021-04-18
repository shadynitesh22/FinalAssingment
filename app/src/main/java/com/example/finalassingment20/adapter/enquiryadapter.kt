package com.example.finalassingment20.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.EnquiryRepository
import com.example.finalassingment20.entity.Enquiry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class enquiryadapter(private val lstPost:ArrayList<Enquiry>, val context: Context)
    : RecyclerView.Adapter<enquiryadapter.EnquiryViewHolder>() {

    class EnquiryViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val postName: TextView = view.findViewById(R.id.postname)
        val postPrice: TextView = view.findViewById(R.id.postprice)
        val postImage: ImageView = view.findViewById(R.id.postimg)
        val delete: ImageButton = view.findViewById(R.id.postdel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnquiryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.enquiry_layout, parent, false)
        return EnquiryViewHolder(view)

    }

    override fun onBindViewHolder(holder: EnquiryViewHolder, position: Int) {
        val cart = lstPost[position]
        holder.postName.text = cart.itemName
        holder.postPrice.text = cart.itemPrice.toString()

        Glide.with(context)
                .load(cart.photo)
                .fitCenter()
                .into(holder.postImage)


        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete student")
            builder.setMessage("Are you sure you want to delete ${cart.itemName} ?")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes") { _, _ ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val cartRepo = EnquiryRepository()
                        val response = cartRepo.deleteCartItem(cart._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                        context,
                                        "${cart.itemName} deleted from Cart!!",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                            withContext(Dispatchers.Main) {
                                lstPost.remove(cart)
                                notifyDataSetChanged()
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                    context,
                                    ex.toString(),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            builder.setNegativeButton("No") { _, _ ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return lstPost.size
    }
}
