package com.neko.themeengine

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.BoolRes
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.IntegerRes

val Context.isDarkMode: Boolean
    get() = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES

@ChecksSdkIntAtLeast(api = 31)
fun hasS(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

fun Context.getBooleanSafe(@BoolRes resId: Int, default: Boolean = false): Boolean =
    runCatching { resources.getBoolean(resId) }.getOrElse { default }

fun Context.getIntSafe(@IntegerRes resId: Int, default: Int = 0): Int =
    runCatching { resources.getInteger(resId) }.getOrElse { default }

fun Context.resolveColor(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}