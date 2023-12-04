package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") qeary: String,
        @Query("days") days: String
    ): Response<WeatherApiResponse>
}