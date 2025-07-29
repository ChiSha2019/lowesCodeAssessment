package com.example.lowes_code_assesment.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lowes_code_assesment.R
import com.example.lowes_code_assesment.data.WeatherForecast
import com.example.lowes_code_assesment.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (WeatherForecast) -> Unit,
    viewModel: WeatherViewModel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { 
                Text(stringResource(R.string.weather_in, viewModel.cityName.value))
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Blue,
                titleContentColor = androidx.compose.ui.graphics.Color.White,
                navigationIconContentColor = androidx.compose.ui.graphics.Color.White
            )
        )
        
        if (viewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewModel.weatherForecasts.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_weather_data))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.weatherForecasts.value) { forecast ->
                    WeatherForecastItem(
                        forecast = forecast,
                        onClick = { onNavigateToDetail(forecast) }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherForecastItem(
    forecast: WeatherForecast,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatDateTime(forecast.dateTimeText),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.temperature_fahrenheit, forecast.main.temperature.roundToInt()),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = forecast.weather.firstOrNull()?.main ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = forecast.weather.firstOrNull()?.description?.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase() else it.toString() 
                    } ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatDateTime(dateTimeText: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
        val date = inputFormat.parse(dateTimeText)
        date?.let { outputFormat.format(it) } ?: dateTimeText
    } catch (e: Exception) {
        dateTimeText
    }
}