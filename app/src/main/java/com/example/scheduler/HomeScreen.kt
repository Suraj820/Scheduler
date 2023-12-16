package com.example.scheduler

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.scheduler.ui.Task
import com.example.scheduler.ui.theme.PaleGreen
import com.example.scheduler.ui.theme.PaleGreenDark
import com.example.scheduler.ui.theme.SchedulerTheme
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScree(calendarEventHelper: CalendarEventHelper) {
    Column {
        Headers()
        Spacer(modifier = Modifier.width(5.dp))
        showWeekDay(weekDay = getCurrentWeekDay())
        Spacer(modifier = Modifier.width(5.dp))
        ShowDateAndTime(getCurrentTime(), getCurrentTimeZone()!!, getCurrentDate())
        Spacer(modifier = Modifier.width(5.dp))
        ShowTodaysTask(calendarEventHelper)



    }
}


@Composable
fun Headers(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Today", color = Color.White)

        }
        Spacer(modifier = Modifier.width(5.dp))
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = PaleGreen),
            border = BorderStroke(1.dp, color = Color.Gray)

        ) {
            Text(text = "Calendar", color = Color.Black)

        }
        Spacer(Modifier.weight(1f))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(41.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PaleGreenDark),
            border = BorderStroke(1.dp, color = Color.Gray),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Schedule",
                tint = Color.Black
            )
        }

    }

}

@Composable
fun showWeekDay(weekDay:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = weekDay,
            color = Color.Black,
            fontSize = 12.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(start = 10.dp)

        )

    }

}
fun getCurrentWeekDay():String{

    val calendar = Calendar.getInstance()
    val dayLongName: String = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
    return dayLongName
}


@Composable
fun ShowDateAndTime(currentTime:String,currentTimeZone: String,currentDate:String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ){
            Column {
                var date = currentDate.split(" ")
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentWidth(),
                    text = date[0],
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000)
                    ),
                )
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentWidth(),
                    text = date[1].toString().uppercase(),
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000)
                    )
                )
            }

        }
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .height(100.dp)
                .width(1.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {

            Column {

                AndroidView(factory = { context ->
                    TextClock(context).apply {
                        format12Hour?.let { this.format12Hour = "hh:mm a" }
                        timeZone?.let { this.timeZone = it }
                        textSize.let { this.textSize= 18f }
                        this.setTextColor(ContextCompat.getColor(context,R.color.black))
                    }
                })
                /*Text(
                    text = currentTime,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                    ),

                )*/
                Text(
                    text = currentTimeZone,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                    ),
                )
            }

        }



    }

}


@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return currentDateTime.format(formatter)
}

fun getCurrentTimeZone(): String? {
    val defaultTimeZone = TimeZone.getDefault()
    return defaultTimeZone.id
}
fun getCurrentDate(): String {
    val currentDate = Date()

    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(currentDate)
}

@Composable
fun ShowTodaysTask(calendarEventHelper: CalendarEventHelper) {

    Column( modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
        .fillMaxHeight()
        .background(
            color = Color.White,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text on the left
            Text(
                text = "Today’s tasks",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .wrapContentHeight()
                ,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000)
                )
            )

            // Button on the right
            Button(
                onClick = { /* Handle button click */ },
                colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFFF4F4F4))
            ) {
                Text(
                    text = "Reminder",
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.width(5.dp))
        TaskList(calendarEventHelper)

    }



}

@Composable
fun TaskList(calendarEventHelper: CalendarEventHelper) {

    var calendarEvents by remember { mutableStateOf<List<CalendarEventWithDetails>>(emptyList()) }

    calendarEventHelper.getEventsWithRemindersAndAttendeesForCurrentDate {
        calendarEvents = it
        Log.e("Suraj", "calendarEventHelper: "+calendarEvents )

    }

   /* viewModel.getGetCalendarEvents {
        calendarEvents = it
    }*/

    LazyColumn{
        this.items(calendarEvents){
            Log.e("Suraj", "TaskList: "+calendarEvents )
            TaskListItem(it)
        }
    }






    val listOfTask = mutableListOf<Task>()
    //val clr = generateRandomColors()
    listOfTask.add(
        Task("You Have A Meeting", listOf("P","S"),"3:00 PM","3:30 PM",
            Color(0xFFE4B877),
            Color(0xFF6A420D)
        )
    )
    listOfTask.add(
        Task("Call With Team", listOf("A","S"),"4:00 PM","4:30 PM",
            Color(0xFFB0BBBD),
            Color(0xFF3B4648)
        )
    )

    listOfTask.add(Task("Mobile Team Meeting", listOf("K","S"),"9:00 PM","9:30 PM",generateRandomColors().first,generateRandomColors().second))
    listOfTask.add(Task("Production release", listOf("S","V"),"10:00 AM","10:30 AM",generateRandomColors().first,generateRandomColors().second))

    /*LazyColumn{
        item(listOfTask){
            TaskListItem()
        }

    }*/

    /*LazyColumn{
        this.items(listOfTask){
            TaskListItem(it)
        }
    }*/


}

