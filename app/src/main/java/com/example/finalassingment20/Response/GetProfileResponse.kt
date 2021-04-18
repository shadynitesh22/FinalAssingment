package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.User


data class GetProfileResponse (
        val success: Boolean? = null,
        val data: User? = null
)