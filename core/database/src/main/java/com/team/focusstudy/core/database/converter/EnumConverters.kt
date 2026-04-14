package com.team.focusstudy.core.database.converter

import androidx.room.TypeConverter
import com.team.focusstudy.core.model.advice.AdviceType
import com.team.focusstudy.core.model.reminder.ReminderType
import com.team.focusstudy.core.model.session.DistractionType
import com.team.focusstudy.core.model.session.SessionStatus
import com.team.focusstudy.core.model.task.TaskStatus

class EnumConverters {

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromSessionStatus(value: SessionStatus): String = value.name

    @TypeConverter
    fun toSessionStatus(value: String): SessionStatus = SessionStatus.valueOf(value)

    @TypeConverter
    fun fromDistractionType(value: DistractionType): String = value.name

    @TypeConverter
    fun toDistractionType(value: String): DistractionType = DistractionType.valueOf(value)

    @TypeConverter
    fun fromAdviceType(value: AdviceType): String = value.name

    @TypeConverter
    fun toAdviceType(value: String): AdviceType = AdviceType.valueOf(value)

    @TypeConverter
    fun fromReminderType(value: ReminderType): String = value.name

    @TypeConverter
    fun toReminderType(value: String): ReminderType = ReminderType.valueOf(value)

    @TypeConverter
    fun fromBoolean(value: Boolean): Int = if (value) 1 else 0

    @TypeConverter
    fun toBoolean(value: Int): Boolean = value == 1
}