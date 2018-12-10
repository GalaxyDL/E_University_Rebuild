package com.galaxydl.e_university.data.source.network

import android.content.Context
import android.support.v4.util.ArrayMap
import com.galaxydl.e_university.data.source.network.cookie.CookieManager
import okhttp3.OkHttpClient

object OkHttpClientManager {

    private val clients = ArrayMap<String, OkHttpClient>()

    fun getClient(key: String, context: Context): OkHttpClient {
        var client = clients[key]
        if (client == null) {
            val cookieManager = CookieManager()
            cookieManager.init(context.applicationContext)
            client = OkHttpClient.Builder()
                    .cookieJar(cookieManager)
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build()
            clients[key] = client
        }
        return client!!
    }

}