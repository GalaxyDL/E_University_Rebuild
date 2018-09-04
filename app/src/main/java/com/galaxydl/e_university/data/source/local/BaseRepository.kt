package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.BaseBean
import com.galaxydl.e_university.data.dao.BaseDao

abstract class BaseRepository<B : BaseBean, D : BaseDao<B>>(val context: Context) {
    val localDatabase: LocalDatabase by lazy {
        LocalDatabase.getInstance(context)
    }

    abstract val dao: D

    fun list(): List<B> = dao.list()

    fun add(bean: B) {
        dao.list()
    }

    fun clear() {
        dao.clear()
    }
}