package com.galaxydl.e_university.data.source.network

import com.galaxydl.e_university.data.source.network.cookie.CookieManager
import okhttp3.OkHttpClient

object OkHttpClientManager {

    private val clients = HashMap<String, OkHttpClient>()

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