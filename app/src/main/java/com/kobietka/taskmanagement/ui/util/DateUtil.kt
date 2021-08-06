package com.kobietka.taskmanagement.ui.util

import java.util.*
import javax.inject.Inject


class DateUtil
@Inject constructor(private val calendar: Calendar){

    fun getCurrentDate(): String {
        return "${format(calendar.get(Calendar.DAY_OF_MONTH))}/${format(calendar.get(Calendar.MONTH) + 1)}/${calendar.get(Calendar.YEAR)}"
    }

    private fun format(number : Int): String {
        return if (number >= 10) number.toString() else "0$number"
    }
}