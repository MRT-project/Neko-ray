package com.neko.themeengine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.neko.v2ray.R

class ThemeEngine private constructor(private val context: Context) {

    private val prefs = context.getSharedPreferences("theme_engine_prefs", Context.MODE_PRIVATE)

    private var isFirstStart: Boolean
        get() = prefs.getBoolean(KEY_FIRST_START, true)
        set(value) = prefs.edit { putBoolean(KEY_FIRST_START, value) }

    init {
        if (isFirstStart) {
            setDefaultValues()
            isFirstStart = false
        }
    }

    var themeMode: Int
        get() = prefs.getInt(KEY_THEME_MODE, ThemeMode.AUTO)
        set(value) {
            require(value in 0..2) { "Invalid ThemeMode value: $value" }
            prefs.edit { putInt(KEY_THEME_MODE, value) }
            AppCompatDelegate.setDefaultNightMode(
                when (value) {
                    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

    private val nightMode: Int
        get() = when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    var isDynamicTheme: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_THEME, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) = prefs.edit { putBoolean(KEY_DYNAMIC_THEME, value) }

    var staticTheme: Theme
        get() = Theme.values().getOrElse(prefs.getInt(KEY_APP_THEME, Theme.Indigo.ordinal)) { Theme.Indigo }
        set(value) = prefs.edit { putInt(KEY_APP_THEME, value.ordinal) }

    fun getTheme(): Int =
        if (hasS() && isDynamicTheme) R.style.Theme_ThemeEngine_Dynamic else staticTheme.themeId

    fun resetTheme() {
        prefs.edit { remove(KEY_APP_THEME) }
    }

    fun switchContrast(level: ContrastLevel) {
        staticTheme = staticTheme.getContrastTheme(level)
    }

    var isTrueBlack: Boolean
        get() = prefs.getBoolean(KEY_TRUE_BLACK, false)
        set(value) = prefs.edit { putBoolean(KEY_TRUE_BLACK, value) }

    var appFont: AppFont
        get() {
            val name = prefs.getString(KEY_APP_FONT, AppFont.DEFAULT.name)!!
            return AppFont.valueOf(name)
        }
        set(value) {
            prefs.edit { putString(KEY_APP_FONT, value.name) }
        }

    var indicatorStyle: IndicatorStyle
        get() {
            val name = prefs.getString(KEY_INDICATOR_STYLE, IndicatorStyle.UWU.name)!!
            return IndicatorStyle.valueOf(name)
        }
        set(value) {
            prefs.edit { putString(KEY_INDICATOR_STYLE, value.name) }
        }

    private fun setDefaultValues() {
        isTrueBlack = context.getBooleanSafe(R.bool.true_black, false)
        themeMode = context.getIntSafe(R.integer.theme_mode, ThemeMode.AUTO)
        staticTheme = Theme.Indigo
        isDynamicTheme = context.getBooleanSafe(R.bool.dynamic_theme, hasS()) && hasS()
        indicatorStyle = IndicatorStyle.UWU
    }

    companion object {
        private var INSTANCE: ThemeEngine? = null

        fun getInstance(context: Context): ThemeEngine =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemeEngine(context.applicationContext).also { INSTANCE = it }
            }

        fun applyToActivities(app: Application) {
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    applyToActivity(activity)
                }

                override fun onActivityCreated(a: Activity, s: Bundle?) {}
                override fun onActivityStarted(a: Activity) {}
                override fun onActivityResumed(a: Activity) {}
                override fun onActivityPaused(a: Activity) {}
                override fun onActivityStopped(a: Activity) {}
                override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
                override fun onActivityDestroyed(a: Activity) {}
            })
        }

        fun applyToActivity(activity: Activity) {
            val engine = getInstance(activity)
            activity.theme.applyStyle(engine.getTheme(), true)
            if (engine.isTrueBlack) {
                activity.theme.applyStyle(R.style.ThemeOverlay_Black, true)
            }
            AppCompatDelegate.setDefaultNightMode(engine.nightMode)
        }

        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_DYNAMIC_THEME = "dynamic_theme"
        private const val KEY_APP_THEME = "app_theme"
        private const val KEY_TRUE_BLACK = "true_black"
        private const val KEY_FIRST_START = "first_start"
        private const val KEY_APP_FONT = "app_font"
        private const val KEY_INDICATOR_STYLE = "indicator_style"
    }
}
