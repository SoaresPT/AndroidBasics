package com.example.parlament_app.data.network

import com.example.parlament_app.data.model.ExtraMPData
import com.example.parlament_app.data.model.MP
import retrofit2.http.GET

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Retrofit API service interface for fetching MP data and extra MP data.
interface MPApiService {
    @GET("~peterh/seating.json")
    suspend fun getMPs(): List<MP>

    @GET("~peterh/extras.json")
    suspend fun getExtraMPData(): List<ExtraMPData>
}