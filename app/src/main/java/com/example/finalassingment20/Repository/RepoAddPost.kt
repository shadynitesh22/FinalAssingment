package com.example.finalassingment20.Repository

import com.example.finalassingment20.Api.MyapiRequest
import com.example.finalassingment20.Api.PostApi
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.Response.AddPostResponse
import com.example.finalassingment20.Response.DeleteResponse
import com.example.finalassingment20.Response.GetAllPostResponse
import com.example.finalassingment20.Response.ImageResponse
import com.example.finalassingment20.entity.Post
import okhttp3.MultipartBody

class RepoAddPost: MyapiRequest() {

    private val postAPI = ServiceBuilder.buildService(PostApi::class.java)

    //Add Student
    suspend fun addPost(post:Post): AddPostResponse {
        return apiRequest {
            postAPI.addPost(
                    ServiceBuilder.token!!, post
            )
        }
    }

    suspend fun getAllPosts(): GetAllPostResponse {
        return apiRequest {
            postAPI.viewPost(ServiceBuilder.token!!)
        }
    }
    suspend fun deletePosts(id:String): DeleteResponse {
        return apiRequest {
            postAPI.deletePost(ServiceBuilder.token!!,id)
        }
    }
    suspend fun uploadImage(id: String, body: MultipartBody.Part)
            : ImageResponse {
        return apiRequest {
            postAPI.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }
}