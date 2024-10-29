package com.example.myfap_v3.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.ui.theme.DarkColors
import com.example.myfap_v3.ui.theme.LightColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage(navController: NavHostController) {
    val notifications = remember { generateNotifications() }
    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = { NotificationTopAppBar() },
//
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Today",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                items(notifications.filter { it.isToday }) { notification ->
                    NotificationItem(notification = notification)
                }
                item {
                    Text(
                        "Earlier",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(notifications.filter { !it.isToday }) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopAppBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.size(48.dp))
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Filled.NotificationsActive,
                    contentDescription = "Mark all as read",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationItem(notification: NotificationData) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            NotificationIcon(notification.icon, notification.color)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (isExpanded) {
                        TextButton(onClick = { /* Handle action */ }) {
                            Text("View Details")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationIcon(icon: ImageVector, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

data class NotificationData(
    val title: String,
    val description: String,
    val time: String,
    val color: Color,
    val icon: ImageVector,
    val isToday: Boolean
)

fun generateNotifications(): List<NotificationData> {
    return listOf(
        NotificationData(
            "New Assignment",
            "Mobile Programming: UI Design Task. Please submit your work by next Friday. Make sure to follow the guidelines provided in the course materials.",
            "2 hours ago",
            Color(0xFF6750A4),
            Icons.Filled.Assignment,
            isToday = true
        ),
        NotificationData(
            "Upcoming Exam",
            "Web Development: Final Exam Next Week. The exam will cover all topics discussed in class. Review your notes and practice exercises.",
            "1 day ago",
            Color(0xFF6750A4),
            Icons.Filled.Event,
            isToday = false
        ),
        NotificationData(
            "Grade Posted",
            "Data Structures: Midterm Results Available. You can view your grade and feedback in the student portal.",
            "2 days ago",
            Color(0xFF6750A4),
            Icons.Filled.Grade,
            isToday = false
        ),
        NotificationData(
            "Course Update",
            "Software Engineering: New Learning Materials. Additional resources have been added to the course repository. Make sure to review them before the next class.",
            "3 days ago",
            Color(0xFF6750A4),
            Icons.Filled.Book,
            isToday = false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationPage() {
    val navController = rememberNavController()
    MaterialTheme {
        NotificationPage(navController)
    }
}