package com.example.myfap_v3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF8E24AA)
    ) {
        val items = listOf(
            Triple(Icons.Filled.DateRange, "Calendar", 0),
            Triple(Icons.Filled.DateRange, "Schedule", 1),
            Triple(Icons.Filled.Add, "Add", 2),
            Triple(Icons.Filled.Notifications, "Notifications", 3),
            Triple(Icons.Filled.Person, "Profile", 4)
        )

        items.forEachIndexed { index, (icon, label, tabIndex) ->
            if (index == 2) {
                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFFAA00FF), Color(0xFF8E24AA))
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = label, tint = Color.White)
                        }
                    },
                    selected = selectedTab == tabIndex,
                    onClick = { onTabSelected(tabIndex) }
                )
            } else {
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) },
                    selected = selectedTab == tabIndex,
                    onClick = { onTabSelected(tabIndex) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF8E24AA),
                        selectedTextColor = Color(0xFF8E24AA),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}