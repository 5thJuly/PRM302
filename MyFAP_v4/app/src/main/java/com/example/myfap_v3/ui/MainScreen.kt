package com.example.myfap_v3.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.ui.theme.DarkColors
import com.example.myfap_v3.ui.theme.LightColors
import com.example.myfap_v3.ui.theme.Pink40
import com.example.myfap_v3.ui.theme.Purple40
import com.example.myfap_v3.ui.theme.Purple80
import com.example.myfap_v3.ui.theme.PurpleGrey40

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(scheduleViewModel: ScheduleViewModel, navController: NavHostController) {
    val selectedDate by scheduleViewModel.selectedDate.observeAsState(Calendar.getInstance())
    val scheduleItemsForToday by scheduleViewModel.scheduleItems.observeAsState(emptyList())

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomNavBar(selectedTab = 0, navController = navController)
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
                item { ScheduleToday(scheduleItems = scheduleItemsForToday) }
                item { Reminder() }
            }
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "$greeting,",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Text(
                    "Nam",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .border(3.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun WeekCalendar(
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit
) {
    var currentDate by remember { mutableStateOf(Calendar.getInstance()) }
    var internalSelectedDate by remember { mutableStateOf(selectedDate.clone() as Calendar) }

    val weekDates = remember(currentDate) {
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
            Spacer(modifier = Modifier.height(12.dp))
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
                            onDateSelected(internalSelectedDate)
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
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onTertiary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onSelectDate)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            if (isSelected) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.2f),
                    radius = 20.dp.toPx(),
                    center = Offset(size.width * 0.7f, size.height * 0.3f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = date,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun ScheduleToday(scheduleItems: List<ScheduleItemData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Schedule Today",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.ExtraBold
            )

            if (scheduleItems.isEmpty()) {
                Text(
                    "No classes scheduled for today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f)
                )
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
    }
}

@Composable
fun ScheduleItem(time: String, subject: String, lecturer: String, slot: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = subject, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = lecturer, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                Text(text = slot, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Additional details about the class...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun Reminder() {
    Column {
        Text(
            "Reminder",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Don't forget schedule for tomorrow",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )
        ReminderItem("Seminar: How to get Start up idea?", "12.00 - 16.00")
        Spacer(modifier = Modifier.height(8.dp))
        ReminderItem("Webinar: How to deploy Docker Image?", "12.00 - 16.00")
    }
}


@Composable
fun ReminderItem(title: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
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
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Text(text = time, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MaterialTheme {
        MainScreen(
            scheduleViewModel = ScheduleViewModel(),
            navController = navController
        )
    }
}
