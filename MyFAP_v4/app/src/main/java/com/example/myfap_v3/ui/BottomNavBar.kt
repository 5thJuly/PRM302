package com.example.myfap_v3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color(0xFFFF4081),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.White
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
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = label, tint = Color(0xFFFF4081))
                            }
                        },
                        selected = selectedTab == tabIndex,
                        onClick = { onTabSelected(tabIndex) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            indicatorColor = Color(0xFFFF4081)
                        )
                    )
                } else {
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = selectedTab == tabIndex,
                        onClick = { onTabSelected(tabIndex) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                            indicatorColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}