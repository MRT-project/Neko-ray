package com.neko.v2ray.ui

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.neko.themeengine.ContrastLevel
import com.neko.themeengine.ThemeChooserDialogBuilder
import com.neko.themeengine.ThemeEngine
import com.neko.themeengine.hasS
import com.neko.v2ray.R

class ThemeSettingsFragment : PreferenceFragmentCompat() {

    private lateinit var themeEngine: ThemeEngine
    private var contrastPref: DropDownPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_theme, rootKey)
        themeEngine = ThemeEngine.getInstance(requireContext())
        setupPreferences()
    }

    private fun setupPreferences() {
        setupThemePicker()
        setupContrastPreference()
        setupDarkMode()
        val amoledPref = setupAmoledToggle()
        setupDynamicThemeToggle(amoledPref)
    }

    private fun setupThemePicker() {
        findPreference<Preference>("theme")?.setOnPreferenceClickListener {
            ThemeChooserDialogBuilder(requireContext())
                .setTitle(R.string.choose_theme)
                .setPositiveButton(R.string.ok) { _, theme ->
                    themeEngine.staticTheme = theme
                    requireActivity().recreate()
                }
                .setNegativeButton(R.string.cancel)
                .setNeutralButton(R.string.default_text) { _, _ ->
                    themeEngine.resetTheme()
                    requireActivity().recreate()
                }
                .setIcon(R.drawable.ic_theme_color)
                .create()
                .show()
            true
        }
    }
    
private fun setupContrastPreference() {
    contrastPref = findPreference("contrast_level")

    contrastPref?.apply {
        val currentContrast = themeEngine.staticTheme.contrastLevel.name
        value = currentContrast

        val index = findIndexOfValue(currentContrast)
        if (index >= 0) summary = entries[index]

        setOnPreferenceChangeListener { preference, newValue ->
            val dropdown = preference as DropDownPreference
            val newLevel = ContrastLevel.valueOf(newValue as String)

            themeEngine.switchContrast(newLevel)

            val newIndex = dropdown.findIndexOfValue(newValue)
            if (newIndex >= 0) dropdown.summary = dropdown.entries[newIndex]

            requireActivity().recreate()
            true
        }
    }

    if (themeEngine.isDynamicTheme) {
        contrastPref?.let { preferenceScreen.removePreference(it) }
    }
}

    private fun setupDarkMode() {
        findPreference<DropDownPreference>("dark_mode")?.apply {
            value = themeEngine.themeMode.toString()
            summary = entry

            setOnPreferenceChangeListener { preference, newValue ->
                val dropdown = preference as DropDownPreference
                val index = dropdown.findIndexOfValue(newValue as String)
                themeEngine.themeMode = newValue.toInt()
                dropdown.summary = dropdown.entries[index]
                requireActivity().recreate()
                true
            }
        }
    }

    private fun setupAmoledToggle(): SwitchPreferenceCompat? {
        return findPreference<SwitchPreferenceCompat>("amoled_mode")?.apply {
            isChecked = themeEngine.isTrueBlack

            if (themeEngine.isDynamicTheme) {
                summary = getString(R.string.disable_dynamic_for_amoled)
                disableToggle()
            } else {
                summary = getString(R.string.amoled_description)
                enableAmoledToggle()
            }
        }
    }

    private fun setupDynamicThemeToggle(amoledPref: SwitchPreferenceCompat?) {
        val dynamicPref = findPreference<SwitchPreferenceCompat>("dynamic_theme")

        if (hasS()) {
            dynamicPref?.apply {
                isChecked = themeEngine.isDynamicTheme

                if (themeEngine.isTrueBlack) {
                    summary = getString(R.string.disable_amoled_for_dynamic)
                    disableToggle()
                } else {
                    summary = getString(R.string.dynamic_color_desc)
                    enableDynamicToggle(amoledPref)
                }
            }
        } else {
            listOf(dynamicPref, amoledPref).forEach {
                it?.let { preferenceScreen.removePreference(it) }
            }
        }
    }

    private fun SwitchPreferenceCompat.disableToggle() {
        isEnabled = false
        setOnPreferenceChangeListener { _, _ -> false }
    }

    private fun SwitchPreferenceCompat.enableAmoledToggle() {
        isEnabled = true
        setOnPreferenceChangeListener { _, newValue ->
            themeEngine.isTrueBlack = newValue as Boolean
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().window.decorView.postDelayed({
                requireActivity().recreate()
            }, 400)
            true
        }
    }

    private fun SwitchPreferenceCompat.enableDynamicToggle(amoledPref: SwitchPreferenceCompat?) {
        isEnabled = true
        setOnPreferenceChangeListener { _, newValue ->
            val enabled = newValue as Boolean
            themeEngine.isDynamicTheme = enabled
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().window.decorView.postDelayed({
                requireActivity().recreate()
            }, 700)

            amoledPref?.apply {
                isChecked = false
                isEnabled = !enabled
                summary = if (enabled) getString(R.string.disable_dynamic_for_amoled) else getString(R.string.amoled_description)
                if (enabled) disableToggle() else enableAmoledToggle()
            }

            if (enabled) {
                contrastPref?.let { preferenceScreen.removePreference(it) }
            } else {
                contrastPref?.let {
                    if (preferenceScreen.findPreference<DropDownPreference>("contrast_level") == null) {
                        preferenceScreen.addPreference(it)
                    }
                }
            }
            true
        }
    }
}
