package com.example.assignment.data

import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.example.assignment.data.Task
import com.google.firebase.auth.FirebaseAuth

class FirebaseDataSource {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val currentUserId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    // Calendar Events
    fun saveCalendarEvent(date: String, event: String) {
        database.child("users")
            .child(currentUserId)
            .child("calendar")
            .child(date)
            .setValue(event)
    }

    fun updateCalendarEvent(date: String, event: String) {
        database.child("users")
            .child(currentUserId)
            .child("calendar")
            .child(date)
            .setValue(event)
    }

    fun deleteCalendarEvent(date: String) {
        database.child("users")
            .child(currentUserId)
            .child("calendar")
            .child(date)
            .removeValue()
    }

    fun observeCalendarEvents(): Flow<Map<String, String>> = callbackFlow {
        val eventsRef = database.child("users")
            .child(currentUserId)
            .child("calendar")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val events = mutableMapOf<String, String>()
                snapshot.children.forEach { dateSnapshot ->
                    val date = dateSnapshot.key
                    val event = dateSnapshot.getValue(String::class.java)
                    if (date != null && event != null) {
                        events[date] = event
                    }
                }
                trySend(events)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        eventsRef.addValueEventListener(listener)
        awaitClose { eventsRef.removeEventListener(listener) }
    }

    // Tasks
    fun saveTask(task: Task) {
        database.child("users")
            .child(currentUserId)
            .child("tasks")
            .child(task.id.toString())
            .setValue(task)
    }

    fun updateTask(taskId: Int, title: String, description: String) {
        database.child("users")
            .child(currentUserId)
            .child("tasks")
            .child(taskId.toString())
            .updateChildren(mapOf(
                "title" to title,
                "description" to description
            ))
    }

    fun toggleTaskComplete(taskId: Int, isCompleted: Boolean) {
        database.child("users")
            .child(currentUserId)
            .child("tasks")
            .child(taskId.toString())
            .child("completed")
            .setValue(isCompleted)
    }

    fun deleteTask(taskId: Int) {
        database.child("users")
            .child(currentUserId)
            .child("tasks")
            .child(taskId.toString())
            .removeValue()
    }

    fun observeTasks(): Flow<List<Task>> = callbackFlow {
        val tasksRef = database.child("users")
            .child(currentUserId)
            .child("tasks")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = mutableListOf<Task>()
                snapshot.children.forEach { taskSnapshot ->
                    taskSnapshot.getValue(Task::class.java)?.let {
                        tasks.add(it)
                    }
                }
                trySend(tasks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }
}