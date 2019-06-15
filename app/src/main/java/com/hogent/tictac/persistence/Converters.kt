package com.hogent.tictac.persistence

import androidx.room.TypeConverter
import com.hogent.tictac.common.Note

private const val SEPARATOR = ","

class Converters {

    @TypeConverter
    fun restoreEnum(enumName: String): Note = Note.valueOf(enumName)

    @TypeConverter
    fun saveEnumToString(enumType: Note) = enumType.name

    @TypeConverter
    fun daysOfWeekToString(chords: MutableList<Note>?): String? =
        chords?.map { it.name }?.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun stringToDaysOfWeek(chords: String?): MutableList<Note>? =
        chords?.split(SEPARATOR)?.map { Note.valueOf(it) }?.toMutableList()

}