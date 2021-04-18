package com.example.finalassingment20.Repository

import com.example.finalassingment20.Api.MyapiRequest
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.Api.UserApi
import com.example.finalassingment20.Response.GetProfileResponse
import com.example.finalassingment20.Response.ImageResponse
import com.example.finalassingment20.Response.LoginResponse
import com.example.finalassingment20.Response.UpdateUserResponse
import com.example.finalassingment20.entity.User
import okhttp3.MultipartBody

class UserRepository: MyapiRequest() {

    private val UserAPI= ServiceBuilder.buildService(UserApi::class.java)

    suspend fun registerUser(user: User): LoginResponse {
        return apiRequest {
            UserAPI.registerUser(user)
        }

    }
    //Login User
    suspend fun loginUser(username:String, password: String,status: String):LoginResponse{
        return apiRequest {
            UserAPI.checkUser(username,password, status)
        }
    }

    suspend fun getMe(): GetProfileResponse {
        return apiRequest {
            UserAPI.getMe(ServiceBuilder.token!!)
        }
    }

    suspend fun updateUser(id:String, user: User): UpdateUserResponse {
        return apiRequest {
            UserAPI.updateUser(id, user)
        }
    }

    suspend fun userImageUpload(id: String, body: MultipartBody.Part)
            : ImageResponse {
        return apiRequest {
            UserAPI.userImageUpload(id, body)
        }
    }
}