fun generateRandomColors(): Pair<Color, Color> {
    val whiteContrastThreshold = 0.5f

    fun isContrastWithWhite(color: Color): Boolean {
        val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue).toFloat()
        return luminance > whiteContrastThreshold
    }

    do {
        // Generate a random RGB color
        val randomColor = Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        )

        // Darker shade
        val darkerShade = randomColor.copy(alpha = 0.8f)
        val darkerHex = Integer.toHexString(darkerShade.toArgb()).substring(2)
        val darkerColor = Color(android.graphics.Color.parseColor("#$darkerHex"))

        // Lighter shade
        val lighterShade = randomColor.copy(alpha = 0.5f)
        val lighterHex = Integer.toHexString(lighterShade.toArgb()).substring(2)
        val lighterColor = Color(android.graphics.Color.parseColor("#$lighterHex"))

        if (isContrastWithWhite(darkerColor) && isContrastWithWhite(lighterColor)) {
            //return Pair(darkerColor, lighterColor)
            return Pair(Color("#${lighterHex}".hashCode()), Color("#${darkerHex}".hashCode()))
        }

    } while (true)
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

fun generateColors():Pair<Color, Color>{

    val randomColor = generateRandomColor()
    val lightColor = lightenColor(randomColor)
    val darkenColor = darkenColor(randomColor)

    return Pair(Color("#${lightColor}".hashCode()), Color("#${darkenColor}".hashCode()))

}









@Composable
fun TaskListItem(calendarEventWithDetails: CalendarEventWithDetails) {
    val colors = generateColors()
    //colors.first = light color
    //colors.second  = dark color

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 16.dp, end = 16.dp)
            .background(color = colors.first, shape = RoundedCornerShape(size = 20.dp)),
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Text(
                text = calendarEventWithDetails.title,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight(800),
                    color = colors.second,
                ),
                softWrap = true,
                modifier = Modifier
                    .wrapContentHeight(),
            )
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color(0x828C1404),
                            radius = this.size.maxDimension
                        )
                    },
                text = "S",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE6E6E6),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color(0x8204198C),
                            radius = this.size.maxDimension
                        )
                    },
                text = "P",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFE6E6E6),
                    textAlign = TextAlign.Center,
                )
            )


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Text(
                    text = calendarEventWithDetails.startTime.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        color = colors.second,
                    ),
                    modifier = Modifier
                        .wrapContentHeight(),
                )
                Text(
                    text = "Start",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = colors.second,
                    ),
                    modifier = Modifier
                        .wrapContentHeight(),
                )
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = colors.first)
            ) {
                Text(
                    text = "${calendarEventWithDetails.duration} min",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = colors.second,
                    )
                )

            }

            Column {
                Text(
                    text = calendarEventWithDetails.endTime.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        color = colors.second,
                    ),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentHeight(),
                )
                Text(
                    text = "End",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = colors.second,
                    ),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentHeight(),
                )
            }


        }


    }
}













@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    SchedulerTheme {
        Headers()
    }
}

@Preview(showBackground = true)
@Composable
fun WeekDayPreview() {
    SchedulerTheme {
        showWeekDay(getCurrentWeekDay())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ShowDateAndTimePreview() {
    SchedulerTheme {
        ShowDateAndTime(getCurrentTime(), getCurrentTimeZone()!!, getCurrentDate())
    }
}
/*

@Preview(showBackground = true)
@Composable
fun ShowTodaysTaskPreview() {
    SchedulerTheme {
        ShowTodaysTask(viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListItemPreview() {
    SchedulerTheme {
        val task = Task("You have A Meeting", listOf("P","S"),"3:00 PM","3:30 PM",Color(0xFFE4B877),Color(0xFF6A420D))
        TaskListItem(task)
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    SchedulerTheme {
        TaskList(viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SchedulerTheme {
        HomeScree(viewModel)
    }
}
*/






