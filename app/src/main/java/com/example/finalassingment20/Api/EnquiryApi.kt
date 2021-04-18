package com.example.finalassingment20.Api

import com.example.finalassingment20.Response.AddEnquiryResponse
import com.example.finalassingment20.Response.DeleteCartResponse
import com.example.finalassingment20.Response.GetEnquiryResponse
import com.example.finalassingment20.adapter.enquiryadapter
import com.example.finalassingment20.entity.Enquiry
import retrofit2.Response
import retrofit2.http.*

interface EnquiryApi {
    @POST("/add/cart")
    suspend fun addItemToCart(
            @Header("Authorization") token: String,
            @Body enquiry: Enquiry
    ): Response<AddEnquiryResponse>

    @GET("/cart/all")
    suspend fun getCartItems(
            @Header("Authorization") token: String,
    ): Response<GetEnquiryResponse>

    @DELETE("/delete/{id}")
    suspend fun deleteCartItem(
            @Header("Authorization") token: String,
            @Path("id") id: String
    ): Response<DeleteCartResponse>
}