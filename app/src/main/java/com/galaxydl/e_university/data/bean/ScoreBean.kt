package com.galaxydl.e_university.data.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "score_bean")
data class ScoreBean(@PrimaryKey val id: String,
                     val title: String,
                     val score: Double,
                     val credit: Double) : BaseBean()