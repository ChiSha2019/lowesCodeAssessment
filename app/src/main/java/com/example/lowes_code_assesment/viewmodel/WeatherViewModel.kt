package com.example.lowes_code_assesment.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lowes_code_assesment.api.WeatherRepository
import com.example.lowes_code_assesment.data.WeatherForecast
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    
    var isLoading = mutableStateOf(false)
        private set
    
    var weatherForecasts = mutableStateOf<List<WeatherForecast>>(emptyList())
        private set
    
    var cityName = mutableStateOf("")
        private set
    
    var errorMessage = mutableStateOf<String?>(null)
        private set
    
    fun searchWeather(city: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val response = repository.getWeatherForecast(city)
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        weatherForecasts.value = weatherResponse.forecasts
                        cityName.value = weatherResponse.city.name
                    }
                } else {
                    errorMessage.value = "Failed to fetch weather data: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        errorMessage.value = null
    }
}