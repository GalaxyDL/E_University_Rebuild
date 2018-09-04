package com.galaxydl.e_university.data.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "exam_bean")
class ExamBean(@PrimaryKey val id: String,
               val title: String,
               val time: String,
               val location: String,
               val modus: String,
               val credit: Int,
               val teacher: String) : BaseBean()