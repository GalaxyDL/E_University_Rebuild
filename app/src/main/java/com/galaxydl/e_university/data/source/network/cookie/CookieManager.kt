package com.galaxydl.e_university.data.source.network.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager : CookieJar {

    private lateinit var cookieStore: PersistentCookieStore

    fun init(context: Context) {
        cookieStore = PersistentCookieStore(context)
    }

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        cookieStore.add(cookies)
    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> = cookieStore.get(url)
}