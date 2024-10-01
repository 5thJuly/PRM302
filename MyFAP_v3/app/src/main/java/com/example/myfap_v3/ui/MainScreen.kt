package com.example.myfap_v3.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import com.google.android.datatransport.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFF8F8F8),
        bottomBar = { BottomNavBar(selectedTab) { selectedTab = it } }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Header() }
            item { WeekCalendar() }
            item { ScheduleToday() }
            item { Reminder() }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Good Morning,",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            Text(
                "Khoa",
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
fun WeekCalendar() {
    val days = listOf("Mo", "Tu", "Wed", "Th", "Fr", "Sa", "Su")
    val dates = listOf("18", "19", "20", "21", "22", "23", "24")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(days.indices.toList()) { index ->
                DayItem(day = days[index], date = dates[index], isSelected = index == 3)
            }
        }
    }
}

@Composable
fun DayItem(day: String, date: String, isSelected: Boolean) {
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
fun ScheduleToday() {
    Text(
        "Schedule Today",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Black
    )
    ScheduleItem(
        time = "08.00\n10.00",
        subject = "Subject: PRM392 - SE1718",
        lecturer = "Lecturer: PhuongLHK",
        slot = "Slot 1"
    )
    Spacer(modifier = Modifier.height(8.dp))
    ScheduleItem(
        time = "12.00\n14.00",
        subject = "Subject: PRM392 - SE1718",
        lecturer = "Lecturer: PhuongLHK",
        slot = "Slot 2"
    )
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
        MainScreen()
    }
}