package com.example.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.ui.screen.City
import com.example.assignment.ui.screen.WeatherActivity
import com.example.assignment.ui.screen.WeatherData
import com.example.assignment.ui.screen.parseWeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class WeatherViewModel : ViewModel() {
    private val _selectedCity = MutableStateFlow(City("Hồ Chí Minh", "ho chi minh,vn"))
    val selectedCity = _selectedCity.asStateFlow()

    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun updateSelectedCity(city: City) {
        _selectedCity.value = city
        fetchWeatherData(city)
    }

    private fun fetchWeatherData(city: City) {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                withContext(Dispatchers.IO) {
                    val url = "https://api.openweathermap.org/data/2.5/weather?q=${city.apiName}&units=metric&appid=${WeatherActivity.API}"
                    val response = URL(url).readText()
                    _weatherData.value = parseWeatherData(response)
                }
            } catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        fetchWeatherData(_selectedCity.value)
    }
}
