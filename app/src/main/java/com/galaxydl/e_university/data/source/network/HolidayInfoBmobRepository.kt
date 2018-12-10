package com.galaxydl.e_university.data.source.network

import android.content.Context
import com.galaxydl.e_university.data.bean.HolidayInfoBean
import okhttp3.OkHttpClient

class HolidayInfoBmobRepository(context: Context) : BaseBmobRepository<HolidayInfoBean>(context) {
    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(BMOB_CLIENT_KEY, context)
    }

    override fun getUrlSuffix(): String = URL_SUFFIX

    override fun getBeanClass(): Class<HolidayInfoBean> = HolidayInfoBean::class.java

    private companion object {
        const val URL_SUFFIX = "HolidayInfo"
    }

}