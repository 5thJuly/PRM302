package com.example.assignment.data

import kotlinx.coroutines.flow.Flow
import com.example.assignment.data.Task
import com.example.assignment.data.FirebaseDataSource

class Repository(private val firebaseDataSource: FirebaseDataSource) {

    // Calendar operations
    fun saveCalendarEvent(date: String, event: String) {
        firebaseDataSource.saveCalendarEvent(date, event)
    }

    fun updateCalendarEvent(date: String, event: String) {
        firebaseDataSource.updateCalendarEvent(date, event)
    }

    fun deleteCalendarEvent(date: String) {
        firebaseDataSource.deleteCalendarEvent(date)
    }

    fun observeCalendarEvents(): Flow<Map<String, String>> {
        return firebaseDataSource.observeCalendarEvents()
    }

    // Task operations
    fun saveTask(task: Task) {
        firebaseDataSource.saveTask(task)
    }

    fun updateTask(taskId: Int, title: String, description: String) {
        firebaseDataSource.updateTask(taskId, title, description)
    }

    fun toggleTaskComplete(taskId: Int, isCompleted: Boolean) {
        firebaseDataSource.toggleTaskComplete(taskId, isCompleted)
    }

    fun deleteTask(taskId: Int) {
        firebaseDataSource.deleteTask(taskId)
    }

    fun observeTasks(): Flow<List<Task>> {
        return firebaseDataSource.observeTasks()
    }
}