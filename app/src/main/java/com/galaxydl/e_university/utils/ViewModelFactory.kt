package com.galaxydl.e_university.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.galaxydl.e_university.data.source.local.ClassRepository
import com.galaxydl.e_university.data.source.local.ExamRepository
import com.galaxydl.e_university.data.source.local.ScoreRepository
import com.galaxydl.e_university.data.source.local.UserInfoRepository
import com.galaxydl.e_university.data.source.network.ClassTableCrawler

class ViewModelFactory private constructor(
        private val mApplication: Application,
        private val mClassRepository: ClassRepository,
        private val mExamRepository: ExamRepository,
        private val mScoreRepository: ScoreRepository,
        private val mUserInfoRepository: UserInfoRepository,
        private val mClassTableCrawler: ClassTableCrawler) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private lateinit var INSTANCE: ViewModelFactory

        fun getInstance(application: Application): ViewModelFactory {
            synchronized(ViewModelFactory::class) {
                if (!::INSTANCE.isInitialized) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = ViewModelFactory(application,
                                ClassRepository(application.applicationContext),
                                ExamRepository(application.applicationContext),
                                ScoreRepository(application.applicationContext),
                                UserInfoRepository(application.applicationContext),
                                ClassTableCrawler())
                    }
                }
            }
            return INSTANCE
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            // TODO add other ViewModels
            else -> super.create(modelClass)
        }
    }

}