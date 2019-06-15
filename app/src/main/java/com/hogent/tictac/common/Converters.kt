package com.hogent.tictac.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hogent.tictac.common.Note

private const val SEPARATOR = ","

class Converters {

    @TypeConverter
    fun restoreEnum(enumName: String): Note = Note.valueOf(enumName)

    @TypeConverter
    fun saveEnumToString(enumType: Note) = enumType.name

    @TypeConverter
    fun noteToString(chords: MutableList<Note>?): String? =
            chords?.map { it.name }?.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun stringToNotes(chords: String?): MutableList<Note>? =
            chords?.split(SEPARATOR)?.map { Note.valueOf(it) }?.toMutableList()
}