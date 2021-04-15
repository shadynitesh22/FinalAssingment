package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.Post

data class AddPostResponse (
        val success: Boolean? = null,
        val token: String? = null,
        val data: Post?=null
        )
