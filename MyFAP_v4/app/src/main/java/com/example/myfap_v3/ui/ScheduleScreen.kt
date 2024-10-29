package com.example.myfap_v3.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Score
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.ui.theme.DarkColors
import com.example.myfap_v3.ui.theme.LightColors
import java.util.Calendar

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePage(scheduleViewModel: ScheduleViewModel, navController: NavHostController) {
    val examScheduleItems by scheduleViewModel.examScheduleItems.observeAsState(emptyMap())
    val examResults by scheduleViewModel.examResults.observeAsState(emptyMap())
    val selectedSemester by scheduleViewModel.selectedSemester.observeAsState("Fall24")
    val semesterCourses by scheduleViewModel.semesterCourses.observeAsState(emptyMap())
    val selectedCourse by scheduleViewModel.selectedCourse.observeAsState()
    val semesters = listOf("Fall24", "Summer24", "Spring24")

    var showCourseDetails by remember { mutableStateOf(false) }

    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = { ScheduleTopAppBar() },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomNavBar(selectedTab = 1, navController = navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .alpha(if (showCourseDetails) 0.3f else 1f)
                ) {
                    EnhancedSemesterSelector(
                        semesters = semesters,
                        selectedSemester = selectedSemester,
                        onSemesterSelected = { scheduleViewModel.setSelectedSemester(it) }
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        item {
                            CourseList(
                                courses = semesterCourses[selectedSemester] ?: emptyList(),
                                onCourseSelected = {
                                    scheduleViewModel.selectCourse(it)
                                    showCourseDetails = true
                                }
                            )
                        }

                        item {
                            ExamSection(
                                title = "Exam Schedule",
                                items = examScheduleItems[selectedSemester] ?: emptyList(),
                                icon = Icons.Filled.Event
                            )
                        }

//                        item {
//                            ExamSection(
//                                title = "Exam Results",
//                                items = examResults[selectedSemester] ?: emptyList(),
//                                icon = Icons.Filled.Score
//                            )
//                        }
                    }
                }

                if (showCourseDetails && selectedCourse != null) {
                    Dialog(
                        onDismissRequest = { showCourseDetails = false }
                    ) {
                        CourseDetailsCard(
                            course = selectedCourse!!,
                            onDismiss = { showCourseDetails = false }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CourseList(courses: List<CourseData>, onCourseSelected: (CourseData) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Courses",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        courses.forEach { course ->
            CourseItem(course, onCourseSelected)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItem(course: CourseData, onCourseSelected: (CourseData) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCourseSelected(course) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Lecturer: ${course.lecturer}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = "Room: ${course.room}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsCard(course: CourseData, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    course.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CourseDetailItem("Lecturer", course.lecturer, Icons.Filled.Person)
            CourseDetailItem("Room", course.room, Icons.Filled.Room)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Schedule",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            course.schedule.forEach { scheduleItem ->
                CourseDetailItem(
                    getDayAbbreviation(scheduleItem.day),
                    "${scheduleItem.startTime} - ${scheduleItem.endTime} (${scheduleItem.type})",
                    Icons.Filled.Schedule
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Grade Components",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            course.gradeComponents.forEach { component ->
                CourseDetailItem(
                    component.name,
                    "${component.score} (${component.weight * 100}%)",
                    Icons.Filled.Grade
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Exam Information",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            CourseDetailItem("Exam Date", course.examDate, Icons.Filled.Event)
            CourseDetailItem("Score", course.examScore.toString(), Icons.Filled.Check)
        }
    }
}

fun getDayAbbreviation(day: Int): String {
    return when (day) {
        2 -> "Mon"
        3 -> "Tue"
        4 -> "Wed"
        5 -> "Thu"
        6 -> "Fri"
        7 -> "Sat"
        8 -> "Sun"
        else -> "Invalid day"
    }
}

@Composable
fun CourseDetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EnhancedSemesterSelector(
    semesters: List<String>,
    selectedSemester: String,
    onSemesterSelected: (String) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            semesters.forEach { semester ->
                val isSelected = semester == selectedSemester
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 300)
                )
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    animationSpec = tween(durationMillis = 300)
                )
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1f,
                    animationSpec = tween(durationMillis = 300)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(horizontal = 4.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(20.dp))
                        .background(backgroundColor)
                        .clickable { onSemesterSelected(semester) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = semester,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = textColor
                    )
                }
            }
        }
    }
}


@Composable
fun <T> ExamSection(title: String, items: List<T>, icon: ImageVector) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items.forEach { item ->
                when (item) {
                    is ExamScheduleItemData -> ExamScheduleItemCard(item)
                    is ExamResultData -> ExamResultItemCard(item)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScheduleItemCard(item: ExamScheduleItemData) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${item.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = "Time: ${item.startTime} - ${item.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = "Room: ${item.room}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Icon(
                Icons.Filled.Event,
                contentDescription = "Exam Date",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamResultItemCard(item: ExamResultData) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Exam Date: ${item.examDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "Score: ${item.score}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Icon(
                Icons.Filled.Check,
                contentDescription = "Exam Score",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopAppBar() {
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
            Box(modifier = Modifier.size(40.dp))

            Text(
                text = "Schedule",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

data class ExamScheduleItemData(
    val subject: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val room: String
)

data class ExamResultData(
    val subject: String,
    val examDate: String,
    val score: String
)

data class ScheduleItemData(
    val subject: String,
    val lecturer: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val type: String,
    val day: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewSchedulePage() {
    val navController = rememberNavController()
    MaterialTheme {
        SchedulePage(ScheduleViewModel(), navController)
    }
}
