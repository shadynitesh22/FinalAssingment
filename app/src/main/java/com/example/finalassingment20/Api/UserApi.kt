package com.example.finalassingment20.Api

import com.example.finalassingment20.Response.GetProfileResponse
import com.example.finalassingment20.Response.ImageResponse
import com.example.finalassingment20.Response.LoginResponse
import com.example.finalassingment20.Response.UpdateUserResponse
import com.example.finalassingment20.entity.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("auth/registeruser")
    suspend fun registerUser(
        @Body user : User
    ): Response<LoginResponse>




    @FormUrlEncoded
    @POST("auth/loginuser")
    suspend fun checkUser(

        @Field("username")username :String,
        @Field("password")password :String,
        @Field("status")status :String

    ): Response<LoginResponse>
    @GET("auth/me")
    suspend fun getMe(
            @Header("Authorization") token: String,
    ): Response<GetProfileResponse>

    @PUT("auth/update/user/{id}")
    suspend fun updateUser(

            @Path("id")id:String,
            @Body user: User
    ): Response<UpdateUserResponse>

    @Multipart
    @PUT("auth/user/{id}/photo")
    suspend fun userImageUpload(
            @Path("id") id: String,
            @Part file: MultipartBody.Part
    ): Response<ImageResponse>
}