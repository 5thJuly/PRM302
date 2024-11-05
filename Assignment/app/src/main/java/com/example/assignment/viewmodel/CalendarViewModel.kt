package com.example.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalendarViewModel(private val repository: Repository) : ViewModel() {

    private val _events = MutableStateFlow<Map<String, String>>(emptyMap())
    val events: StateFlow<Map<String, String>> = _events

    init {
        viewModelScope.launch {
            repository.observeCalendarEvents().collect {
                _events.value = it
            }
        }
    }

    fun addEvent(date: String, event: String) {
        repository.saveCalendarEvent(date, event)
    }

    fun updateEvent(date: String, event: String) {
        repository.updateCalendarEvent(date, event)
    }

    fun deleteEvent(date: String) {
        repository.deleteCalendarEvent(date)
    }
}