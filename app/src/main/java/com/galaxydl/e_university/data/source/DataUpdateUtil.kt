package com.galaxydl.e_university.data.source

import com.galaxydl.e_university.data.source.local.ClassRepository
import com.galaxydl.e_university.data.source.network.ClassTableCrawler

class DataUpdateUtil(private val mClassRepository: ClassRepository,
                     private val mClassTableCrawler: ClassTableCrawler) {

    fun update(onUpdate: (Boolean) -> Unit) {
        mClassTableCrawler.load({
            if (it.isNotEmpty()) {
                mClassRepository.clear()
                mClassRepository.addAll(it)
                onUpdate(true)
            } else {
                onUpdate(false)
            }
        }, {
            onUpdate(false)
        })
    }

}