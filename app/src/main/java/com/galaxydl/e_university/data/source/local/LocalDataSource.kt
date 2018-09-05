package com.galaxydl.e_university.data.source.local

import com.galaxydl.e_university.data.bean.BaseBean

interface LocalDataSource<B : BaseBean> {

    fun get(): B?

    fun list(): List<B>

    fun add(bean: B)

    fun addAll(beans: List<B>)

    fun clear()
}