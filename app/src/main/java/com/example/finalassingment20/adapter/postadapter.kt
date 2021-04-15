package com.example.finalassingment20.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.R
import com.example.finalassingment20.Repository.RepoAddPost
import com.example.finalassingment20.entity.Post
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class postadapter (
        private val lstStudents:ArrayList<Post>,
        private val context: Context
): RecyclerView.Adapter<postadapter.StudentViewHolder>() {
    class StudentViewHolder (view:View): RecyclerView.ViewHolder(view) {
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val imgProfile: CircleImageView = view.findViewById(R.id.imgProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAge: TextView = view.findViewById(R.id.tvAge)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvGender: TextView = view.findViewById(R.id.tvGender)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mylayout, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = lstStudents[position]
        holder.tvName.text = student.PostName
        holder.tvAge.text = student.PostLocation
        holder.tvAddress.text = student.PostPrice
        holder.tvGender.text = student.PostStatus
        val imagePath = ServiceBuilder.loadImagePath() + student.photo
        if (!student.photo.equals("no-photo.jpg")) {
            Glide.with(context)
                    .load(imagePath)
                    .fitCenter()
                    .into(holder.imgProfile)
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Post")
            builder.setMessage("Are you sure you want to delete ${student.PostName} ??")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val PostRepository = RepoAddPost()
                        val response = PostRepository.deletePosts(student._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                        context,
                                        "Student Deleted",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                            withContext(Dispatchers.Main) {
                                lstStudents.remove(student)
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

        when(student.PostStatus){
            "Male" -> Glide.with(context)
                    .load("https://cdn.wallpapersafari.com/4/72/Ou9IRM.jpg")
                    .into(holder.imgProfile)
            "Female" -> Glide.with(context)
                    .load("https://media.istockphoto.com/vectors/default-avatar-profile-icon-grey-photo-placeholder-hand-drawn-modern-vector-id1273297923?b=1&k=6&m=1273297923&s=612x612&w=0&h=kCbZRaXozftYrZv44poGI6_RrTg7DMa1lIqz_NtZNis=")
                    .into(holder.imgProfile)
            "Other" -> Glide.with(context)
                    .load("https://png.pngtree.com/png-vector/20190217/ourmid/pngtree-smile-vector-template-design-illustration-png-image_555082.jpg")
                    .into(holder.imgProfile)
        }
    }

    override fun getItemCount(): Int {
        return lstStudents.size
    }
}