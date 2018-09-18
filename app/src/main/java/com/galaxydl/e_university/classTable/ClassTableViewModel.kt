package com.galaxydl.e_university.classTable

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.util.Log
import com.galaxydl.e_university.data.bean.ClassBean
import com.galaxydl.e_university.data.bean.HolidayInfoBean
import com.galaxydl.e_university.data.bean.StartingDayBean
import com.galaxydl.e_university.data.source.local.ClassRepository
import com.galaxydl.e_university.data.source.local.StartingDayRepository
import com.galaxydl.e_university.data.source.network.HolidayInfoBmobRepository
import java.util.*
import kotlin.collections.HashMap

class ClassTableViewModel(application: Application,
                          private val mClassTableRepository: ClassRepository,
                          private val mHolidayInfoBmobRepository: HolidayInfoBmobRepository,
                          private val mStartingDayRepository: StartingDayRepository)
    : AndroidViewModel(application) {

    val empty = ObservableBoolean()

    val classTable = ObservableArrayList<ClassBean>()

    private val context: Context by lazy { application.applicationContext }

    private lateinit var holidayMap: HashMap<HolidayInfoBean.Day, HolidayInfoBean>

    private lateinit var startingDay: StartingDayBean

    fun start() {
        mStartingDayRepository.get()?.let {
            startingDay = it
        }
        if (!::startingDay.isInitialized) {
            empty.set(true)
            return
        }
        mHolidayInfoBmobRepository.load({
            holidayMap = HashMap()
            it.forEach {
                holidayMap[it.formerDay] = it
            }
            parseClassTable(mClassTableRepository.list())
        }, {
            Log.e(TAG, HOLIDAY_LOAD_ERROR, it)
            holidayMap = HashMap()
            parseClassTable(mClassTableRepository.list())
        })
    }

    private fun parseClassTable(classes: List<ClassBean>) {
        val allClasses = ArrayList<ClassBean>()
        for (i in 1..20) {
            allClasses.addAll(parseWeekClasses(classes, i))
        }
        classTable.addAll(allClasses)
        empty.set(allClasses.isEmpty())
    }

    private fun parseWeekClasses(classes: List<ClassBean>, week: Int): List<ClassBean> {
        val weekClasses = ArrayList<ClassBean>()
        for (i in 1..7) {
            weekClasses.addAll(parseDayClasses(classes, week, i))
        }
        return weekClasses
    }

    private fun parseDayClasses(classes: List<ClassBean>, week: Int, date: Int): List<ClassBean> {
        val dayClasses = ArrayList<ClassBean>()
        for (aClass in classes) {
            if (judge(aClass, week, date)) {
                dayClasses.add(aClass)
            }
        }
        return dayClasses
    }

    private fun judge(aClass: ClassBean, week: Int, date: Int): Boolean {
        var actualDate = date
        var actualWeek = week
        holidayMap[HolidayInfoBean.Day(week, date)]?.let {
            actualDate = it.actualDay.day
            actualWeek = it.actualDay.week
        }
        if (aClass.date == actualDate) {
            if (aClass.startWeek <= actualWeek && aClass.endWeek >= actualWeek) {
                if (aClass.singleWeek == week % 2 || aClass.doubleWeek == week % 2 + 1) {
                    return true
                }
            }
        }
        return false
    }

    private companion object {
        const val TAG = "ClassTableViewModel"
        const val HOLIDAY_LOAD_ERROR = "holiday info load error"

    }

}