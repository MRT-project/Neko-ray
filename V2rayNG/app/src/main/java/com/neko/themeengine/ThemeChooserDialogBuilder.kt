package com.neko.themeengine

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.neko.v2ray.databinding.RecyclerviewBinding

class ThemeChooserDialogBuilder(private val context: Context) {

    private val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
    private val themes = Theme.values().filter { it.contrastLevel == ContrastLevel.DEFAULT }
    private val colorAdapter = ColorAdapter(themes)

    init {
        val binding = RecyclerviewBinding.inflate(LayoutInflater.from(context))
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = colorAdapter
        }

        ThemeEngine.getInstance(context).staticTheme.let {
            colorAdapter.setCheckedPosition(it)
        }

        builder.setView(binding.root)
    }

    fun setTitle(@StringRes resId: Int): ThemeChooserDialogBuilder = apply {
        builder.setTitle(resId)
    }

    fun setIcon(@DrawableRes resId: Int): ThemeChooserDialogBuilder = apply {
        builder.setIcon(resId)
    }

    fun setPositiveButton(text: String, listener: OnClickListener): ThemeChooserDialogBuilder = apply {
        builder.setPositiveButton(text) { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                val pos = colorAdapter.checkedPosition
                if (pos in themes.indices) {
                    listener.onClick(pos, themes[pos])
                    if (context is android.app.Activity) context.recreate()
                }
            }
        }
    }

    fun setPositiveButton(@StringRes resId: Int, listener: OnClickListener): ThemeChooserDialogBuilder =
        setPositiveButton(context.getString(resId), listener)

    fun setNegativeButton(text: String): ThemeChooserDialogBuilder = apply {
        builder.setNegativeButton(text, null)
    }

    fun setNegativeButton(@StringRes resId: Int): ThemeChooserDialogBuilder = apply {
        builder.setNegativeButton(resId, null)
    }

    fun setNeutralButton(text: String, listener: OnClickListener): ThemeChooserDialogBuilder = apply {
        builder.setNeutralButton(text) { _, which ->
            if (which == DialogInterface.BUTTON_NEUTRAL) {
                val pos = colorAdapter.checkedPosition
                if (pos in themes.indices) {
                    listener.onClick(pos, themes[pos])
                }
            }
        }
    }

    fun setNeutralButton(@StringRes resId: Int, listener: OnClickListener): ThemeChooserDialogBuilder =
        setNeutralButton(context.getString(resId), listener)

    fun create(): AlertDialog = builder.create()

    fun interface OnClickListener {
        fun onClick(position: Int, theme: Theme)
    }
}
