package com.galaxydl.e_university.data.bean

data class UserInfoBean(val userID: String,
                        val password: String,
                        val userName: String,
                        val department: String,
                        val major: String,
                        val savePassword: Boolean) : BaseBean()