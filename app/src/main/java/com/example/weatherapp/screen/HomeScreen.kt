package com.example.weatherapp.screen


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.api.ApiService
import com.example.weatherapp.model.EmptyWeatherData
import com.example.weatherapp.model.Forecastday
import com.example.weatherapp.model.Hour
import com.example.weatherapp.model.WeatherApiResponse
import com.example.weatherapp.ui.theme.MainTextColor
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utility.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.provider.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun WeatherMainScreen(navController: NavHostController) {
    val authHelper = FirebaseAuth.getInstance()

    var emptyData = EmptyWeatherData;
    val context = LocalContext.current


    var weather by remember { mutableStateOf<WeatherApiResponse?>(emptyData.instance) }

    var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    var lat = "55.3781"
    var long = "3.4360"

    Log.e("lat", "function")
    // get the last location
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            long = location?.longitude.toString();
            lat = location?.latitude.toString();
            Log.e("lat", location?.latitude.toString())
            Log.e("long", location?.longitude.toString())

            CoroutineScope(Dispatchers.IO).launch {


                val weatherApi = ApiClient.getInstance().create(ApiService::class.java)
                val result = weatherApi.getWeather(Constants.key, lat + "," + long, "10")
                Log.d("ayush: ", result.body().toString())
                Log.d("ayush error: ", result.errorBody().toString().toString())
                if (result.body() != null && result.isSuccessful) {
                    weather = result.body()!!

                    Log.d("ayush: ", result.body()!!.location.toString())

                }
                // process response...

            }


        }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .paint(
                // Replace with your image id
                painterResource(id = R.drawable.weather_ev),
                contentScale = ContentScale.FillBounds
            )

            .padding(15.dp)
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    authHelper.signOut()
                    navController.navigate("login_screen")
                },
                colors = ButtonDefaults.buttonColors(Color.Red)

            ) {
                Text(text = "Logout")

            }
        }


        weather?.let { CurrentWeatherCard(it, navController) }

        Text(
            text = "Hourly Forecast",
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(8.dp),
            color = MainTextColor
        )

        if (weather != null) {

            weather!!.forecast.forecastday[0].let { HourlyForecastList(it.hour) }
        }




        Text(
            text = "7-Day Forecast",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(8.dp),
            color = MainTextColor
        )
        if (weather != null) {
            DailyForecastList(weather!!.forecast.forecastday)
        }
    }
}

@Composable
fun CurrentWeatherCard(weather: WeatherApiResponse, navController: NavHostController) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "My Location",
            style = TextStyle(fontSize = 20.sp),
            color = MainTextColor,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = weather.location.name,
            style = TextStyle(fontSize = 16.sp),
            color = MainTextColor,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = weather.current.temp_c.toString() + "°C",
            style = TextStyle(fontSize = 45.sp, fontWeight = FontWeight.Bold),
            color = MainTextColor,
            modifier = Modifier.padding(4.dp)
        )



        Text(
            text = weather.current.condition.text.toString(),
            style = TextStyle(fontSize = 18.sp),
            color = MainTextColor,
            modifier = Modifier.padding(4.dp)
        )

    }

}


@Composable
fun HourlyForecastList(hourlyForecasts: List<Hour>) {
    var hourList = arrayListOf<Hour>()
    hourlyForecasts.forEach {
        if (it.time_epoch > System.currentTimeMillis() / 1000) {
            hourList.add(it)
        }
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hourList) { forecast ->


            val convertedTime = convertTo12HourFormat(forecast.time.toString())
            HourlyForecastItem(
                HourlyForecast(
                    convertedTime,
                    forecast.temp_c.toInt().toString() + "°C",
                    forecast.condition.icon
                )
            )
        }

    }
}

@Composable
fun HourlyForecastItem(hourlyForecast: HourlyForecast) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 4.dp
//        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f), //Card background color
            contentColor = Color.White.copy(alpha = 0.3f)  //Card content color,e.g.text
        )
    ) {
        Column(

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = hourlyForecast.time,
                style = TextStyle(fontSize = 16.sp),
                color = MainTextColor,
                modifier = Modifier.padding(4.dp)
            )

            Image(
//                painter = painterResource(id = R.drawable.weather_icon),
                painter = rememberAsyncImagePainter("https:" + hourlyForecast.weatherIcon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = hourlyForecast.temperature,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(4.dp),
                color = MainTextColor
            )
        }
    }
}

@Composable
fun DailyForecastList(dailyForecasts: List<Forecastday>) {
    LazyColumn {
        items(dailyForecasts) { forecast ->
            var dayName = getDateDayName(forecast.date)
            val tempStr =
                forecast.day.mintemp_c.toInt().toString() + "/" + forecast.day.maxtemp_c.toInt()
                    .toString() + "°C"
            DailyForecastItem(
                DailyForecast(
                    dayName,
                    tempStr,
                    "Description",
                    forecast.day.condition.icon
                )
            )
        }
    }
}


@Composable
fun DailyForecastItem(dailyForecast: DailyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 4.dp
//        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f), //Card background color
            contentColor = Color.White.copy(alpha = 0.3f)  //Card content color,e.g.text
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            Arrangement.SpaceEvenly

        ) {
            Text(
                text = dailyForecast.day,
                style = TextStyle(fontSize = 16.sp),
                color = MainTextColor,
                modifier = Modifier.padding(4.dp)
            )

            Image(
                painter = rememberAsyncImagePainter("https:" + dailyForecast.weatherIcon),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = dailyForecast.temperature,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(4.dp),
                color = MainTextColor

            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    WeatherAppTheme {
//        MainScreen()
    }
}


data class Weather(
    val temperature: String,
    val weatherCondition: String,
    val location: String,
    val weatherIcon: Int
)

data class HourlyForecast(
    val time: String,
    val temperature: String,
    val weatherIcon: String
)

data class DailyForecast(
    val day: String,
    val temperature: String,
    val description: String,
    val weatherIcon: String
)

fun convertTo12HourFormat(timeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("ha", Locale.getDefault())

    val date = inputFormat.parse(timeString)
    return outputFormat.format(date)
}

fun getDateDayName(dateString: String): String {

    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = format.parse(dateString)

    val calendar = Calendar.getInstance()
    calendar.time = date

    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dayFormat.format(calendar.time)
}

fun turnOnLocation(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        // Prompt the user to turn on location services
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}