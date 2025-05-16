package com.neko.themeengine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.R
import com.neko.v2ray.databinding.ItemColorBinding

class ColorAdapter(private val themes: List<Theme>) :
    RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    var checkedPosition = -1
        private set

    private var onItemSelected: ((Int) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    fun setCheckedPosition(theme: Theme) {
        val last = checkedPosition
        checkedPosition = themes.indexOfFirst { it == theme }
        notifyItemChanged(last)
        notifyItemChanged(checkedPosition)
    }

    fun setOnItemSelectedListener(listener: (Int) -> Unit) {
        onItemSelected = listener
    }

    override fun getItemId(position: Int) = themes[position].ordinal.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val theme = themes[position]
            colorView.setBackgroundColorRes(theme.primaryColor)

            val isChecked = checkedPosition == position
            val shouldShowCheck = isChecked && theme.contrastLevel == ContrastLevel.DEFAULT
            cardView.isChecked = shouldShowCheck

            val colorSurface = cardView.context.resolveColor(com.google.android.material.R.attr.colorSurface)
            if (shouldShowCheck) {
                cardView.checkedIcon?.setTint(colorSurface)
            } else {
                cardView.checkedIcon?.setTintList(null)
            }
        }
    }

    override fun getItemCount() = themes.size

    inner class ViewHolder(val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {
                val previous = checkedPosition
                checkedPosition = bindingAdapterPosition
                notifyItemChanged(previous)
                notifyItemChanged(checkedPosition)
                onItemSelected?.invoke(checkedPosition)
            }
        }
    }
}
