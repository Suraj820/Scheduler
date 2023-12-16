package com.example.scheduler

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.scheduler.ui.theme.PaleGreen
import com.example.scheduler.ui.theme.SchedulerTheme


class MainActivity : ComponentActivity() {

    private val calendarEventHelper = CalendarEventHelper(this)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PaleGreen
                ) {
                   // val viewModel: HomeScreenViewModel by viewModels()
                    HomeScree(calendarEventHelper)
                }
            }
        }
    }
}















