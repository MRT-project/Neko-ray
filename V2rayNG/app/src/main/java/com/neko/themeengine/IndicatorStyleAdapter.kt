package com.neko.themeengine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.R

class IndicatorStyleAdapter(
    private val context: Context,
    private val selected: IndicatorStyle,
    private val onSelect: (IndicatorStyle) -> Unit
) : RecyclerView.Adapter<IndicatorStyleAdapter.ViewHolder>() {

    private val styles = IndicatorStyle.values()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.imagePreview)
        val check = view.findViewById<ImageView>(R.id.imageCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_indicator_style, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = styles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val style = styles[position]
        holder.image.setImageResource(style.drawableRes)
        holder.check.visibility = if (style == selected) View.VISIBLE else View.GONE
        holder.view.setOnClickListener {
            onSelect(style)
        }
    }
}
