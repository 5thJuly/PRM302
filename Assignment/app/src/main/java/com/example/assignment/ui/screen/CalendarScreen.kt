package com.example.assignment.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

private val Primary = Color(0xFF006C51)
private val OnPrimary = Color(0xFFFFFFFF)
private val PrimaryContainer = Color(0xFF7FF8D4)
private val OnPrimaryContainer = Color(0xFF002117)
private val Secondary = Color(0xFF4B635B)
private val OnSecondary = Color(0xFFFFFFFF)
private val Surface = Color(0xFFFBFDF9)
private val OnSurface = Color(0xFF191C1B)
private val SurfaceVariant = Color(0xFFDBE5DE)
private val OnSurfaceVariant = Color(0xFF3F4945)
private val Tertiary = Color(0xFF006C51).copy(alpha = 0.1f)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    events: Map<String, String>,
    onDateSelected: (String) -> Unit,
    onEventAdd: (String, String) -> Unit,
    onEventUpdate: (String, String) -> Unit,
    onEventDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        onPrimaryContainer = OnPrimaryContainer,
        secondary = Secondary,
        onSecondary = OnSecondary,
        surface = Surface,
        onSurface = OnSurface,
        surfaceVariant = SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant
    )

    var showEventDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var eventText by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var showViewEventDialog by remember { mutableStateOf(false) }

    val fabScale by animateFloatAsState(
        targetValue = if (selectedDate != null) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fab_scale"
    )

    MaterialTheme(colorScheme = customColorScheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            var currentMonth by remember { mutableStateOf(YearMonth.now()) }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(colorScheme.surface)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    CalendarHeader(
                        currentMonth = currentMonth,
                        onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                        onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        WeekDayHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                        CalendarDays(
                            currentMonth = currentMonth,
                            events = events,
                            selectedDate = selectedDate,
                            onDateClick = { date ->
                                selectedDate = if (selectedDate == date) null else date
                                val dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                onDateSelected(dateStr)
                                if (events.containsKey(dateStr)) {
                                    eventText = events[dateStr] ?: ""
                                    isEditMode = true
                                    showViewEventDialog = true
                                } else {
                                    eventText = ""
                                    isEditMode = false
                                }
                                onDateSelected(dateStr)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                EventsSummarySection(events = events)

            }

            FloatingActionButton(
                onClick = {
                    if (selectedDate != null) {
                        val dateStr = selectedDate!!.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        eventText = events[dateStr] ?: ""
                        isEditMode = events.containsKey(dateStr)
                        showEventDialog = true
                    } else {
                        showSnackbar = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .scale(fabScale),
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Event",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (showSnackbar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 96.dp)
                ) {
                    Snackbar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        action = {
                            TextButton(
                                onClick = { showSnackbar = false },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colorScheme.primary
                                )
                            ) {
                                Text("Dismiss")
                            }
                        },
                        dismissAction = {
                            IconButton(onClick = { showSnackbar = false }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = colorScheme.onPrimary
                                )
                            }
                        }
                    ) {
                        Text("Please select a date first")
                    }
                }

                LaunchedEffect(showSnackbar) {
                    delay(3000)
                    showSnackbar = false
                }
            }
        }
        if (showViewEventDialog && selectedDate != null) {
            ViewEventDialog(
                date = selectedDate!!,
                eventText = eventText,
                onDismiss = { showViewEventDialog = false },
                onEdit = {
                    showViewEventDialog = false
                    showEventDialog = true
                },
                onDelete = {
                    val dateStr = selectedDate!!.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    onEventDelete(dateStr)
                    showViewEventDialog = false
                }
            )
        }

        AnimatedVisibility(
            visible = showEventDialog && selectedDate != null,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            EventDialog(
                date = selectedDate!!,
                initialEventText = eventText,
                isEditMode = isEditMode,
                onDismiss = { showEventDialog = false },
                onSave = { newEventText, isTask ->
                    val dateStr = selectedDate!!.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    if (isEditMode) {
                        onEventUpdate(dateStr, newEventText)
                    } else {
                        onEventAdd(dateStr, newEventText)
                    }
                    showEventDialog = false
                },
                onDelete = {
                    val dateStr = selectedDate!!.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    onEventDelete(dateStr)
                    showEventDialog = false
                }
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDialog(
    date: LocalDate,
    initialEventText: String,
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var eventText by remember { mutableStateOf(initialEventText) }
    var isTask by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = if (isEditMode) "Edit Event" else "Add New Event",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Event Type",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isTask) "Task" else "Event",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.primary
                        )
                        Switch(
                            checked = isTask,
                            onCheckedChange = { isTask = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorScheme.primary,
                                checkedTrackColor = colorScheme.primaryContainer,
                                uncheckedThumbColor = colorScheme.secondary,
                                uncheckedTrackColor = colorScheme.secondaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = eventText,
                    onValueChange = { eventText = it },
                    label = { Text(if (isTask) "Task Description" else "Event Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedLabelColor = colorScheme.primary,
                        cursorColor = colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.onSurfaceVariant
                        )
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Cancel",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    if (isEditMode) {
                        OutlinedButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(1.dp, Color.Red)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                            }
                        }
                    }

                    Button(
                        onClick = { onSave(eventText, isTask) },
                        enabled = eventText.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Icon(
                                if (isEditMode) Icons.Default.Save else Icons.Default.Add,
                                contentDescription = if (isEditMode) "Save" else "Add",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (isEditMode) "Save" else "Add",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier
                .clip(CircleShape)
                .background(colorScheme.onPrimary.copy(alpha = 0.1f))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                tint = colorScheme.onPrimary
            )
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onPrimary,
            modifier = Modifier.animateContentSize()
        )

        IconButton(
            onClick = onNextMonth,
            modifier = Modifier
                .clip(CircleShape)
                .background(colorScheme.onPrimary.copy(alpha = 0.1f))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                tint = colorScheme.onPrimary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeekDayHeader() {
    val daysOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek.let { firstDay ->
        (0..6).map { firstDay.plus(it.toLong()) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = colorScheme.primary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarDays(
    currentMonth: YearMonth,
    events: Map<String, String>,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDay = currentMonth.atDay(1)
    val lastDay = currentMonth.atEndOfMonth()

    // Tìm ngày đầu tiên của tuần chứa ngày 1 của tháng
    val firstDayOfCalendar = firstDay.with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).firstDayOfWeek))

    // Tìm ngày cuối cùng của tuần chứa ngày cuối của tháng
    val lastDayOfCalendar = lastDay.with(TemporalAdjusters.nextOrSame(WeekFields.of(Locale.getDefault()).firstDayOfWeek.plus(6)))

    val days = mutableListOf<LocalDate>()
    var current = firstDayOfCalendar

    while (!current.isAfter(lastDayOfCalendar)) {
        days.add(current)
        current = current.plusDays(1)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(days) { date ->
            CalendarDay(
                date = date,
                isSelected = date == selectedDate,
                isInCurrentMonth = date.month == currentMonth.month,
                hasEvent = events.containsKey(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))),
                onClick = { onDateClick(date) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    isInCurrentMonth: Boolean,
    hasEvent: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> colorScheme.primary
            hasEvent -> Tertiary
            else -> Color.Transparent
        },
        label = "background_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected || hasEvent) FontWeight.Bold else FontWeight.Normal
                ),
                color = when {
                    isSelected -> colorScheme.onPrimary
                    !isInCurrentMonth -> colorScheme.onSurface.copy(alpha = 0.38f)
                    hasEvent -> colorScheme.primary
                    else -> colorScheme.onSurface
                }
            )
            if (hasEvent && !isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewEventDialog(
    date: LocalDate,
    eventText: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Event Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = eventText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.onSurfaceVariant
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Close",
                                modifier = Modifier.size(20.dp)
                            )

                        }
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                        }
                    }

                    Button(
                        onClick = onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.EditOff,
                                contentDescription = "Edit",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsSummarySection(
    events: Map<String, String>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentDate = remember { LocalDate.now() }
    val tabs = listOf("Upcoming", "Monthly Stats", "Quick Add")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tab Section
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f),
            contentColor = colorScheme.primary,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(3.dp)
                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .padding(horizontal = 16.dp)
                        .background(colorScheme.primary)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = colorScheme.primary,
                    unselectedContentColor = colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        when (selectedTab) {
            0 -> UpcomingEvents(events, currentDate)
            1 -> MonthlyStats(events, currentDate)
            2 -> QuickAddSection()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun UpcomingEvents(
    events: Map<String, String>,
    currentDate: LocalDate
) {
    val upcomingEvents = remember(events, currentDate) {
        events.entries
            .filter { entry ->
                val eventDate = LocalDate.parse(
                    entry.key,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )
                !eventDate.isBefore(currentDate)
            }
            .sortedBy { it.key }
            .take(3)
    }

    if (upcomingEvents.isEmpty()) {
        EmptyStateMessage("No upcoming events")
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            upcomingEvents.forEach { event ->
                EventCard(event)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthlyStats(
    events: Map<String, String>,
    currentDate: LocalDate
) {
    val monthlyStats = remember(events, currentDate) {
        val eventsThisMonth = events.count { entry ->
            val eventDate = LocalDate.parse(
                entry.key,
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            )
            eventDate.month == currentDate.month
        }
        val busyDay = events.entries
            .groupBy { entry ->
                LocalDate.parse(
                    entry.key,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                ).dayOfWeek
            }
            .maxByOrNull { it.value.size }
            ?.key
            ?.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())

        Triple(eventsThisMonth, busyDay, events.size)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem(
                icon = Icons.Default.DateRange,
                title = "Events this month",
                value = "${monthlyStats.first}"
            )
            if (monthlyStats.second != null) {
                StatItem(
                    icon = Icons.Default.Refresh,
                    title = "Busiest day",
                    value = monthlyStats.second ?: ""
                )
            }
            StatItem(
                icon = Icons.Default.Star,
                title = "Total events",
                value = "${monthlyStats.third}"
            )
        }
    }
}

@Composable
private fun QuickAddSection() {
    var quickNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = quickNote,
            onValueChange = { quickNote = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type a quick note...") },
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                focusedLabelColor = colorScheme.primary,
                cursorColor = colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = { /* TODO: Implement quick add logic */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = quickNote.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Quick Add",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add to Today")
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun EventCard(event: Map.Entry<String, String>) {
    val eventDate = LocalDate.parse(
        event.key,
        DateTimeFormatter.ofPattern("dd-MM-yyyy")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Tertiary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = event.value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = eventDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM")),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = { /* TODO: Navigate to event */ },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View event",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}