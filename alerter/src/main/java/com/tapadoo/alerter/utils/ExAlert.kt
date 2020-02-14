package com.tapadoo.alerter.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import com.tapadoo.alerter.Alert

fun Alert.getDimenPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

@RequiresApi(Build.VERSION_CODES.P)
fun Alert.notchHeight() = (context as? Activity)?.window?.decorView?.rootWindowInsets?.displayCutout?.safeInsetTop
        ?: 0