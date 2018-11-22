package com.galaxydl.e_university.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.annotation.StringRes

class SnackbarMessage : SingleLiveEvent<Int>() {

    fun observe(owner: LifecycleOwner, observer: (Int) -> Unit) {
        super.observe(owner, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }

    fun show(@StringRes id: Int) {
        value = id
    }

}