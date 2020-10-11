package com.davinciapp.bigwatson.view.tweets

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DateUtils @Inject constructor() {

    fun getDateString(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return formatter.format(date)
    }

}