package com.example.weather_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("q") q: String, @Query("APPID") app_id: String): Call<Weather_Data>
}