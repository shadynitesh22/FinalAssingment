package com.example.finalassingment20.Response

import com.example.finalassingment20.entity.Enquiry

data class GetEnquiryResponse(
        val success: Boolean? = null,
        val count: Int? =null,
        val data: MutableList<Enquiry>? = null
)
