package com.example.assignment

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment.ui.screen.WeatherScreen
import com.example.assignment.ui.screen.CalendarScreen
import com.example.assignment.ui.screen.TaskScreen
import com.example.assignment.viewmodel.TodoViewModel

import com.example.assignment.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val viewModel: TodoViewModel = viewModel()
    var selectedTab by remember { mutableIntStateOf(0) }
    val calendarEvents by viewModel.calendarEvents.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Padding for NavigationBar
        ) {
            when (selectedTab) {
                0 -> TaskScreen(viewModel = viewModel)
                1 -> WeatherScreen()
                2 -> CalendarScreen(
                    events = calendarEvents,
                    onDateSelected = viewModel::onDateSelected,
                    onEventAdd = { date, event -> viewModel.addCalendarEvent(date, event) },
                    onEventUpdate = { date, event -> viewModel.updateCalendarEvent(date, event) },
                    onEventDelete = { date -> viewModel.removeCalendarEvent(date) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedTab == 0) Icons.Filled.ImageAspectRatio else Icons.Outlined.CheckCircle,
                        contentDescription = "Tasks",tint = if (selectedTab == 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { Text("Tasks",
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal)
                },
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedTab == 1) Icons.Filled.WbSunny else Icons.Outlined.WbSunny,
                        contentDescription = "Weather",
                        tint = if (selectedTab == 1) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { Text("Weather",
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal)
                },
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedTab == 2) Icons.Filled.Event else Icons.Outlined.Event,
                        contentDescription = "Calendar",
                        tint = if (selectedTab == 2) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { Text("Calendar",
                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal)
                },
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 }
            )
        }
    }
}