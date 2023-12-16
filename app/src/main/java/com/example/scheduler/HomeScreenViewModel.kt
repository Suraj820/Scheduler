package com.example.scheduler

import android.content.Context
import android.provider.CalendarContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val context: Context) :ViewModel() {
    private val calendarEventHelper = CalendarEventHelper(context)

    fun getGetCalendarEvents(onData:(List<CalendarEventWithDetails>)->Unit){
        /*viewModelScope.launch(Dispatchers.IO) {
            val calendarData =  calendarEventHelper.getEventsWithRemindersAndAttendeesForCurrentDate()
            launch(Dispatchers.Main) {
                onData(calendarData)
            }

        }*/
    }
}