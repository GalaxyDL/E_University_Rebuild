package com.galaxydl.e_university.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.ArrayMap

object LiveDataEventBus {

    private val buses: MutableMap<String, BusLiveData<Any>>
            by lazy { ArrayMap<String, BusLiveData<Any>>() }

    fun get(key: String): MutableLiveData<Any> = get(key, Any::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, clazz: Class<T>): MutableLiveData<T> {
        if (!buses.containsKey(key)) {
            buses[key] = BusLiveData(key)
        }
        return buses[key]!! as MutableLiveData<T>
    }

    private class BusLiveData<T>(private val mEventKey: String) : MutableLiveData<T>() {

        var needNotify = false
            private set(v) {
                field = v
            }

        override fun setValue(value: T) {
            needNotify = true
            super.setValue(value)
        }

        override fun postValue(value: T) {
            needNotify = true
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            super.observe(owner, BusObserver(observer, this))
        }

    }

    private class BusObserver<T>(private val mObserver: Observer<T>,
                                 private val mLiveData: BusLiveData<T>) : Observer<T> {

        override fun onChanged(t: T?) {
            if (mLiveData.needNotify) {
                if (t != null) {
                    mObserver.onChanged(t)
                }
            }
        }

    }
}