package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.Post

data class GetAllPostResponse (
        val success: Boolean? =null,
        val count: Int? = null,
        val data: MutableList<Post>? = null
)
