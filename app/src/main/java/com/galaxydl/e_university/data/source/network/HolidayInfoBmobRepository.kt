package com.galaxydl.e_university.data.source.network

import com.alibaba.fastjson.JSON
import com.galaxydl.e_university.data.bean.HolidayInfoBean
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class HolidayInfoBmobRepository : BaseBmobRepository<HolidayInfoBean>() {
    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(BMOB_CLIENT_KEY)
    }

    override fun load(onLoad: (List<HolidayInfoBean>) -> Unit, onError: (Exception) -> Unit) {
        val request = Request.Builder().url(URL_FREFIX + URL_SUFFIX)
                .get()
                .headers(HEADERS)
                .build()
        launch(CommonPool) {
            try {
                val reponse = client.newCall(request).execute()
                launch(UI) {
                    onLoad(JSON.parseArray(reponse.body().toString(), HolidayInfoBean::class.java))
                }
            } catch (e: Exception) {
                launch(UI) { onError(e) }
            }
        }
    }

    private companion object {
        const val URL_SUFFIX = "GameScore"
    }

}