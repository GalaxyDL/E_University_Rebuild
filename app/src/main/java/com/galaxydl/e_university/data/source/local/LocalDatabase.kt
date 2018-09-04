package com.galaxydl.e_university.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.galaxydl.e_university.data.bean.ClassBean
import com.galaxydl.e_university.data.bean.ExamBean
import com.galaxydl.e_university.data.bean.ScoreBean
import com.galaxydl.e_university.data.dao.ClassBeanDao
import com.galaxydl.e_university.data.dao.ExamBeanDao
import com.galaxydl.e_university.data.dao.ScoreBeanDao

@Database(entities = [ClassBean::class, ExamBean::class, ScoreBean::class],
        version = 1)
abstract class LocalDatabase : RoomDatabase() {
    companion object {
        private val DATABASE_NAME = "eUniversity.db"

        private lateinit var INSTANCE: LocalDatabase

        fun getInstance(context: Context): LocalDatabase {
            synchronized(LocalDatabase::class) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LocalDatabase::class.java,
                            DATABASE_NAME).build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun getClassBeanDao(): ClassBeanDao

    abstract fun getExamBeanDao(): ExamBeanDao

    abstract fun getScoreBeanDao(): ScoreBeanDao
}