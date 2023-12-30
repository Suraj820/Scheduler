package com.example.scheduler

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class CalendarEventHelper(private val context: Context) {

    // ... (previous code)

    // Function to get events with reminders and attendees for the current date

    private val eventProjection by lazy {
        arrayOf(
        CalendarContract.Events._ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.DURATION,
        CalendarContract.Events.ORGANIZER,
        CalendarContract.Events.STATUS,
    )
    }

    private val remindersProjection by lazy {
        arrayOf(
        CalendarContract.Reminders._ID,
        CalendarContract.Reminders.EVENT_ID,
        CalendarContract.Reminders.MINUTES,
    )
    }

    private val attendeesProjection by lazy {
        arrayOf(
        CalendarContract.Attendees.EVENT_ID,
        CalendarContract.Attendees.ATTENDEE_NAME,
        CalendarContract.Attendees.ATTENDEE_EMAIL
            )
    }

    fun getEventsWithRemindersAndAttendeesForCurrentDate(onData:(List<CalendarEventWithDetails>)->Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val currentDate = Calendar.getInstance()
            val startOfDay = currentDate.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }.timeInMillis
            val endOfDay = currentDate.apply { set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }.timeInMillis

            val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTEND} <= ?"
            val selectionArgs = arrayOf(startOfDay.toString(), endOfDay.toString())

            val eventsCursor = queryCalendarProvider(eventProjection, selection, selectionArgs, null,CalendarContract.Events.CONTENT_URI)
            val caldata = parseEventsWithDetails(eventsCursor)
            launch(Dispatchers.Main){
                onData(caldata)
            }



        }

        //return parseEventsWithDetails(eventsCursor)
    }


    fun generateColors():Pair<Color, Color>{

        val randomColor = generateRandomColor()
        val lightColor = lightenColor(randomColor)
        val darkenColor = darkenColor(randomColor)

        return Pair(Color("#${lightColor}".hashCode()), Color("#${darkenColor}".hashCode()))

    }

    fun generateRandomColor(): String {
        val random = Random.Default
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)

        // Creating the color code in hexadecimal format (RRGGBB)
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    fun lightenColor(color: String): String {
        val factor = 0.2 // Adjust this factor to control the lightness

        val red = Integer.parseInt(color.substring(1, 3), 16)
        val green = Integer.parseInt(color.substring(3, 5), 16)
        val blue = Integer.parseInt(color.substring(5, 7), 16)

        val newRed = ((255 - red) * factor + red).toInt()
        val newGreen = ((255 - green) * factor + green).toInt()
        val newBlue = ((255 - blue) * factor + blue).toInt()

        return String.format("#%02X%02X%02X", newRed, newGreen, newBlue)
    }

    fun darkenColor(color: String): String {
        val factor = 0.2 // Adjust this factor to control the darkness

        val red = Integer.parseInt(color.substring(1, 3), 16)
        val green = Integer.parseInt(color.substring(3, 5), 16)
        val blue = Integer.parseInt(color.substring(5, 7), 16)

        val newRed = (red * (1 - factor)).toInt()
        val newGreen = (green * (1 - factor)).toInt()
        val newBlue = (blue * (1 - factor)).toInt()

        return String.format("#%02X%02X%02X", newRed, newGreen, newBlue)
    }


    // Function to parse events with details from the cursor
    private fun parseEventsWithDetails(cursor: Cursor?): List<CalendarEventWithDetails> {
        val eventsWithDetailsList = mutableListOf<CalendarEventWithDetails>()
        cursor?.use {
            while (it.moveToNext()) {
                val eventId = it.getLong(it.getColumnIndex(CalendarContract.Events._ID))
                val title = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                val startTime = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                val endTime = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))
                val duration = it.getString(it.getColumnIndex(CalendarContract.Events.DURATION))
                val organizer = it.getString(it.getColumnIndex(CalendarContract.Events.ORGANIZER))
                val status = it.getInt(it.getColumnIndex(CalendarContract.Events.STATUS))

                // Query reminders and attendees for the current event
                val remindersCursor = queryRemindersForEvent(eventId)
                val attendeesCursor = queryAttendeesForEvent(eventId)

                val reminders = parseReminders(remindersCursor)
                val attendees = parseAttendees(attendeesCursor)
                
                val formattedStartTime = formattedDate(startTime)
                val formattedEndTime = formattedDate(endTime)
                val totalDuration =  endTime - startTime
                val timeDifferenceMinutes = TimeUnit.MILLISECONDS.toMinutes(totalDuration)
                Log.e("Suraj==>>", "parseEventsWithDetails: $timeDifferenceMinutes")

                val eventWithDetails = CalendarEventWithDetails(eventId, title, formattedStartTime, formattedEndTime, timeDifferenceMinutes.toString(), organizer, status, reminders, attendees,generateColors())
                eventsWithDetailsList.add(eventWithDetails)
            }
        }
        return eventsWithDetailsList
    }

    private fun formattedDate(time: Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("hh:mm a")
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        val formatted: String = formatter.format(date)
        return formatted
    }

    // Function to query reminders for a specific event
    private fun queryRemindersForEvent(eventId: Long): Cursor? {
        val selection = "${CalendarContract.Reminders.EVENT_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        return queryCalendarProvider(remindersProjection, selection, selectionArgs, null,CalendarContract.Reminders.CONTENT_URI)
    }

    // Function to query attendees for a specific event
    private fun queryAttendeesForEvent(eventId: Long): Cursor? {
        val selection = "${CalendarContract.Attendees.EVENT_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        return queryCalendarProvider(attendeesProjection, selection, selectionArgs, null,CalendarContract.Attendees.CONTENT_URI)
    }

    private fun queryCalendarProvider(
        projection: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        uri : Uri
    ): Cursor? {
        val contentResolver: ContentResolver = context.contentResolver
        val uri = uri
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    private fun parseReminders(cursor: Cursor?): List<CalendarReminder> {
        val remindersList = mutableListOf<CalendarReminder>()
        cursor?.use {
            while (it.moveToNext()) {
                val reminderId = it.getLong(it.getColumnIndex(CalendarContract.Reminders._ID))
                val eventId = it.getLong(it.getColumnIndex(CalendarContract.Reminders.EVENT_ID))
                val minutes = it.getInt(it.getColumnIndex(CalendarContract.Reminders.MINUTES))

                val reminder = CalendarReminder(reminderId, eventId, minutes)
                remindersList.add(reminder)
            }
        }
        return remindersList
    }

    private fun parseAttendees(cursor: Cursor?): List<CalendarAttendee> {
        val attendeesList = mutableListOf<CalendarAttendee>()
        cursor?.use {
            while (it.moveToNext()) {
                val eventId = it.getLong(it.getColumnIndex(CalendarContract.Attendees.EVENT_ID))
                val attendeeName = it.getString(it.getColumnIndex(CalendarContract.Attendees.ATTENDEE_NAME))
                val attendeeEmail = it.getString(it.getColumnIndex(CalendarContract.Attendees.ATTENDEE_EMAIL))

                val attendee = CalendarAttendee(eventId, attendeeName, attendeeEmail)
                attendeesList.add(attendee)
            }
        }
        return attendeesList
    }



    // ... (previous code)
}

data class CalendarEventWithDetails(
    val eventId: Long,
    val title: String,
    val startTime: String,
    val endTime: String,
    val duration: String?,
    val organizer: String?,
    val status: Int,
    val reminders: List<CalendarReminder>,
    val attendees: List<CalendarAttendee>,
    val colorPair: Pair<Color,Color>
)

data class CalendarReminder(
    val reminderId: Long,
    val eventId: Long,
    val minutes: Int
)

data class CalendarAttendee(
    val eventId: Long,
    val attendeeName: String?,
    val attendeeEmail: String?
)
