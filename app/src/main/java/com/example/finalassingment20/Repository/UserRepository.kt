package com.example.finalassingment20.Repository

import com.example.finalassingment20.Api.MyapiRequest
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.Api.UserApi
import com.example.finalassingment20.Response.LoginResponse
import com.example.finalassingment20.entity.User

class UserRepository: MyapiRequest() {

    private val UserAPI= ServiceBuilder.buildService(UserApi::class.java)

    suspend fun registerUser(user: User): LoginResponse {
        return apiRequest {
            UserAPI.registerUser(user)
        }

    }
    //Login User
    suspend fun loginUser(username:String, paswword: String):LoginResponse{
        return apiRequest {
            UserAPI.checkUser(username,paswword)
        }
    }
}