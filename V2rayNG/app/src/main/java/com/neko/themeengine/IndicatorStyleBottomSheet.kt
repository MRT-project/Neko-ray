package com.neko.themeengine

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.neko.v2ray.R

class IndicatorStyleBottomSheet(
    private val context: Context,
    private val onSelected: () -> Unit
) {
    fun show() {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_indicator_style, null)
        val recycler = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerStyle)

        val engine = ThemeEngine.getInstance(context)
        val selectedStyle = engine.indicatorStyle

        recycler.layoutManager = GridLayoutManager(context, 2)
        recycler.adapter = IndicatorStyleAdapter(context, selectedStyle) { style ->
            engine.indicatorStyle = style
            onSelected()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
