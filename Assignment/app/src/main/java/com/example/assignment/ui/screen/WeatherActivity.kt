package com.example.assignment.ui.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment.R
import com.example.assignment.viewmodel.WeatherViewModel
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog


class WeatherActivity : ComponentActivity() {
    companion object {
        const val API = "19e5498aae13212bcef61eebecc17219"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherScreen()
        }
    }
}

data class City(
    val name: String,
    val apiName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val selectedCity by weatherViewModel.selectedCity.collectAsState()
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val isError by weatherViewModel.isError.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()
    val weatherData by weatherViewModel.weatherData.collectAsState()
    var showCityDialog by remember { mutableStateOf(false) }

    val cities = listOf(
        City("Hồ Chí Minh", "ho chi minh,vn"),
        City("Hà Nội", "hanoi,vn"),
        City("Đà Nẵng", "da nang,vn"),
        City("Cần Thơ", "can tho,vn"),
        City("Huế", "hue,vn"),
        City("Nha Trang", "nha trang,vn"),
        City("Nam Định", "nam dinh,vn"),
        City("Vũng Tàu", "vung tau,vn"),
        City("Quy Nhơn", "quy nhon,vn"),
        City("Biên Hòa", "bien hoa,vn"),
        City("Tokyo", "tokyo,jp"),
        City("Singapore", "singapore,sg"),
        City("New York", "new york,us"),
        City("London", "london,uk"),
        City("Paris", "paris,fr")
    )

    val colorScheme = MaterialTheme.colorScheme

    val isDaytime = remember {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        currentHour in 6..18
    }

    val gradient = if (isDaytime) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.primary,
                colorScheme.tertiary,
                colorScheme.secondary
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A237E), // Deep night blue
                Color(0xFF283593),
                Color(0xFF3949AB)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        AnimatedVisibility(
            visible = !isLoading && !isError,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item { CitySelectionSection(selectedCity, onCitySelectClick = { showCityDialog = true }) }

                    weatherData?.let { data ->
                        item { WeatherHeader(data) }
                        item { WeatherMainInfo(data) }
                        item { WeatherDetailsGrid(data) }
                    }
                }

                weatherData?.let { data -> AIWeatherAssistantButton(data) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = Color.White,
                        contentColor = Color(0xFF6B52FF),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assistant,
                            contentDescription = "AI Weather Assistant",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoadingAnimation()
    }

    AnimatedVisibility(
        visible = isError,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        ErrorDisplay(errorMessage)
    }

    if (showCityDialog) {
        CitySelectionDialog(
            cities = cities,
            selectedCity = selectedCity,
            onCitySelected = { city ->
                weatherViewModel.updateSelectedCity(city)
                showCityDialog = false
            },
            onDismiss = { showCityDialog = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionDialog(
    cities: List<City>,
    selectedCity: City,
    onCitySelected: (City) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Choose the city",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp)
            ) {
                items(cities) { city ->
                    CityItem(
                        city = city,
                        isSelected = city == selectedCity,
                        onSelect = { onCitySelected(city) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun CityItem(
    city: City,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            Color.Transparent,
        onClick = onSelect
    ) {
        Text(
            text = city.name,
            modifier = Modifier.padding(16.dp),
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun WeatherHeader(data: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.address,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = data.updatedAt,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CitySelectionSection(
    selectedCity: City,
    onCitySelectClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        OutlinedButton(
            onClick = onCitySelectClick,
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedCity.name)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Choose city",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun WeatherMainInfo(data: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.weatherDescription,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            text = data.temp,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Thin
            ),
            color = Color.White
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = data.tempMin,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(50.dp))
            Text(
                text = data.tempMax,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun WeatherDetailsGrid(data: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherDetailItem(
                icon = R.drawable.sunrise,
                title = "Sunrise",
                value = data.sunrise,
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                icon = R.drawable.sunset,
                title = "Sunset",
                value = data.sunset,
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                icon = R.drawable.wind,
                title = "Wind",
                value = "${data.windSpeed} m/s",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherDetailItem(
                icon = R.drawable.pressure,
                title = "Pressure",
                value = "${data.pressure} hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                icon = R.drawable.humidity,
                title = "Humidity",
                value = "${data.humidity}%",
                modifier = Modifier.weight(1f)
            )
            CompactAIWeatherButton(
                data = data,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CompactAIWeatherButton(
    data: WeatherData,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { showDialog = true },
        color = Color.White.copy(alpha = 0.2f),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Assistant,
                contentDescription = "AI Assistant",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "AI Assistant",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Tap for tips",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }

    // Hiển thị dialog khi nhấn vào nút
    if (showDialog) {
        val suggestion = getAIWeatherSuggestion(data)

        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = suggestion.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "AI Weather Assistant",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { showDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Assistant Message
                    Text(
                        text = suggestion.assistantMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Suggestions
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestion.suggestions.forEach { tip ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailItem(
    icon: Int,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    var itemVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        itemVisible = true
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .graphicsLayer {
                alpha = if (itemVisible) 1f else 0f
                translationY = if (itemVisible) 0f else 50f
            }
            .animateContentSize(),
        color = Color.White.copy(alpha = 0.2f),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

data class WeatherData(
    val address: String,
    val updatedAt: String,
    val weatherDescription: String,
    val temp: String,
    val tempMin: String,
    val tempMax: String,
    val sunrise: String,
    val sunset: String,
    val windSpeed: String,
    val pressure: String,
    val humidity: String
)

fun parseWeatherData(response: String?): WeatherData? {
    if (response.isNullOrEmpty()) return null

    return try {
        val jsonObj = JSONObject(response)
        val main = jsonObj.optJSONObject("main")
        val sys = jsonObj.optJSONObject("sys")
        val wind = jsonObj.optJSONObject("wind")
        val weather = jsonObj.optJSONArray("weather")?.optJSONObject(0)

        if (main == null || sys == null || wind == null || weather == null) {
            return null
        }

        val updatedAt = jsonObj.optLong("dt", 0)
        val updatedAtText = if (updatedAt > 0) {
            "Updated at: " + SimpleDateFormat(
                "dd/MM/yyyy hh:mm a",
                Locale.ENGLISH
            ).format(Date(updatedAt * 1000))
        } else {
            "Update time not available"
        }

        WeatherData(
            address = "${jsonObj.optString("name", "Unknown")}, ${sys.optString("country", "")}",
            updatedAt = updatedAtText,
            weatherDescription = weather.optString("description", "Unknown")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            temp = "${main.optString("temp", "N/A")}°C",
            tempMin = "Min Temp: ${main.optString("temp_min", "N/A")}°C",
            tempMax = "Max Temp: ${main.optString("temp_max", "N/A")}°C",
            sunrise = try {
                SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                    .format(Date(sys.getLong("sunrise") * 1000))
            } catch (e: Exception) {
                "N/A"
            },
            sunset = try {
                SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                    .format(Date(sys.getLong("sunset") * 1000))
            } catch (e: Exception) {
                "N/A"
            },
            windSpeed = wind.optString("speed", "N/A"),
            pressure = main.optString("pressure", "N/A"),
            humidity = main.optString("humidity", "N/A")
        )
    } catch (e: Exception) {
        null
    }
}


@Composable
fun ErrorDisplay(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error when fetching data",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LoadingAnimation() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        contentAlignment = Alignment.Center
    ) {
        var currentRotation by remember { mutableStateOf(0f) }
        val rotation = remember {
            Animatable(currentRotation)
        }

        LaunchedEffect(Unit) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    rotationZ = rotation.value
                },
            color = Color.White,
            strokeWidth = 4.dp
        )
    }
}

data class AIWeatherSuggestion(
    val icon: ImageVector,
    val assistantMessage: String,
    val suggestions: List<String>,
    val backgroundColor: Color = Color(0x33FFFFFF)
)

fun getAIWeatherSuggestion(weatherData: WeatherData): AIWeatherSuggestion {
    val temperature = weatherData.temp.replace("°C", "").trim().toFloatOrNull() ?: 20f
    val weatherDescription = weatherData.weatherDescription.lowercase()
    val humidity = weatherData.humidity.replace("%", "").toIntOrNull() ?: 0
    val windSpeed = weatherData.windSpeed.toFloatOrNull() ?: 0f

    return when {
        weatherDescription.contains("rain") -> AIWeatherSuggestion(
            icon = Icons.Default.Umbrella,
            assistantMessage = "I notice it's raining. Let me help you prepare for the wet weather!",
            suggestions = listOf(
                "Carry an umbrella or raincoat",
                "Wear water-resistant footwear",
                if (windSpeed > 5f) "Watch out for strong winds with rain" else "Perfect time for indoor activities"
            )
        )

        temperature > 30f -> AIWeatherSuggestion(
            icon = Icons.Default.WbSunny,
            assistantMessage = "I detect high temperatures. Here's how to stay comfortable:",
            suggestions = listOf(
                "Stay hydrated - drink water regularly",
                if (humidity > 70) "High humidity - limit outdoor activities" else "Seek shaded areas when outside",
                "Wear light, breathable clothing"
            )
        )

        // Cold weather
        temperature < 15f -> AIWeatherSuggestion(
            icon = Icons.Default.AcUnit,
            assistantMessage = "It's quite cold today. Let me help you stay warm:",
            suggestions = listOf(
                "Wear layers to maintain body heat",
                if (windSpeed > 5f) "Strong winds - wear a windbreaker" else "Perfect for a warm beverage",
                "Keep indoor spaces well-ventilated"
            )
        )

        weatherDescription.contains("thunderstorm") -> AIWeatherSuggestion(
            icon = Icons.Default.Thunderstorm,
            assistantMessage = "⚡ Thunderstorm detected! Your safety is my priority:",
            suggestions = listOf(
                "Stay indoors and away from windows",
                "Keep devices charged",
                "Avoid using electrical equipment"
            )
        )

        weatherDescription.contains("clear") -> AIWeatherSuggestion(
            icon = Icons.Default.WbSunny,
            assistantMessage = "What a beautiful day! Here's how to make the most of it:",
            suggestions = listOf(
                if (temperature > 25f) "Great time for outdoor activities" else "Perfect weather for a walk",
                "UV index might be high - use sun protection",
                if (humidity < 60) "Good conditions for outdoor exercise" else "Stay hydrated while outside"
            )
        )

        weatherDescription.contains("cloud") -> AIWeatherSuggestion(
            icon = Icons.Default.Cloud,
            assistantMessage = "Cloudy conditions detected. Here's what you should know:",
            suggestions = listOf(
                if (windSpeed > 5f) "Breezy conditions - great for flying kites!" else "Perfect for outdoor photography",
                "Moderate temperatures expected",
                "Good time for outdoor activities"
            )
        )

        else -> AIWeatherSuggestion(
            icon = Icons.Default.Info,
            assistantMessage = "I'm analyzing the weather conditions for you:",
            suggestions = listOf(
                "Temperature is ${weatherData.temp}",
                "Humidity is at ${weatherData.humidity}",
                "Wind speed is ${weatherData.windSpeed} m/s"
            )
        )
    }
}

@Composable
fun AIWeatherAssistantButton(weatherData: WeatherData) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom =16.dp, end=16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Assistant,
                contentDescription = "AI Weather Assistant",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Dialog
    if (showDialog) {
        val suggestion = getAIWeatherSuggestion(weatherData)

        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = suggestion.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "AI Weather Assistant",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { showDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Assistant Message
                    Text(
                        text = suggestion.assistantMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Suggestions
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestion.suggestions.forEach { tip ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}