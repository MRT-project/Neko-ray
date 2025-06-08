package com.neko.themeengine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.R

class IndicatorStyleAdapter(
    private val context: Context,
    private val selected: IndicatorStyle,
    private val onSelect: (IndicatorStyle) -> Unit
) : RecyclerView.Adapter<IndicatorStyleAdapter.ViewHolder>() {

    private val styles = IndicatorStyle.values()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<CardView>(R.id.cardImage)
        val container = view.findViewById<LinearLayout>(R.id.imagePreviewContainer)
        val check = view.findViewById<ImageView>(R.id.imageCheck)
        val overlay = view.findViewById<LinearLayout>(R.id.overlayContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_indicator_style, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = styles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val style = styles[position]

        // Set background sebagai preview style
        holder.container.background = ContextCompat.getDrawable(context, style.drawableRes)

        // Tampilkan centang kalau terpilih
        val isSelected = style == selected
        holder.check.visibility = if (isSelected) View.VISIBLE else View.GONE

        // (Opsional) Tambahkan overlay item di sini kalau ingin icon/text:
        // holder.overlay.addView(TextView(context).apply {
        //     text = "Nama Gaya"
        //     setTextColor(Color.WHITE)
        // })

        holder.view.setOnClickListener {
            onSelect(style)
        }
    }
}
