package com.example.myfap_v3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.ui.theme.DarkColors
import com.example.myfap_v3.ui.theme.LightColors


object CustomColors {
    val Background = Color(0xFFF0F4F8)
    val CardBackground = Color(0xFFFFFFFF)
    val PrimaryText = Color(0xFF1A237E)
    val SecondaryText = Color(0xFF5C6BC0)
    val AccentColor = Color(0xFF00BCD4)
    val HighlightColor = Color(0xFFFF4081)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkReportScreen(
    viewModel: ScheduleViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val semesterGrades by viewModel.semesterGrades.observeAsState(emptyList())
    val overallGPA by viewModel.overallGPA.observeAsState(0.0)
    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = { MarkReportTopAppBar(onBackClick) },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = { BottomNavBar(selectedTab = 3, navController = rememberNavController()) }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item { MarkReportHeader() }
                items(semesterGrades) { grade ->
                    SemesterGradeItem(grade)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OverallGPAItem(overallGPA)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkReportTopAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                "Mark Report",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        actions = {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = Modifier.shadow(4.dp)
    )
}

@Composable
fun MarkReportHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.AccountBalance,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Academic Performance",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Semester grades and overall GPA",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SemesterGradeItem(grade: SemesterGrade) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = grade.semester,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                GPABadge(gpa = grade.gpa)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total Credits: ${grade.credits}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GPABadge(gpa: Double) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = "GPA: ${String.format("%.2f", gpa)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun OverallGPAItem(overallGPA: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Overall GPA",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your academic performance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(40.dp),
                color = MaterialTheme.colorScheme.tertiary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = String.format("%.2f", overallGPA),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
