package com.neko.v2ray.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.handler.SettingsManager
import com.neko.v2ray.helper.CustomDividerItemDecoration
import com.neko.v2ray.util.MyContextWrapper
import com.neko.themeengine.AppFont
import com.neko.themeengine.FontContextWrapper
import com.neko.themeengine.Theme
import com.neko.themeengine.ThemeEngine

abstract class BaseActivity : AppCompatActivity() {

    private var lastKnownTheme: Theme? = null
    private var lastKnownNightMode: Int = -1
    private var lastKnownDynamic: Boolean = false
    private var lastKnownTrueBlack: Boolean = false
    private var lastKnownFont: AppFont? = null

    override fun attachBaseContext(newBase: Context) {
        val localeWrapped = MyContextWrapper.wrap(newBase, SettingsManager.getLocale())
        val fontWrapped = FontContextWrapper.wrap(localeWrapped)
        super.attachBaseContext(fontWrapped)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeEngine.applyToActivity(this)

        val engine = ThemeEngine.getInstance(this)
        lastKnownTheme = engine.staticTheme
        lastKnownNightMode = engine.themeMode
        lastKnownDynamic = engine.isDynamicTheme
        lastKnownTrueBlack = engine.isTrueBlack
        lastKnownFont = engine.appFont
    }

    override fun onResume() {
        super.onResume()
        val engine = ThemeEngine.getInstance(this)

        val themeChanged = engine.staticTheme != lastKnownTheme
        val modeChanged = engine.themeMode != lastKnownNightMode
        val dynamicChanged = engine.isDynamicTheme != lastKnownDynamic
        val trueBlackChanged = engine.isTrueBlack != lastKnownTrueBlack
        val fontChanged = engine.appFont != lastKnownFont

        if (themeChanged || modeChanged || dynamicChanged || trueBlackChanged || fontChanged) {
            recreate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressedDispatcher.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun addCustomDividerToRecyclerView(
        recyclerView: RecyclerView,
        context: Context?,
        drawableResId: Int,
        orientation: Int = DividerItemDecoration.VERTICAL
    ) {
        val drawable = ContextCompat.getDrawable(context!!, drawableResId)
        requireNotNull(drawable) { "Drawable resource not found" }
        val dividerItemDecoration = CustomDividerItemDecoration(drawable, orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
}
