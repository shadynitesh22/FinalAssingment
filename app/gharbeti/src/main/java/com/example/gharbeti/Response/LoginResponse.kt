package com.example.gharbeti.Response


import com.example.gharbeti.entity.User

data class LoginResponse(
    val success:Boolean?=null,
    val token:String?=null,
    val data: User? = null
)