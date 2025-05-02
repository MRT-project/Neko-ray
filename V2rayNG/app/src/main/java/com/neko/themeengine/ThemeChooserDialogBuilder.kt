package com.neko.themeengine

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.neko.v2ray.databinding.RecyclerviewBinding

class ThemeChooserDialogBuilder(private val context: Context) {

    private val themes = Theme.values()
    private val colorAdapter = ColorAdapter(themes.map { it.primaryColor })

    private val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context).apply {
        val binding = RecyclerviewBinding.inflate(LayoutInflater.from(context)).apply {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
            recyclerView.adapter = colorAdapter
        }
        setView(binding.root)
    }

    init {
        colorAdapter.setCheckedPosition(ThemeEngine.getInstance(context).staticTheme)
    }

    fun setTitle(@StringRes res: Int) = apply { builder.setTitle(res) }

    fun setIcon(@DrawableRes iconId: Int) = apply { builder.setIcon(iconId) }

    fun setPositiveButton(text: String, listener: OnClickListener) = apply {
        builder.setPositiveButton(text) { _, _ ->
            val pos = colorAdapter.checkedPosition
            listener.onClick(pos, themes[pos])
        }
    }

    fun setPositiveButton(@StringRes res: Int, listener: OnClickListener) =
        setPositiveButton(context.getString(res), listener)

    fun setNegativeButton(text: String) = apply { builder.setNegativeButton(text, null) }

    fun setNegativeButton(@StringRes res: Int) =
        apply { builder.setNegativeButton(res, null) }

    fun setNeutralButton(text: String, listener: OnClickListener) = apply {
        builder.setNeutralButton(text) { _, _ ->
            val pos = colorAdapter.checkedPosition
            listener.onClick(pos, themes[pos])
        }
    }

    fun setNeutralButton(@StringRes res: Int, listener: OnClickListener) =
        setNeutralButton(context.getString(res), listener)

    fun create(): AlertDialog = builder.create()

    fun interface OnClickListener {
        fun onClick(position: Int, theme: Theme)
    }
}
