package com.hogent.tictac.persistence

import androidx.room.TypeConverter

private const val SEPARATOR = ","

class Converters {

    @TypeConverter
    fun restoreEnum(enumName: String): Model.Note = Model.Note.valueOf(enumName)

    @TypeConverter
    fun saveEnumToString(enumType: Model.Note) = enumType.name

    @TypeConverter
    fun daysOfWeekToString(chords: MutableList<Model.Note>?): String? =
            chords?.map { it.name }?.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun stringToDaysOfWeek(chords: String?): MutableList<Model.Note>? =
            chords?.split(SEPARATOR)?.map { Model.Note.valueOf(it) }?.toMutableList()

}