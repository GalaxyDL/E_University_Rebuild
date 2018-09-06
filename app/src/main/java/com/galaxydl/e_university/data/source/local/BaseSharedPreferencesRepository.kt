package com.galaxydl.e_university.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.galaxydl.e_university.data.bean.BaseBean

abstract class BaseSharedPreferencesRepository<B : BaseBean>(val context: Context)
    : LocalDataSource<B> {

    protected val sp: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPreferencesName(), Context.MODE_PRIVATE)
    }

    abstract fun sharedPreferencesName(): String

}