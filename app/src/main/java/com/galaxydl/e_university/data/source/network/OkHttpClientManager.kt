package com.galaxydl.e_university.data.source.network

import android.support.v4.util.ArrayMap
import com.galaxydl.e_university.data.source.network.cookie.CookieManager
import okhttp3.OkHttpClient

object OkHttpClientManager {

    private val clients = ArrayMap<String, OkHttpClient>()

    fun getClient(key: String): OkHttpClient {
        var client = clients[key]
        if (client == null) {
            client = OkHttpClient.Builder()
                    .cookieJar(CookieManager())
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build()
            clients[key] = client
        }
        return client!!
    }

}