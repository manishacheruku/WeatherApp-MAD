package com.example.weatherapp.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentmanagement.screens.SignUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    var permissionRequested by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    LaunchedEffect(permissionRequested) {
        if (!permissionRequested) {
            permissionRequested = true
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // UI for the location permission screen
    // You can customize this based on your app's design
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Permission") },
//                elevation = 4.dp
            )
        },
        content = {
            it.calculateTopPadding()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("This app requires location permission.")
            }
        }
    )
}

@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LocationPermissionScreen(
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionDeniedScreen() {
    // UI for the case when the user denies the location permission
    // You can customize this based on your app's design
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permission Denied") },
//                elevation = 4.dp
            )
        },
        content = {
            it.calculateTopPadding()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Location permission is required for this app to function.")
            }
        }
    )
}

@Composable
fun LocationPermissionDeniedHandler() {
    LocationPermissionDeniedScreen()
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "location_permission_handler"
    ) {
        composable("location_permission_handler") {
            LocationPermissionHandler(
                onPermissionGranted = {
                    // Handle permission granted
                    navController.navigate("login_screen")
                },
                onPermissionDenied = {
                    // Handle permission denied
                    navController.navigate("location_permission_denied_handler")
                }
            )
        }
        composable("location_permission_denied_handler") {
            LocationPermissionDeniedHandler()
        }
        composable("main_content") {
            WeatherMainScreen( navController)
            // Your main content composable goes here
            // This is where you navigate after getting location permission
        }
        composable("login_screen") {
            LoginScreen( navController)
            // Your main content composable goes here
            // This is where you navigate after getting location permission
        }
        composable("forget_screen") {
            ForgetScreen( navController)
            // Your main content composable goes here
            // This is where you navigate after getting location permission
        }
        composable("sign_up") {
            SignUp(navController )
            // Your main content composable goes here
            // This is where you navigate after getting location permission
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
//    val localContext= LocalContext.current
    MyApp()
}
