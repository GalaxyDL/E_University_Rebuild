package com.galaxydl.e_university.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.galaxydl.e_university.classTable.ClassTableViewModel
import com.galaxydl.e_university.data.source.local.*
import com.galaxydl.e_university.data.source.network.ClassTableCrawler
import com.galaxydl.e_university.data.source.network.HolidayInfoBmobRepository
import com.galaxydl.e_university.data.source.network.LoginHelper
import com.galaxydl.e_university.data.source.network.StartingDayBmobRepository
import com.galaxydl.e_university.main.MainActivityViewModel

class ViewModelFactory private constructor(
        private val mApplication: Application,
        private val mClassRepository: ClassRepository,
        private val mExamRepository: ExamRepository,
        private val mScoreRepository: ScoreRepository,
        private val mUserInfoRepository: UserInfoRepository,
        private val mHolidayInfoBmobRepository: HolidayInfoBmobRepository,
        private val mStartingDayBmobRepository: StartingDayBmobRepository,
        private val mStartingDayRepository: StartingDayRepository,
        private val mLoginHelper: LoginHelper,
        private val mClassTableCrawler: ClassTableCrawler) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private lateinit var INSTANCE: ViewModelFactory

        fun getInstance(application: Application): ViewModelFactory {
            if (!::INSTANCE.isInitialized) {
                synchronized(ViewModelFactory::class) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = ViewModelFactory(application,
                                ClassRepository(application.applicationContext),
                                ExamRepository(application.applicationContext),
                                ScoreRepository(application.applicationContext),
                                UserInfoRepository(application.applicationContext),
                                HolidayInfoBmobRepository(),
                                StartingDayBmobRepository(),
                                StartingDayRepository(application.applicationContext),
                                LoginHelper(),
                                ClassTableCrawler())
                    }
                }
            }
            return INSTANCE
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (modelClass) {
            MainActivityViewModel::class.java -> MainActivityViewModel(
                    mApplication,
                    mUserInfoRepository) as T
            ClassTableViewModel::class.java -> ClassTableViewModel(
                    mApplication,
                    mClassRepository,
                    mHolidayInfoBmobRepository,
                    mStartingDayRepository) as T
            else -> super.create(modelClass)
        }
    }

}