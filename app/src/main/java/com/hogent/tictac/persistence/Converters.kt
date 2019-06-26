package com.hogent.tictac.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



private const val SEPARATOR = ","

class Converters {

    @TypeConverter
    fun restoreEnum(enumName: String): Model.Note = Model.Note.valueOf(enumName)

    @TypeConverter
    fun saveEnumToString(enumType: Model.Note) = enumType.name

    @TypeConverter
    fun daysOfWeekToString(chords: MutableList<Model.Note>?): String? =
        chords?.joinToString(separator = SEPARATOR) { it.name }

    @TypeConverter
    fun stringToDaysOfWeek(chords: String?): MutableList<Model.Note>? =
            chords?.split(SEPARATOR)?.map { Model.Note.valueOf(it) }?.toMutableList()

    @TypeConverter
    fun fromSongStrings(value: String): ArrayList<Model.Song> {
        val listType = object : TypeToken<ArrayList<Model.Song>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromSongArrayList(list: ArrayList<Model.Song>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}