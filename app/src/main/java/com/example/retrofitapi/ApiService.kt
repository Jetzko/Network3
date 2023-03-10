package com.example.retrofitapi


import com.example.retrofitapi.network.Data
import retrofit2.http.GET

interface ApiService {
    @GET("/champions?lang=it_IT")
    suspend fun  getDetails(): Data
}