package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.source.network.cookie.CookieManager
import okhttp3.OkHttpClient

object OkHttpClientManager {

    private val clients = HashMap<String, OkHttpClient>()

    fun getClient(key: String): OkHttpClient {
        val client = clients.get(key)
        return if (client == null) {
            OkHttpClient.Builder()
                    .cookieJar(CookieManager())
                    .build()
        } else {
            client
        }
    }

}