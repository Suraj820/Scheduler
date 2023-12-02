package com.example.scheduler.ui

import androidx.compose.ui.graphics.Color


data class Task(
    val title: String,
    val members:List<String>,
    val startTime:String,
    val endTime:String,
    val backgroundColor:Color,
    val textColor:Color,
)
