package com.example.weatherapp.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SplashScreen() {
    val systemUiController: SystemUiController = rememberSystemUiController()

//    systemUiController.isStatusBarVisible = false // Status bar
    systemUiController.isNavigationBarVisible = false // Navigation bar
//    systemUiController.isSystemBarsVisible = false // Status & Navigation bars
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .graphicsLayer(alpha = 1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.weather_logo), // Replace with your image resource
                contentDescription = null, // You can provide a description here
                modifier = Modifier.size(250.dp) // Adjust the size as needed
            )

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    WeatherAppTheme {
        SplashScreen()
    }
}
