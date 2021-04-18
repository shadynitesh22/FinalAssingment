package com.example.gharbeti.Repo



import com.example.gharbeti.Api.MyapiRequest
import com.example.gharbeti.Api.ServiceBuilder
import com.example.gharbeti.Api.UserApi
import com.example.gharbeti.Response.LoginResponse

class UserRepository: MyapiRequest() {

    private val UserAPI= ServiceBuilder.buildService(UserApi::class.java)


    //Login User
    suspend fun loginUser(username:String, password: String): LoginResponse {
        return apiRequest {
            UserAPI.checkUser(username,password)
        }
    }


}