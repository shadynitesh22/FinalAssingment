package com.example.finalassingment20.Api

import com.example.finalassingment20.Response.LoginResponse
import com.example.finalassingment20.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    @POST("auth/registeruser")
    suspend fun registerUser(
        @Body user : User
    ): Response<LoginResponse>




    @FormUrlEncoded
    @POST("auth/loginuser")
    suspend fun checkUser(
        @Field("username")username :String,
        @Field("password")password :String

    ): Response<LoginResponse>
}