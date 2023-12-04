package com.example.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.screen.SplashScreen
import com.example.weatherapp.screen.WeatherMainScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.delay
import android.Manifest
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import com.example.weatherapp.screen.MyApp


class MainActivity : ComponentActivity() {
    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            WeatherComponent()
            WeatherAppTheme {
                if (isLocationPermissionGranted()) {
                    // Permission already granted, proceed with your location-related logic
                } else {
                    // Request location permission
                    requestLocationPermission()
                }
                val splashDuration = 2000L // 2 seconds

                // Create a splash screen composable that transitions to the main screen
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(splashDuration)

                    showSplash = false
                }

                // When showSplash is true, display the splash screen, otherwise navigate to the main screen
                if (showSplash) {
                    SplashScreen()


                } else {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        MyApp()
                        turnOnLocation(this)

                    }
                    else{
                        turnOnLocation(this)
                        MyApp()
                    }


                }
            }

        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted, proceed with your location-related logic
                } else {
                    // Location permission denied, handle accordingly (e.g., show a message)
                }
                return
            }
        }
    }

    fun turnOnLocation(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Prompt the user to turn on location services
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
    }
}


