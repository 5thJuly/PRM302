package com.example.myfap_v3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Schedule : Screen("schedule")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
    object MarkReport: Screen("markReport")
}

@Composable
fun AppNavigation(scheduleViewModel: ScheduleViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(scheduleViewModel = scheduleViewModel, navController = navController)
        }
        composable(Screen.Schedule.route) {
            SchedulePage(scheduleViewModel = scheduleViewModel, navController = navController)
        }
        composable(Screen.Notification.route) {
            NotificationPage(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfilePage(navController = navController)
        }
        composable(Screen.MarkReport.route) {
            MarkReportScreen(viewModel = scheduleViewModel, onBackClick = { navController.navigateUp() })
        }
    }
}

@Composable
fun BottomNavBar(selectedTab: Int, navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.large),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large
    ) {
        NavigationBar(
            modifier = Modifier
                .height(80.dp)
                .clip(MaterialTheme.shapes.large),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            val items = listOf(
                Triple(Icons.Filled.CalendarMonth, "Home", Screen.Main.route),
                Triple(Icons.Filled.DateRange, "Schedule", Screen.Schedule.route),
                Triple(Icons.Filled.Add, "Add", ""),
                Triple(Icons.Filled.Notifications, "M'Report", Screen.MarkReport.route),
                Triple(Icons.Filled.Person, "Profile", Screen.Profile.route)
            )

            items.forEachIndexed { index, (icon, label, route) ->
                if (index == 2) {
                    NavigationBarItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFFF79A3), Color(0xFFFF4081))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = label,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        selected = selectedTab == index,
                        onClick = {},
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                } else {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                icon,
                                contentDescription = label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(label) },
                        selected = selectedTab == index,
                        onClick = {
                            if (route.isNotEmpty()) {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
