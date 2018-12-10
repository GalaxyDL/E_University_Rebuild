package com.galaxydl.e_university.data.source.network

import android.content.Context
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class LoginHelper(val context: Context) {

    private val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(TJUT_CLIENT_KEY, context)
    }

    fun login(loginParams: LoginParams,
              onLogin: (Boolean) -> Unit) {
        launch(CommonPool) {
            val requestBody = RequestBody.create(MEDIA_TPYE, loginParams.toString())
            val request = Request.Builder()
                    .url(LOGIN_URL)
                    .headers(HEADERS)
                    .post(requestBody)
                    .build()
            val response = client.newCall(request).execute()
            if (response.code() == 302) {
                if (doSsfwLogin()) {
                    onLogin(true)
                }
            }
            onLogin(false)
        }
    }

    private suspend fun doSsfwLogin(): Boolean {
        val request = Request.Builder()
                .url(SSFW_LOGIN_URL)
                .headers(HEADERS)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val redirectLocation = response.header("location") ?: return false
        return redirectLocation.contains("success=true")
    }

    fun getLoginParams(onGet: (LoginParams?) -> Unit) {
        launch(CommonPool) {
            val request = Request.Builder()
                    .url(INDEX_URL)
                    .headers(HEADERS)
                    .get()
                    .build()
            val response = client.newCall(request).execute()
            when {
                response.code() == 302 -> {
                    onGet(getLoginParams())
                }
                response.code() == 200 -> {
                    onGet(LoginParams(true))
                }
                else -> {
                    onGet(null)
                }
            }
        }
    }

    private suspend fun getLoginParams(): LoginParams {
        val request = Request.Builder()
                .url(LOGIN_URL)
                .headers(HEADERS)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val document: Document = Jsoup.parse(response.body().toString())
        val from = document.getElementById(LOGIN_FROM_ID)
        val loginParams = LoginParams()
        val size = from.childNodeSize()
        var child: Element
        for (i in 1 until size) {
            child = from.child(i)
            loginParams.addParameter(child.attr("name"),
                    child.attr("value"))
        }
        child = from.getElementById(CAPTCHA_DIV_ID)
        loginParams.needCaptcha = child.childNodeSize() > 0
        return loginParams
    }

    private companion object {
        const val TJUT_CLIENT_KEY = "tjut_client"
        const val INDEX_URL = "http://my.tjut.edu.cn/new/index.html"
        const val LOGIN_URL = "http://authserver.tjut.edu.cn/authserver/login?service=http%3A%2F%2Fmy.tjut.edu.cn%2Fnew%2Findex.html"
        const val SSFW_LOGIN_URL = "http://ssfw.tjut.edu.cn/ssfw/j_spring_ids_security_check"
        const val LOGIN_FROM_ID = "casLoginForm"
        const val CAPTCHA_DIV_ID = "cpatchaDiv"

        val HEADERS = Headers.Builder()
                .add("Accept", "*/*")
                .add("Upgrade-Insecure-Requests", "1")
                .add("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .add("Connection", "Keep-alive")
                .build()!!
        val MEDIA_TPYE = MediaType.parse("application/x-www-form-urlencoded")!!
    }

    class LoginParams(var hasLogin: Boolean = false) {
        private val parameters = HashMap<String, String>()
        var needCaptcha: Boolean = false


        fun addParameter(key: String, value: String) {
            parameters[key] = value
        }

        fun getParameter(key: String) = parameters[key]

        override fun toString(): String {
            val stringBuilder = StringBuilder()
            for (entry: Map.Entry<String, String> in parameters) {
                stringBuilder.append("${entry.key}=${entry.value}&")
            }
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
            }
            return stringBuilder.toString()
        }
    }
}