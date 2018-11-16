package com.galaxydl.e_university.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.galaxydl.e_university.R
import com.galaxydl.e_university.data.source.local.UserInfoRepository
import com.galaxydl.e_university.data.source.network.CaptchaCrawler
import com.galaxydl.e_university.data.source.network.LoginHelper
import com.galaxydl.e_university.utils.SingleLiveEvent

class LoginViewModel(application: Application,
                     private val mLoginHelper: LoginHelper,
                     private val mUserInfoRepository: UserInfoRepository,
                     private val mCaptchaCrawler: CaptchaCrawler)
    : AndroidViewModel(application) {

    private val username = ObservableField<String>()

    private val password = ObservableField<String>()

    private val captcha = ObservableField<String>()

    private val captchaImage = ObservableField<Bitmap>()

    private val errorInfo = ObservableField<String>()

    private val savePassword = ObservableBoolean(false)

    private val needCaptcha = ObservableBoolean(false)

    private val error = ObservableBoolean(false)

    private val context: Context by lazy { application.applicationContext }

    val onLoginEvent = SingleLiveEvent<Boolean>()

    private lateinit var mLoginParams: LoginHelper.LoginParams

    fun start() {
        val userInfo = mUserInfoRepository.get()
        userInfo?.let {
            username.set(it.userName)
            if (it.savePassword) {
                password.set(it.password)
                savePassword.set(true)
            }
        }
        mLoginHelper.getLoginParams { loginParams ->
            loginParams?.let {
                if (it.hasLogin) {
                    onLoginEvent.call()
                } else {
                    if (it.needCaptcha) {
                        needCaptcha.set(it.needCaptcha)
                        getCaptcha()
                    }
                }
                mLoginParams = it
            }
        }
    }

    private fun getCaptcha() {
        mCaptchaCrawler.load({
            if (it.isEmpty()) {
                //TODO 提示用户验证码获取失败
                getCaptcha()
            } else {
                captchaImage.set(it[0].captcha)
            }
        }, {
            //TODO 提示用户验证码获取失败
            getCaptcha()
        })
    }

    fun login() {
        if (username.get() == null || password.get() == null || (needCaptcha.get() && captcha.get() == null)) {
            onError(R.string.login_empty_info)
            return
        }
        if (::mLoginParams.isInitialized) {
            mLoginParams.apply {
                addParameter(USERNAME_NAME, username.get()!!)
                addParameter(PASSWORD_NAME, password.get()!!)
                if (needCaptcha) {
                    addParameter(CAPTCHA_NAME, captcha.get()!!)
                }
            }
            doLogin()
        } else {
            onError(R.string.Login_server_error)
        }
    }

    private fun doLogin() {
        mLoginHelper.login(mLoginParams) { success ->
            if (success) {
                onLoginEvent.call()
            } else {
                onError(R.string.Login_server_error)
            }
        }
    }

    private fun onError(stringId: Int) {
        error.set(true)
        errorInfo.set(context.getString(stringId))
    }

    private companion object {
        const val USERNAME_NAME = "username"
        const val PASSWORD_NAME = "password"
        const val CAPTCHA_NAME = "captchaResponse"
    }

}