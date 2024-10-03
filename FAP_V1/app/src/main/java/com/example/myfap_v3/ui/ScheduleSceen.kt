package com.example.myfap_v3.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePage(scheduleViewModel: ScheduleViewModel) {
    val selectedDate by scheduleViewModel.selectedDate.observeAsState(Calendar.getInstance())
    val scheduleItemsForToday by scheduleViewModel.scheduleItems.observeAsState(emptyList())
    var selectedTab by remember { mutableStateOf(1) }

    Scaffold(
        topBar = { ScheduleTopAppBar() },
        containerColor = Color(0xFFF8F8F8),
        bottomBar = {
            BottomNavBar(selectedTab = selectedTab) { tabIndex ->
                selectedTab = tabIndex
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            WeekSelector(
                selectedDay = selectedDate.get(Calendar.DAY_OF_WEEK) - 1, // Adjust for 0-indexed
                onDaySelected = { day ->
                    val newDate = Calendar.getInstance()
                    newDate.set(Calendar.DAY_OF_WEEK, day + 1) // Adjust back to 1-indexed for Calendar
                    newDate.set(Calendar.HOUR_OF_DAY, 0)
                    newDate.set(Calendar.MINUTE, 0)
                    newDate.set(Calendar.SECOND, 0)
                    newDate.set(Calendar.MILLISECOND, 0)
                    scheduleViewModel.setSelectedDate(newDate)
                }
            )
            ScheduleToday(scheduleItems = scheduleItemsForToday)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopAppBar() {
    TopAppBar(
        title = {
            Text(
                "Schedule",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Implement calendar view */ }) {
                Icon(Icons.Filled.Person, contentDescription = "Calendar View")
            }
            IconButton(onClick = { /* TODO: Implement settings */ }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun WeekSelector(selectedDay: Int, onDaySelected: (Int) -> Unit) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val dates = listOf("18", "19", "20", "21", "22", "23", "24")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(days.indices.toList()) { index ->
            DayItem(
                day = days[index],
                date = dates[index],
                isSelected = index == selectedDay,
                onSelected = { onDaySelected(index) }
            )
        }
    }
}

@Composable
fun DayItem(day: String, date: String, isSelected: Boolean, onSelected: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFFF4081) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelected)
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 16.dp)
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
fun ScheduleItem(item: ScheduleItemData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.lecturer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.startTime} - ${item.endTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF4081)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFF79A3), Color(0xFFFF4081))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.room,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class ScheduleItemData(
    val subject: String,
    val lecturer: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val type: String,
    val day: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewSchedulePage() {
    MaterialTheme {
        SchedulePage(ScheduleViewModel())
    }
}
