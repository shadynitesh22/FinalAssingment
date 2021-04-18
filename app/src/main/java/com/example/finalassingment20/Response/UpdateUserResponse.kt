package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.User


data class UpdateUserResponse(
        val success: Boolean? = null,
        val data: User? = null
)