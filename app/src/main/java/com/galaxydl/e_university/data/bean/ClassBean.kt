package com.galaxydl.e_university.data.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "class_bean")
data class ClassBean(@PrimaryKey val id: String,
                     val title: String,
                     val time: String,
                     val location: String,
                     val day: Int,
                     val date: Int,
                     val month: Int,
                     val passed: Boolean,
                     val teacher: String,
                     val startWeek: Int,
                     val endWeek: Int,
                     val singleWeek: Int,
                     val doubleWeek: Int,
                     val startTime: Int,
                     val endTime: Int,
                     val classNumber: Int,
                     val colorId: Int) : BaseBean()