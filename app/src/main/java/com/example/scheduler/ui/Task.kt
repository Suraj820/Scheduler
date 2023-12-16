package com.example.scheduler.ui

import androidx.compose.ui.graphics.Color
import java.util.Date


data class Task(
    val title: String,
    val members:List<String>,
    val startTime:String,
    val endTime:String,
    val backgroundColor:Color,
    val textColor:Color,
)

data class CalendarEvent(
    val title: String,
    val description: String,
    val startTime: Date,
    val endTime: Date,
    val organizer: String,
    val duration: String,
)
