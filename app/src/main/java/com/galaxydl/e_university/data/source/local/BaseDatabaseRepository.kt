package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.BaseBean
import com.galaxydl.e_university.data.dao.BaseDao

abstract class BaseDatabaseRepository<B : BaseBean, D : BaseDao<B>>(val context: Context)
    : LocalDataSource<B> {

    val localDatabase: LocalDatabase by lazy {
        LocalDatabase.getInstance(context)
    }

    abstract val dao: D

    override fun get(): B? = dao.get()

    override fun list(): List<B> = dao.list()

    override fun add(bean: B) {
        dao.list()
    }

    override fun addAll(beans: List<B>) {
        beans.forEach { add(it) }
    }

    override fun clear() {
        dao.clear()
    }
}