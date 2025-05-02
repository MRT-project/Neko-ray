package com.neko.themeengine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.neko.v2ray.R

class ThemeEngine private constructor(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var isFirstStart: Boolean
        get() = prefs.getBoolean(KEY_FIRST_START, true)
        set(value) = prefs.edit { putBoolean(KEY_FIRST_START, value) }

    init {
        if (isFirstStart) {
            applyDefaultValues(context)
            isFirstStart = false
        }
    }

    var themeMode: Int
        get() = prefs.getInt(KEY_THEME_MODE, ThemeMode.AUTO)
        set(value) {
            require(value in ThemeMode.AUTO..ThemeMode.DARK) {
                "Invalid theme mode. Use ThemeMode.AUTO, LIGHT, or DARK"
            }
            prefs.edit { putInt(KEY_THEME_MODE, value) }
            AppCompatDelegate.setDefaultNightMode(
                when (value) {
                    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

    var isDynamicTheme: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_THEME, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) = prefs.edit { putBoolean(KEY_DYNAMIC_THEME, value) }

    var staticTheme: Theme
        get() = Theme.values()[prefs.getInt(KEY_STATIC_THEME, Theme.Blue.ordinal)]
        set(value) = prefs.edit { putInt(KEY_STATIC_THEME, value.ordinal) }

    fun getTheme(): Int {
        return if (hasS() && isDynamicTheme) R.style.Theme_ThemeEngine_Dynamic
        else staticTheme.themeId
    }

    fun resetTheme() {
        prefs.edit { remove(KEY_STATIC_THEME) }
    }

    private fun applyDefaultValues(context: Context) {
        themeMode = context.getIntSafe(R.integer.theme_mode, ThemeMode.AUTO)
        staticTheme = Theme.values()[context.getIntSafe(R.integer.static_theme, Theme.Blue.ordinal)]
        isDynamicTheme = context.getBooleanSafe(R.bool.dynamic_theme, hasS()) && hasS()
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemeEngine? = null

        fun getInstance(context: Context): ThemeEngine {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemeEngine(context.applicationContext).also { INSTANCE = it }
            }
        }

        fun applyToActivities(application: Application) {
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    applyToActivity(activity)
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }

        fun applyToActivity(activity: Activity) {
            with(getInstance(activity)) {
                activity.theme.applyStyle(getTheme(), true)
                AppCompatDelegate.setDefaultNightMode(
                    when (themeMode) {
                        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
            }
        }

        private const val PREFS_NAME = "theme_engine_prefs"
        private const val KEY_FIRST_START = "first_start"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_DYNAMIC_THEME = "dynamic_theme"
        private const val KEY_STATIC_THEME = "app_theme"
    }
}
