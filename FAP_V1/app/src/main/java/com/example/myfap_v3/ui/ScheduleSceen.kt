package com.example.myfap_v3.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePage(scheduleViewModel: ScheduleViewModel) {
    val selectedDate by scheduleViewModel.selectedDate.observeAsState(Calendar.getInstance())
    val scheduleItemsForToday = scheduleViewModel.getScheduleForSelectedDate()
    var selectedTab by remember { mutableStateOf(1) }

    Scaffold(
        topBar = { ScheduleTopAppBar() },
        containerColor = Color(0xFFF8F8F8),
        bottomBar = {
            BottomNavBar(selectedTab = selectedTab) { tabIndex ->
                selectedTab = tabIndex
                when (tabIndex) {
                    0 -> {
                        // Logic cho Calendar
                    }
                    1 -> {
                        // Logic cho Schedule (đang ở đây)
                    }
                    2 -> {
                        // Logic cho Add
                    }
                    3 -> {
                        // Logic cho Notifications
                    }
                    4 -> {
                        // Logic cho Profile
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            WeekSelector(
                selectedDay = selectedDate.get(Calendar.DAY_OF_WEEK),
                onDaySelected = { day ->
                    val newDate = Calendar.getInstance()
                    newDate.set(Calendar.DAY_OF_WEEK, day)
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
    val backgroundColor by animateColorAsState(
        if (isSelected) Color(0xFFFF4081) else Color.Transparent,
        label = "backgroundColor"
    )

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
fun FilterChips(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("All", "Lectures", "Labs", "Seminars")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF4081),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun ScheduleList(selectedDay: Int, selectedFilter: String) {
    val scheduleItems = remember { generateScheduleItems() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(scheduleItems.filter {
            (selectedFilter == "All" || it.type == selectedFilter) && it.day == selectedDay
        }) { item ->
            ScheduleItem(item)
        }
    }
}

@Composable
fun ScheduleItem(item: ScheduleItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                        shape = CircleShape
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