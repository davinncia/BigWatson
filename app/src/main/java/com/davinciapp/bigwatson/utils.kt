package com.davinciapp.bigwatson

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun <T: View> Activity.bind(@IdRes resId: Int) : Lazy<T> {
    return lazy { findViewById<T>(resId) }
}