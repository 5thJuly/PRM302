package com.example.myfap_v3.ui

import android.annotation.SuppressLint
import android.widget.AdapterView.OnItemSelectedListener
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfap_v3.R
import java.util.Calendar
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(scheduleViewModel: ScheduleViewModel) {
    var selectedTab by remember { mutableStateOf(0) }

    // Observe the selected date LiveData
    val selectedDate by scheduleViewModel.selectedDate.observeAsState(Calendar.getInstance())

    // Observe the schedule items LiveData
    val scheduleItemsForToday by scheduleViewModel.scheduleItems.observeAsState(emptyList())

    Scaffold(
        containerColor = Color(0xFFF8F8F8),
        bottomBar = {
            BottomNavBar(selectedTab) { tabIndex ->
                selectedTab = tabIndex

            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Header() }
            item {
                WeekCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        scheduleViewModel.setSelectedDate(date)
                    }
                )
            }
            item { ScheduleToday(scheduleItems = scheduleItemsForToday) } // Lấy danh sách mới từ ViewModel
            item { Reminder() }
        }
    }
}


@Composable
fun Header() {
    val currentTime = remember { Calendar.getInstance() }
    val greeting = remember(currentTime) {
        when (currentTime.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Have a great day!"
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "$greeting,",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            Text(
                "Nam",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                ),
                color = Color.Black
            )
        }
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .shadow(4.dp, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun WeekCalendar(
    selectedDate: Calendar, // Pass the selected date
    onDateSelected: (Calendar) -> Unit
) {
    var currentDate by remember { mutableStateOf(Calendar.getInstance()) }
    var internalSelectedDate by remember { mutableStateOf(selectedDate.clone() as Calendar) } // Track internal selection

    val weekDates = remember(internalSelectedDate) {
        (-3..3).map {
            val calendar = currentDate.clone() as Calendar
            calendar.add(Calendar.DAY_OF_YEAR, it)
            calendar
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentDate.add(Calendar.WEEK_OF_YEAR, -1)
                    currentDate = currentDate.clone() as Calendar
                }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Week")
                }
                Text(
                    text = "${currentDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} ${currentDate.get(Calendar.YEAR)}",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = {
                    currentDate.add(Calendar.WEEK_OF_YEAR, 1)
                    currentDate = currentDate.clone() as Calendar
                }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Week")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDates.forEach { date ->
                    DayItemMain(
                        day = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) ?: "",
                        date = date.get(Calendar.DAY_OF_MONTH).toString(),
                        isSelected = isSameDay(date, internalSelectedDate),
                        onSelectDate = {
                            internalSelectedDate = date.clone() as Calendar
                            onDateSelected(internalSelectedDate) // Update ViewModel with the selected date
                        }
                    )
                }
            }
        }
    }
}


fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

@Composable
fun DayItemMain(day: String, date: String, isSelected: Boolean, onSelectDate: () -> Unit) {
    val backgroundColor = if (isSelected) {
        Brush.verticalGradient(listOf(Color(0xFFFF79A3), Color(0xFFFF4081)))
    } else {
        Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onSelectDate)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) Color.White else Color.Gray
        )
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}



@Composable
fun ScheduleToday(scheduleItems: List<ScheduleItemData>) {
    Text(
        "Schedule Today",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Black
    )

    if (scheduleItems.isEmpty()) {
        Text("No classes scheduled for today", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    } else {
        scheduleItems.forEach { item ->
            ScheduleItem(
                time = "${item.startTime} - ${item.endTime}",
                subject = item.subject,
                lecturer = item.lecturer,
                slot = item.room
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
fun ScheduleItem(time: String, subject: String, lecturer: String, slot: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFF4081),
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = subject, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Text(text = lecturer, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = slot, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFF4081))
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Additional details about the class...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun Reminder() {
    Text(
        "Reminder",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Black
    )
    Text(
        "Don't forget schedule for tomorrow",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    ReminderItem("Seminar: How to get Start up idea?", "12.00 - 16.00")
    Spacer(modifier = Modifier.height(8.dp))
    ReminderItem("Webinar: How to deploy Docker Image?", "12.00 - 16.00")
}

@Composable
fun ReminderItem(title: String, time: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8E24AA)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Event",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Text(text = time, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MaterialTheme {
        MainScreen(
            scheduleViewModel = ScheduleViewModel()
        )
    }
}

