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
import com.galaxydl.e_university.utils.SnackbarMessage

class LoginViewModel(application: Application,
                     private val mLoginHelper: LoginHelper,
                     private val mUserInfoRepository: UserInfoRepository,
                     private val mCaptchaCrawler: CaptchaCrawler)
    : AndroidViewModel(application) {

    val username = ObservableField<String>()

    val password = ObservableField<String>()

    val captcha = ObservableField<String>()

    val captchaImage = ObservableField<Bitmap>()

    val savePassword = ObservableBoolean(false)

    val needCaptcha = ObservableBoolean(false)

    val usernameIsEmpty = ObservableBoolean(false)

    val passwordIsEmpty = ObservableBoolean(false)

    val captchaIsEmpty = ObservableBoolean(false)

    private val context: Context by lazy { application.applicationContext }

    val onLoginEvent = SingleLiveEvent<Boolean>()

    val snackbarMessage = SnackbarMessage()
 
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

    fun getCaptcha() {
        mCaptchaCrawler.load({
            if (it.isEmpty()) {
                onError(R.string.login_captcha_error)
            } else {
                captchaImage.set(it[0].captcha)
            }
        }, {
            onError(R.string.login_captcha_error)
        })
    }

    fun login() {
        usernameIsEmpty.set(username.get() == null)
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
            onError(R.string.login_server_error)
        }
    }

    private fun doLogin() {
        mLoginHelper.login(mLoginParams) { success ->
            if (success) {
                onLoginEvent.call()
            } else {
                onError(R.string.login_server_error)
            }
        }
    }

    private fun onError(stringId: Int) {
        snackbarMessage.show(stringId)
    }

    private companion object {
        const val USERNAME_NAME = "username"
        const val PASSWORD_NAME = "password"
        const val CAPTCHA_NAME = "captchaResponse"
    }

}