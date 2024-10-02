package com.example.myfap_v3.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage() {
    var selectedTab by remember { mutableStateOf(3) }

    Scaffold(
        topBar = { NotificationTopAppBar() },
        containerColor = Color(0xFFF8F8F8),
        bottomBar = { BottomNavBar(selectedTab) { selectedTab = it } }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(generateNotifications()) { notification ->
                NotificationItem(notification)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopAppBar() {
    TopAppBar(
        title = {
            Text(
                "Notifications",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Implement mark all as read */ }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Mark all as read")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun NotificationItem(notification: NotificationData) {
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(notification.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(notification.icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF4081)
                )
            }
        }
    }
}

data class NotificationData(
    val title: String,
    val description: String,
    val time: String,
    val color: Color,
    val icon: ImageVector
)

fun generateNotifications(): List<NotificationData> {
    return listOf(
        NotificationData(
            "New Assignment",
            "Mobile Programming: UI Design Task",
            "2 hours ago",
            Color(0xFFFF4081),
            Icons.Filled.Notifications
        ),
        NotificationData(
            "Upcoming Exam",
            "Web Development: Final Exam Next Week",
            "1 day ago",
            Color(0xFF2196F3),
            Icons.Filled.Notifications
        ),
        NotificationData(
            "Grade Posted",
            "Data Structures: Midterm Results Available",
            "2 days ago",
            Color(0xFF4CAF50),
            Icons.Filled.Notifications
        ),
        NotificationData(
            "Course Update",
            "Software Engineering: New Learning Materials",
            "3 days ago",
            Color(0xFFFFC107),
            Icons.Filled.Notifications
        )
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewNotificationPage() {
    MaterialTheme {
        NotificationPage()
    }
}