package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.bean.BaseBean
import okhttp3.Headers
import okhttp3.OkHttpClient

abstract class BaseCrawler<B : BaseBean> : NetworkDataSource<B> {

    protected abstract val client: OkHttpClient

    protected companion object {
        val TJUT_CLIENT_KEY = "tjut_client"
        val HEADERS = Headers.Builder()
                .add("Accept", "*/*")
                .add("Upgrade-Insecure-Requests", "1")
                .add("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .add("Connection", "Keep-alive")
                .build()
    }

    abstract suspend fun crawl(): List<B>

}