package com.neko.v2ray.ui

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.neko.v2ray.R

class ThemeSettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_settings_activity)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val toolbarLayout: CollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, ThemeSettingsFragment())
            .commit()
    }
}
