package com.neko.v2ray.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neko.themeengine.hasS
import com.neko.themeengine.ThemeChooserDialogBuilder
import com.neko.themeengine.ThemeEngine
import com.neko.themeengine.ThemeMode
import com.neko.v2ray.R
import com.neko.v2ray.databinding.FragmentSettingsThemeBinding

/**
 * A BottomSheetDialogFragment that allows the user to change the app's theme settings.
 * Supports theme mode (light, dark, auto), dynamic colors (on Android 12+),
 * and custom theme selection using ThemeChooserDialog.
 */
class SettingsFragmentTheme : BottomSheetDialogFragment() {

    private lateinit var themeEngine: ThemeEngine

    private var _binding: FragmentSettingsThemeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ThemeEngine and inflate the layout
        themeEngine = ThemeEngine.getInstance(requireContext())
        _binding = FragmentSettingsThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Handle dynamic color options if device supports them (Android 12+)
        if (hasS()) {
            // Check appropriate radio button based on current dynamic theme setting
            binding.dynamicGroup.check(
                if (themeEngine.isDynamicTheme) R.id.dynamic_on else R.id.dynamic_off
            )

            // Listener for dynamic theme toggle
            binding.dynamicGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.dynamic_off -> themeEngine.isDynamicTheme = false
                        R.id.dynamic_on -> themeEngine.isDynamicTheme = true
                    }
                    // Recreate activity to apply changes
                    requireActivity().recreate()
                }
            }
        } else {
            // Hide dynamic theme options if not supported
            binding.dynamicColorLabel.isVisible = false
            binding.dynamicGroup.isVisible = false
        }

        // Set current theme mode (Auto, Light, Dark)
        binding.themeGroup.check(
            when (themeEngine.themeMode) {
                ThemeMode.AUTO -> R.id.auto_theme
                ThemeMode.LIGHT -> R.id.light_theme
                ThemeMode.DARK -> R.id.dark_theme
                else -> R.id.auto_theme
            }
        )

        // Listener for theme mode changes
        binding.themeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                themeEngine.themeMode = when (checkedId) {
                    R.id.auto_theme -> ThemeMode.AUTO
                    R.id.light_theme -> ThemeMode.LIGHT
                    R.id.dark_theme -> ThemeMode.DARK
                    else -> ThemeMode.AUTO
                }
            }
        }

        // Launch the custom theme chooser dialog when button is clicked
        binding.changeTheme.setOnClickListener {
            ThemeChooserDialogBuilder(requireContext())
                .setTitle(R.string.title_choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    themeEngine.staticTheme = theme
                    requireActivity().recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    themeEngine.resetTheme()
                    requireActivity().recreate()
                }
                .setIcon(R.drawable.ic_round_brush)
                .create()
                .show()
        }
    }
}
