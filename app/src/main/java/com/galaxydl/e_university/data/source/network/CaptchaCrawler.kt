package com.galaxydl.e_university.data.source.network

import android.content.Context
import android.graphics.BitmapFactory
import com.galaxydl.e_university.data.bean.CaptchaBean
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class CaptchaCrawler(context: Context) : BaseCrawler<CaptchaBean>(context) {
    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(TJUT_CLIENT_KEY, context)
    }

    override suspend fun crawl(): List<CaptchaBean> {
        val request = Request.Builder().url(CAPTCHA_URL)
                .get()
                .headers(HEADERS)
                .build()
        val response = client.newCall(request).execute()
        val captchaBean: CaptchaBean
        captchaBean = CaptchaBean(BitmapFactory.decodeStream(response.body()!!.byteStream()))
        return listOf(captchaBean)
    }

    override fun load(onLoad: (List<CaptchaBean>) -> Unit, onError: (Exception) -> Unit) {
        launch(CommonPool) {
            try {
                val result = crawl()
                launch(UI) { onLoad(result) }
            } catch (e: Exception) {
                launch(UI) { onError(e) }
            }
        }
    }

    private companion object {
        val CAPTCHA_URL = "http://authserver.tjut.edu.cn/authserver/captcha.html"
    }

}