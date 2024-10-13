package com.example.parlament_app.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Singleton object providing a Retrofit instance for API calls.
object RetrofitInstance {
    private const val BASE_URL = "https://users.metropolia.fi/"

    // Lazy initialization of the Retrofit API service
    val api: MPApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MPApiService::class.java)
    }
}