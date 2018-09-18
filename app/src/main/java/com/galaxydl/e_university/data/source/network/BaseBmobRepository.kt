package com.galaxydl.e_university.data.source.network

import com.alibaba.fastjson.JSON
import com.galaxydl.e_university.data.bean.BaseBean
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class BaseBmobRepository<B : BaseBean> : NetworkDataSource<B> {
    protected abstract val client: OkHttpClient

    protected companion object {
        const val BMOB_CLIENT_KEY = "bmob_client"
        const val URL_PREFIX = "http://api2.bmob.cn/1/classes/"
        val HEADERS = Headers.Builder()
                .add("X-Bmob-Application-Id", "e6a7025c7eead5d5edac4ddf01137d14")
                .add("X-Bmob-REST-API-Key", "5a8683aa09d6ccd6d5e5e3117b912f71")
                .add("Content-Type", "application/json")
                .build()!!
    }

    override fun load(onLoad: (List<B>) -> Unit, onError: (Exception) -> Unit) {
        val request = Request.Builder().url(URL_PREFIX + getUrlSuffix())
                .get()
                .headers(HEADERS)
                .build()
        launch(CommonPool) {
            try {
                val reponse = client.newCall(request).execute()
                launch(UI) {
                    onLoad(JSON.parseArray(reponse.body().toString(), getBeanClass()))
                }
            } catch (e: Exception) {
                launch(UI) { onError(e) }
            }
        }
    }

    abstract fun getUrlSuffix(): String

    abstract fun getBeanClass(): Class<B>

}