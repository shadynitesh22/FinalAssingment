package com.example.finalassingment20.Api

import com.example.finalassingment20.Response.AddPostResponse
import com.example.finalassingment20.Response.DeleteResponse
import com.example.finalassingment20.Response.GetAllPostResponse
import com.example.finalassingment20.Response.ImageResponse
import com.example.finalassingment20.entity.Post
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostApi {
    //Add Student
    @POST("post/")
    suspend fun addPost(
            @Header("Authorization") token: String,
            @Body post: Post
    ): Response<AddPostResponse>

    @GET("post/")
    suspend fun viewPost(
            @Header("Authorization") token: String,
    ): Response<GetAllPostResponse>

    @DELETE("post/{id}")
    suspend fun deletePost(
            @Header("Authorization")token: String,
            @Path("id")id:String
    ): Response<DeleteResponse>


    @Multipart
    @PUT("post/{id}/photo")
    suspend fun uploadImage(
            @Header("Authorization") token: String,
            @Path("id") id: String,
            @Part file: MultipartBody.Part
    ): Response<ImageResponse>
}