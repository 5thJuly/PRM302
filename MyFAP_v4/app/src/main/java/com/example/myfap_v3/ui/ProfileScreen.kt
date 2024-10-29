package com.example.myfap_v3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.R
import com.example.myfap_v3.ui.theme.DarkColors
import com.example.myfap_v3.ui.theme.LightColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavHostController) {

    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = { ProfileTopAppBar() },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = { BottomNavBar(selectedTab = 4, navController = navController) }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                item { ProfileHeader() }
                item { ProfileStats() }
                item { ProfileActions(navController) }
                item { ProfileSettings(navController) }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar() {
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
                text = "Profile",
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
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Dao Hai Nam",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "SE173582",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ProfileStats() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Subjects", "8")
            StatItem("Credits", "24")
            StatItem("GPA", "8.8")
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ProfileActions(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(Icons.Filled.DateRange, "Schedule") {
            navController.navigate("schedule")
        }
        ActionButton(Icons.Filled.Score, "Mark Report") {
            navController.navigate("markReport")
        }
        ActionButton(Icons.Filled.Call, "Contact") {
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        FilledTonalButton(
            onClick = onClick,
            modifier = Modifier.size(78.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ProfileSettings(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SettingItem(Icons.Filled.Notifications, "Notifications"){
            navController.navigate(Screen.Notification.route)
        }
        SettingItem(Icons.Filled.Lock, "Privacy")
        SettingItem(Icons.Filled.Language, "Language")
        SettingItem(Icons.Filled.HelpCenter, "Help & Support")
        SettingItem(Icons.Filled.ExitToApp, "Logout")
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfilePage() {
    val navController = rememberNavController()
    MaterialTheme {
        ProfilePage(navController)
    }
}