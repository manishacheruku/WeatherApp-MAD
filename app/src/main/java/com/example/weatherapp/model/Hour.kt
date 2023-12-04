package com.example.weatherapp.model

data class Hour(

    val condition: Condition,
    val temp_c: Double,
    val time: String,
    val time_epoch: Int
)