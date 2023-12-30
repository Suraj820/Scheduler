package com.example.scheduler

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun HomeScreePreview() {
        SchedulerTheme {
            HomeScree(calendarEventHelper)
        }
    }


}
















