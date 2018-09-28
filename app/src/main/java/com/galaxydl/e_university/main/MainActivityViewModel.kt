package com.galaxydl.e_university.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.galaxydl.e_university.data.bean.UserInfoBean
import com.galaxydl.e_university.data.source.local.UserInfoRepository

class MainActivityViewModel(application: Application,
                            private val mUserInfoRepository: UserInfoRepository)
    : AndroidViewModel(application) {

    val userInfo: ObservableField<UserInfoBean> = ObservableField()

    fun start() {
        userInfo.set(mUserInfoRepository.get())
    }
}