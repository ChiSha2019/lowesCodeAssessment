package com.example.lowes_code_assesment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lowes_code_assesment.data.WeatherForecast
import com.example.lowes_code_assesment.ui.WeatherDetailScreen
import com.example.lowes_code_assesment.ui.WeatherForecastListScreen
import com.example.lowes_code_assesment.ui.WeatherSearchScreen
import com.example.lowes_code_assesment.ui.theme.Lowes_code_assesmentTheme
import com.example.lowes_code_assesment.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lowes_code_assesmentTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val weatherViewModel: WeatherViewModel = viewModel()
    var selectedForecast by remember { mutableStateOf<WeatherForecast?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("search") {
                WeatherSearchScreen(
                    onNavigateToForecast = {
                        navController.navigate("forecast")
                    },
                    viewModel = weatherViewModel
                )
            }
            
            composable("forecast") {
                WeatherForecastListScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToDetail = { forecast ->
                        selectedForecast = forecast
                        navController.navigate("detail")
                    },
                    viewModel = weatherViewModel
                )
            }
            
            composable("detail") {
                WeatherDetailScreen(
                    forecast = selectedForecast,
                    cityName = weatherViewModel.cityName.value,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}