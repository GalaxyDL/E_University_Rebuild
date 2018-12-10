package com.galaxydl.e_university.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.galaxydl.e_university.classTable.ClassTableViewModel
import com.galaxydl.e_university.data.source.DataUpdateUtil
import com.galaxydl.e_university.data.source.local.*
import com.galaxydl.e_university.data.source.network.*
import com.galaxydl.e_university.login.LoginViewModel
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
        private val mClassTableCrawler: ClassTableCrawler,
        private val mCaptchaCrawler: CaptchaCrawler) : ViewModelProvider.NewInstanceFactory() {

    private val mDataUpdateUtil: DataUpdateUtil
            by lazy { DataUpdateUtil(mClassRepository, mClassTableCrawler) }

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(application,
                                ClassRepository(application.applicationContext),
                                ExamRepository(application.applicationContext),
                                ScoreRepository(application.applicationContext),
                                UserInfoRepository(application.applicationContext),
                                HolidayInfoBmobRepository(application.applicationContext),
                                StartingDayBmobRepository(application.applicationContext),
                                StartingDayRepository(application.applicationContext),
                                LoginHelper(application.applicationContext),
                                ClassTableCrawler(application.applicationContext),
                                CaptchaCrawler(application.applicationContext))
                    }
                }
            }
            return INSTANCE!!
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
            LoginViewModel::class.java -> LoginViewModel(
                    mApplication,
                    mLoginHelper,
                    mUserInfoRepository,
                    mCaptchaCrawler,
                    mDataUpdateUtil) as T
            else -> super.create(modelClass)
        }
    }

}