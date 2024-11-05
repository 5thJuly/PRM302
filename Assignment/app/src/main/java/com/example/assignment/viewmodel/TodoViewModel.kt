package com.example.assignment.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _enhancedTasks = MutableStateFlow<List<Task>>(emptyList())
    val enhancedTasks: StateFlow<List<Task>> = _enhancedTasks.asStateFlow()

    private val _calendarEvents = MutableStateFlow<Map<String, String>>(emptyMap())
    val calendarEvents: StateFlow<Map<String, String>> = _calendarEvents.asStateFlow()

    private val _isEditing = MutableStateFlow<Int?>(null)
    val isEditing: StateFlow<Int?> = _isEditing.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private var nextId = 1

    init {
        // Khởi tạo lắng nghe sự thay đổi dữ liệu từ Firebase
        setupFirebaseListeners()
    }

    private fun setupFirebaseListeners() {
        auth.currentUser?.let { user ->
            // Lắng nghe thay đổi tasks
            database.child("users").child(user.uid).child("tasks")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tasksList = mutableListOf<Task>()
                        snapshot.children.forEach { taskSnapshot ->
                            taskSnapshot.getValue(Task::class.java)?.let { task ->
                                tasksList.add(task)
                            }
                        }
                        _enhancedTasks.value = tasksList
                        nextId = (tasksList.maxOfOrNull { it.id } ?: 0) + 1
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })

            // Lắng nghe thay đổi calendar events
            database.child("users").child(user.uid).child("calendarEvents")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val eventsMap = mutableMapOf<String, String>()
                        snapshot.children.forEach { eventSnapshot ->
                            val date = eventSnapshot.key
                            val event = eventSnapshot.getValue(String::class.java)
                            if (date != null && event != null) {
                                eventsMap[date] = event
                            }
                        }
                        _calendarEvents.value = eventsMap
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(title: String, description: String = "") {
        auth.currentUser?.let { user ->
            val newTask = Task(
                id = nextId++,
                title = title,
                description = description,
                createdAt = getCurrentDateTime()
            )

            // Thêm task vào Firebase
            database.child("users").child(user.uid)
                .child("tasks").child(newTask.id.toString())
                .setValue(newTask)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCalendarEvent(date: String, event: String) {
        auth.currentUser?.let { user ->
            // Thêm calendar event vào Firebase
            database.child("users").child(user.uid)
                .child("calendarEvents").child(date)
                .setValue(event)

            // Thêm task tương ứng
            addTask(
                title = event,
                description = "Calendar Event for $date"
            )
        }
    }

    fun updateCalendarEvent(date: String, newEvent: String) {
        auth.currentUser?.let { user ->
            val oldEvent = _calendarEvents.value[date]

            // Cập nhật calendar event trong Firebase
            database.child("users").child(user.uid)
                .child("calendarEvents").child(date)
                .setValue(newEvent)

            // Cập nhật task tương ứng
            if (oldEvent != null) {
                viewModelScope.launch {
                    _enhancedTasks.value.find { it.title == oldEvent }?.let { task ->
                        updateEnhancedTask(task.id, newEvent, "Calendar Event for $date (Updated)")
                    }
                }
            }
        }
    }

    fun removeCalendarEvent(date: String) {
        auth.currentUser?.let { user ->
            val eventToRemove = _calendarEvents.value[date]

            // Xóa calendar event khỏi Firebase
            database.child("users").child(user.uid)
                .child("calendarEvents").child(date)
                .removeValue()

            // Xóa task tương ứng
            if (eventToRemove != null) {
                viewModelScope.launch {
                    _enhancedTasks.value.find { it.title == eventToRemove }?.let { task ->
                        deleteEnhancedTask(task.id)
                    }
                }
            }

            if (_selectedDate.value == date) {
                clearSelectedDate()
            }
        }
    }

    fun updateEnhancedTask(taskId: Int, newTitle: String, newDescription: String) {
        auth.currentUser?.let { user ->
            val updatedTask = _enhancedTasks.value.find { it.id == taskId }?.copy(
                title = newTitle,
                description = newDescription
            )

            updatedTask?.let {
                database.child("users").child(user.uid)
                    .child("tasks").child(taskId.toString())
                    .setValue(it)
            }
        }
    }

    fun deleteEnhancedTask(taskId: Int) {
        auth.currentUser?.let { user ->
            database.child("users").child(user.uid)
                .child("tasks").child(taskId.toString())
                .removeValue()
        }
    }

    fun toggleEnhancedTask(taskId: Int) {
        auth.currentUser?.let { user ->
            val task = _enhancedTasks.value.find { it.id == taskId }
            task?.let {
                val updatedTask = it.copy(isCompleted = !it.isCompleted)
                database.child("users").child(user.uid)
                    .child("tasks").child(taskId.toString())
                    .setValue(updatedTask)
            }
        }
    }

    // Helper functions
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return current.format(formatter)
    }

    fun onDateSelected(dateString: String) {
        _selectedDate.value = dateString
    }

    fun clearSelectedDate() {
        _selectedDate.value = null
    }

    fun startEditing(taskId: Int) {
        _isEditing.value = taskId
    }

    fun stopEditing() {
        _isEditing.value = null
    }
}