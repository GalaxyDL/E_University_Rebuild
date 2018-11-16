package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.bean.HolidayInfoBean
import okhttp3.OkHttpClient

class HolidayInfoBmobRepository : BaseBmobRepository<HolidayInfoBean>() {
    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(BMOB_CLIENT_KEY)
    }

    override fun getUrlSuffix(): String = URL_SUFFIX

    override fun getBeanClass(): Class<HolidayInfoBean> = HolidayInfoBean::class.java

    private companion object {
        const val URL_SUFFIX = "HolidayInfo"
    }

}