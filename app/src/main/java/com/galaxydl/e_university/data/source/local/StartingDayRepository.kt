package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.StartingDayBean

class StartingDayRepository(context: Context)
    : BaseSharedPreferencesRepository<StartingDayBean>(context) {
    override fun sharedPreferencesName(): String = "starting_day"

    companion object {
        private const val STARTING_YEAR_KEY = "startingYear"
        private const val STARTING_MONTH_KEY = "startingMonth"
        private const val STARTING_DAY_KEY = "startingDay"
    }

    override fun get(): StartingDayBean? {
        val year = sp.getInt(STARTING_YEAR_KEY, 0)
        if (year == 0) {
            return null
        }
        val month = sp.getInt(STARTING_MONTH_KEY, 0)
        val day = sp.getInt(STARTING_DAY_KEY, 0)
        return StartingDayBean(year, month, day)
    }

    override fun list(): List<StartingDayBean> {
        throw UnsupportedOperationException()
    }

    override fun add(bean: StartingDayBean) {
        val editor = sp.edit()
        editor.putInt(STARTING_YEAR_KEY, bean.year)
        editor.putInt(STARTING_MONTH_KEY, bean.month)
        editor.putInt(STARTING_DAY_KEY, bean.day)
        editor.apply()
    }

    override fun addAll(beans: List<StartingDayBean>) {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        val editor = sp.edit()
        editor.remove(STARTING_YEAR_KEY)
        editor.remove(STARTING_MONTH_KEY)
        editor.remove(STARTING_DAY_KEY)
        editor.apply()
    }
}