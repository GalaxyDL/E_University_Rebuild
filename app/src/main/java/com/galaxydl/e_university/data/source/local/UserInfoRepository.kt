package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.UserInfoBean

class UserInfoRepository(context: Context) : BaseSharedPreferencesRepository<UserInfoBean>(context) {
    override fun sharedPreferencesName(): String = "user_info"

    companion object {
        private val USER_ID_SP_KEY = "userID"
        private val PASSWORD_SP_KEY = "password"
        private val USER_NAME_SP_KEY = "username"
        private val DEPARTMENT_SP_KEY = "department"
        private val MAJOR_SP_KEY = "major"
        private val SAVE_PASSWORD_SP_KEY = "savePassword"
        private val NULL_STRING = "NULL"
    }

    override fun get(): UserInfoBean? {
        val userId = sp.getString(USER_ID_SP_KEY, NULL_STRING)
        if (NULL_STRING == userId) {
            return null
        }
        val savePassword = sp.getBoolean(SAVE_PASSWORD_SP_KEY, false)
        val password = sp.getString(PASSWORD_SP_KEY, NULL_STRING)
        val username = sp.getString(USER_NAME_SP_KEY, NULL_STRING)
        val department = sp.getString(DEPARTMENT_SP_KEY, NULL_STRING)
        val major = sp.getString(MAJOR_SP_KEY, NULL_STRING)

        return UserInfoBean(userId, password, username, department, major, savePassword)
    }

    override fun list(): List<UserInfoBean> {
        throw UnsupportedOperationException()
    }

    override fun add(bean: UserInfoBean) {
        val editor = sp.edit()
        editor.putString(USER_ID_SP_KEY, bean.userID)
        editor.putString(PASSWORD_SP_KEY, bean.password)
        editor.putString(USER_NAME_SP_KEY, bean.userName)
        editor.putString(DEPARTMENT_SP_KEY, bean.department)
        editor.putString(MAJOR_SP_KEY, bean.major)
        editor.putBoolean(SAVE_PASSWORD_SP_KEY, bean.savePassword)
        editor.apply()
    }

    override fun addAll(beans: List<UserInfoBean>) {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        val editor = sp.edit()
        editor.remove(USER_ID_SP_KEY)
        editor.remove(PASSWORD_SP_KEY)
        editor.remove(USER_NAME_SP_KEY)
        editor.remove(DEPARTMENT_SP_KEY)
        editor.remove(MAJOR_SP_KEY)
        editor.remove(SAVE_PASSWORD_SP_KEY)
        editor.apply()
    }

}