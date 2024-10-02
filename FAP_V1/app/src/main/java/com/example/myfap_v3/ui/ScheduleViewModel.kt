package com.example.myfap_v3.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class ScheduleViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData<Calendar>(Calendar.getInstance())
    val selectedDate: LiveData<Calendar> = _selectedDate

    private val _scheduleItems = MutableLiveData<List<ScheduleItemData>>(generateScheduleItems())
    val scheduleItems: LiveData<List<ScheduleItemData>> = _scheduleItems

    fun setSelectedDate(date: Calendar) {
        _selectedDate.value = date
    }

    fun getScheduleForSelectedDate(): List<ScheduleItemData> {
        val dayOfWeek = _selectedDate.value?.get(Calendar.DAY_OF_WEEK) ?: Calendar.MONDAY
        return _scheduleItems.value?.filter { it.day == dayOfWeek } ?: emptyList()
    }
}

fun generateScheduleItems(): List<ScheduleItemData> {
    return listOf(
        ScheduleItemData("Mobile Programming", "PhuongLHK", "08:00", "10:00", "BE-301", "Lectures", Calendar.MONDAY),
        ScheduleItemData("Web Development", "TuanVM", "10:30", "12:30", "BE-302", "Labs", Calendar.MONDAY),
        ScheduleItemData("Data Structures", "HungNV", "13:30", "15:30", "BE-303", "Lectures", Calendar.TUESDAY),
        ScheduleItemData("Software Engineering", "ThaoLTT", "08:00", "10:00", "BE-304", "Seminars", Calendar.WEDNESDAY),
        ScheduleItemData("Database Systems", "AnhNT", "10:30", "12:30", "BE-305", "Labs", Calendar.WEDNESDAY),
        ScheduleItemData("Artificial Intelligence", "LongDT", "13:30", "15:30", "BE-306", "Lectures", Calendar.THURSDAY)
    )
}


