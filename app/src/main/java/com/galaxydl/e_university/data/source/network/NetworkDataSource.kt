package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.bean.BaseBean

interface NetworkDataSource<B : BaseBean> {

    fun load(onLoad: (List<B>) -> Unit)

}