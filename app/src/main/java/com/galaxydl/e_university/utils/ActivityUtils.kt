package com.galaxydl.e_university.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.replaceFragmentInActivity(
        fm: android.support.v4.app.FragmentManager,
        fragment: Fragment,
        frameId: Int) {
    val transaction = fm.beginTransaction()
    transaction.replace(frameId, fragment)
    transaction.commit()
}

fun <T : ViewModel> obtainViewModel(
        activity: FragmentActivity,
        clazz: Class<T>): T = ViewModelProviders
        .of(activity, ViewModelFactory.getInstance(activity.application))
        .get(clazz)
