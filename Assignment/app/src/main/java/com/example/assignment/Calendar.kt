package com.example.assignment

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import java.util.Calendar

@Composable
fun CalendarView(
    events: Map<String, String>,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<String?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            com.applandeo.materialcalendarview.CalendarView(context).apply {
                val eventsList = mutableListOf<CalendarDay>()

                events.forEach { (dateStr, eventName) ->
                    val (day, month, year) = dateStr.split("-").map { it.toInt() }
                    val calendar = Calendar.getInstance().apply {
                        set(year, month - 1, day)
                    }

                    val calendarDay = CalendarDay(calendar).apply {
                        labelColor = R.color.red
                        imageResource = R.drawable.icon_birthday
                    }
                    eventsList.add(calendarDay)
                }
                setCalendarDays(eventsList)

                setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
                    override fun onClick(calendarDay: CalendarDay) {
                        val day = String.format("%02d", calendarDay.calendar.get(Calendar.DAY_OF_MONTH))
                        val month = String.format("%02d", calendarDay.calendar.get(Calendar.MONTH) + 1)
                        val year = calendarDay.calendar.get(Calendar.YEAR)
                        val dateString = "$day-$month-$year"
                        selectedDate = dateString
                        onDateSelected(dateString)
                    }
                })
            }
        },
        update = { calendarView ->
            val eventsList = mutableListOf<CalendarDay>()
            events.forEach { (dateStr, eventName) ->
                val (day, month, year) = dateStr.split("-").map { it.toInt() }
                val calendar = Calendar.getInstance().apply {
                    set(year, month - 1, day)
                }
                val calendarDay = CalendarDay(calendar).apply {
                    labelColor = R.color.red
                    imageResource = R.drawable.icon_birthday
                }
                eventsList.add(calendarDay)
            }
            calendarView.setCalendarDays(eventsList)
        }
    )
}



