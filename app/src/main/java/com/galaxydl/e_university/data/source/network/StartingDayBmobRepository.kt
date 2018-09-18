package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.bean.StartingDayBean
import okhttp3.OkHttpClient

class StartingDayBmobRepository : BaseBmobRepository<StartingDayBean>() {
    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(BMOB_CLIENT_KEY)
    }

    override fun getUrlSuffix(): String = URL_SUFFIX

    override fun getBeanClass(): Class<StartingDayBean> = StartingDayBean::class.java

    private companion object {
        const val URL_SUFFIX = "StartDate"
    }
}