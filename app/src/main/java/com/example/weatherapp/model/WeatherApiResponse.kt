package com.example.weatherapp.model

data class WeatherApiResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)

object EmptyWeatherData {
    val condition=Condition("https://cdn.weatherapi.com/weather/64x64/day/296.png","_")
    val day=Day(condition,0.0,0.0)
    val current=Current(condition,"0")
    val hour=Hour(condition,0.0,"_",0)
    val forcastDay=Forecastday("2023-11-28",0, day, hour = listOf(hour, hour, hour, hour, hour, hour, hour))
    val forecast=Forecast(forecastday = listOf(forcastDay, forcastDay,forcastDay,forcastDay,forcastDay,forcastDay))
    val instance: WeatherApiResponse = WeatherApiResponse(current, forecast, Location("Location"))
}