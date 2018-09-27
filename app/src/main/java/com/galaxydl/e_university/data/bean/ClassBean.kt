package com.galaxydl.e_university.data.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "class_bean")
data class ClassBean(@PrimaryKey val id: String = "NULL",
                     val title: String = "NULL",
                     val time: String = "NULL",
                     val location: String = "NULL",
                     val day: Int = 0,
                     val date: Int = 0,
                     val month: Int = 0,
                     val passed: Boolean = false,
                     val teacher: String = "NULL",
                     val startWeek: Int = 0,
                     val endWeek: Int = 0,
                     val singleWeek: Int = 0,
                     val doubleWeek: Int = 0,
                     val startTime: Int = 0,
                     val endTime: Int = 0,
                     val classNumber: Int = 0,
                     val colorId: Int = 0,
                     @Ignore val isForDate: Boolean = false) : BaseBean() {

    constructor(month: Int, day: Int, date: Int)
            : this(month = month,
            day = day,
            date = date,
            time = "${month}月 ${day}日",
            isForDate = true)
}