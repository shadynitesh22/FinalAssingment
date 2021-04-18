package com.example.finalassingment20.Repository

import com.example.finalassingment20.Api.EnquiryApi
import com.example.finalassingment20.Api.MyapiRequest
import com.example.finalassingment20.Api.ServiceBuilder
import com.example.finalassingment20.Response.AddEnquiryResponse
import com.example.finalassingment20.Response.DeleteCartResponse
import com.example.finalassingment20.Response.GetEnquiryResponse
import com.example.finalassingment20.entity.Enquiry

class EnquiryRepository:MyapiRequest()
{
    private val cartAPI= ServiceBuilder.buildService(EnquiryApi::class.java)

    suspend fun addItemToCart(enquiry: Enquiry): AddEnquiryResponse {
        return apiRequest {
            cartAPI.addItemToCart(
                    ServiceBuilder.token!!, enquiry
            )
        }
    }

    suspend fun getCartItems(): GetEnquiryResponse {
        return apiRequest {
            cartAPI.getCartItems(ServiceBuilder.token!!)
        }
    }

    suspend fun  deleteCartItem(id: String): DeleteCartResponse {
        return apiRequest {
            cartAPI.deleteCartItem(ServiceBuilder.token!!,id)
        }
    }
}
