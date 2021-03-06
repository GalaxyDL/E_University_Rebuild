package com.galaxydl.e_university.data.dao

import com.galaxydl.e_university.data.bean.BaseBean

interface BaseDao<B : BaseBean> {

    fun get(): B?

    fun list(): List<B>

    fun add(bean: B)

    fun clear()
}