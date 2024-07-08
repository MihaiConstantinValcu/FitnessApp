package com.example.fitnessapp.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}