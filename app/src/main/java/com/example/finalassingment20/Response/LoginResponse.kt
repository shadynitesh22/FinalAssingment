package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.User

data class LoginResponse(
    val success:Boolean?=null,
    val token:String?=null,
    val data: User? = null
)