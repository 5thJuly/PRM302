package com.example.myfap_v3.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class ScheduleViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(Calendar.getInstance())
    val selectedDate: LiveData<Calendar> = _selectedDate

    // Tạo một MutableLiveData cho danh sách các môn học
    private val _scheduleItems = MutableLiveData<List<ScheduleItemData>>()
    val scheduleItems: LiveData<List<ScheduleItemData>> = _scheduleItems

    init {
        // Khởi tạo danh sách ban đầu với môn học của ngày hiện tại
        updateScheduleForSelectedDate()
    }

    // Khi thay đổi ngày, cập nhật selectedDate và gọi updateScheduleForSelectedDate
    fun setSelectedDate(date: Calendar) {
        _selectedDate.value = date
        updateScheduleForSelectedDate()
    }

    // Cập nhật danh sách các môn học theo ngày đã chọn
    private fun updateScheduleForSelectedDate() {
        val selectedDate = _selectedDate.value ?: return
        val selectedDayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)

        // Lọc danh sách môn học theo ngày trong tuần đã chọn
        val filteredItems = generateScheduleItems().filter { item ->
            item.day == selectedDayOfWeek
        }

        // Cập nhật lại LiveData của danh sách môn học
        _scheduleItems.value = filteredItems
    }

    // Hàm tạo dữ liệu giả lập về các môn học
    private fun generateScheduleItems(): List<ScheduleItemData> {
        return listOf(
            ScheduleItemData("Mobile Programming", "PhuongLHK", "08:00", "10:00", "BE-301", "Lectures", Calendar.MONDAY),
            ScheduleItemData("Web Development", "TuanVM", "10:30", "12:30", "BE-302", "Labs", Calendar.MONDAY),
            ScheduleItemData("Data Structures", "HungNV", "13:30", "15:30", "BE-303", "Lectures", Calendar.TUESDAY),
            ScheduleItemData("Software Engineering", "ThaoLTT", "08:00", "10:00", "BE-304", "Seminars", Calendar.WEDNESDAY),
            ScheduleItemData("Database Systems", "AnhNT", "10:30", "12:30", "BE-305", "Labs", Calendar.WEDNESDAY),
            ScheduleItemData("Artificial Intelligence", "LongDT", "13:30", "15:30", "BE-306", "Lectures", Calendar.THURSDAY),
            ScheduleItemData("Software Development Project", "HoangNT", "07:30", "09:30", "BE-308", "Lectures", Calendar.FRIDAY),
            ScheduleItemData("Philosophy of Marxism – Leninism", "DuyNK32", "10:00", "12:00", "BE-301", "Lectures", Calendar.FRIDAY)
        )
    }
}
