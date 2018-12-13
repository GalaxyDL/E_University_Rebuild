package com.galaxydl.e_university.data.source

import android.util.Log
import com.galaxydl.e_university.data.source.local.ClassRepository
import com.galaxydl.e_university.data.source.network.ClassTableCrawler
import java.util.*

class DataUpdateUtil(private val mClassRepository: ClassRepository,
                     private val mClassTableCrawler: ClassTableCrawler) {

    fun update(onUpdate: (Boolean) -> Unit) {
        mClassTableCrawler.load({
            Log.d("DataUpdateUtil", Arrays.toString(arrayOf(it)))
            if (it.isNotEmpty()) {
                mClassRepository.clear()
                mClassRepository.addAll(it)
                onUpdate(true)
            } else {
                onUpdate(false)
            }
        }, {
            Log.d("DataUpdateUtil", it.toString())
            Log.d("DataUpdateUtil", Arrays.toString(it.stackTrace))
            onUpdate(false)
        })
    }

}