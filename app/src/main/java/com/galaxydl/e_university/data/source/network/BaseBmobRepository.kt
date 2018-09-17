package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.bean.BaseBean
import okhttp3.Headers
import okhttp3.OkHttpClient

abstract class BaseBmobRepository<B : BaseBean> : NetworkDataSource<B> {
    protected abstract val client: OkHttpClient

    protected companion object {
        const val BMOB_CLIENT_KEY = "bmob_client"
        const val URL_FREFIX = "http://api2.bmob.cn/1/classes/"
        val HEADERS = Headers.Builder()
                .add("X-Bmob-Application-Id", "e6a7025c7eead5d5edac4ddf01137d14")
                .add("X-Bmob-REST-API-Key", "5a8683aa09d6ccd6d5e5e3117b912f71")
                .add("Content-Type", "application/json")
                .build()!!
    }
}