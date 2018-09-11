package com.galaxydl.e_university.utils

import android.support.design.widget.Snackbar
import android.view.View

fun showSnackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}