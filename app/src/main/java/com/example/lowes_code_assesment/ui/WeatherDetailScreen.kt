package com.example.lowes_code_assesment.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lowes_code_assesment.R
import com.example.lowes_code_assesment.data.WeatherForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(
    forecast: WeatherForecast?,
    cityName: String,
    onNavigateBack: () -> Unit
) {
    if (forecast == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_forecast_data))
        }
        return
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.weather_details, cityName)) },
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
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatDateTime(forecast.dateTimeText),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = forecast.weather.firstOrNull()?.main ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = forecast.weather.firstOrNull()?.description?.replaceFirstChar { 
                            if (it.isLowerCase()) it.titlecase() else it.toString() 
                        } ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.temperature_details),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.temperature),
                        value = stringResource(R.string.temperature_fahrenheit, forecast.main.temperature.roundToInt())
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.feels_like),
                        value = stringResource(R.string.temperature_fahrenheit, forecast.main.feelsLike.roundToInt())
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.min_temperature),
                        value = stringResource(R.string.temperature_fahrenheit, forecast.main.tempMin.roundToInt())
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.max_temperature),
                        value = stringResource(R.string.temperature_fahrenheit, forecast.main.tempMax.roundToInt())
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.environmental_conditions),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.humidity),
                        value = stringResource(R.string.percentage, forecast.main.humidity)
                    )
                    
                    WeatherDetailRow(
                        label = stringResource(R.string.pressure),
                        value = stringResource(R.string.pressure_hpa, forecast.main.pressure)
                    )
                    
                    forecast.main.seaLevel?.let { seaLevel ->
                        WeatherDetailRow(
                            label = stringResource(R.string.sea_level_pressure),
                            value = stringResource(R.string.pressure_hpa, seaLevel)
                        )
                    }
                    
                    forecast.main.groundLevel?.let { groundLevel ->
                        WeatherDetailRow(
                            label = stringResource(R.string.ground_level_pressure),
                            value = stringResource(R.string.pressure_hpa, groundLevel)
                        )
                    }
                    
                    forecast.clouds?.let { clouds ->
                        WeatherDetailRow(
                            label = stringResource(R.string.cloudiness),
                            value = stringResource(R.string.percentage, clouds.all)
                        )
                    }
                }
            }
            
            forecast.wind?.let { wind ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.wind_information),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        WeatherDetailRow(
                            label = stringResource(R.string.wind_speed),
                            value = stringResource(R.string.wind_speed_mph, wind.speed)
                        )
                        
                        WeatherDetailRow(
                            label = stringResource(R.string.wind_direction),
                            value = stringResource(R.string.wind_direction_degrees, wind.deg, getWindDirection(wind.deg))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDateTime(dateTimeText: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        val date = inputFormat.parse(dateTimeText)
        date?.let { outputFormat.format(it) } ?: dateTimeText
    } catch (e: Exception) {
        dateTimeText
    }
}

@Composable
private fun getWindDirection(degrees: Int): String {
    return when (degrees) {
        in 0..11 -> stringResource(R.string.wind_n)
        in 12..33 -> stringResource(R.string.wind_nne)
        in 34..56 -> stringResource(R.string.wind_ne)
        in 57..78 -> stringResource(R.string.wind_ene)
        in 79..101 -> stringResource(R.string.wind_e)
        in 102..123 -> stringResource(R.string.wind_ese)
        in 124..146 -> stringResource(R.string.wind_se)
        in 147..168 -> stringResource(R.string.wind_sse)
        in 169..191 -> stringResource(R.string.wind_s)
        in 192..213 -> stringResource(R.string.wind_ssw)
        in 214..236 -> stringResource(R.string.wind_sw)
        in 237..258 -> stringResource(R.string.wind_wsw)
        in 259..281 -> stringResource(R.string.wind_w)
        in 282..303 -> stringResource(R.string.wind_wnw)
        in 304..326 -> stringResource(R.string.wind_nw)
        in 327..348 -> stringResource(R.string.wind_nnw)
        in 349..360 -> stringResource(R.string.wind_n)
        else -> stringResource(R.string.wind_n)
    }
}