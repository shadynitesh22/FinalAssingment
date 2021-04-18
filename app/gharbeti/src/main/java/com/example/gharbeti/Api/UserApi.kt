package com.example.gharbeti.Api


import com.example.gharbeti.Response.LoginResponse
import com.example.gharbeti.entity.User
import retrofit2.Response
import retrofit2.http.*

interface UserApi {





    @FormUrlEncoded
    @POST("auth/loginuser")
    suspend fun checkUser(

            @Field("username") username: String,
            @Field("password") password: String

    ): Response<LoginResponse>



}