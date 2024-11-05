package com.example.assignment.data

import android.os.Build
import androidx.annotation.RequiresApi

data class Task @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int,
    var title: String,
    val description: String,
    var isCompleted: Boolean = false,
    val createdAt: String
)
{
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(0, "", "", false, "")
}

