package com.neko.themeengine

import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.R
import com.neko.v2ray.databinding.ItemColorBinding

class ColorAdapter(private val colorArray: List<Int>) :
    RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    var checkedPosition = -1
        private set

    fun setCheckedPosition(theme: Theme) {
        val last = checkedPosition
        checkedPosition = theme.ordinal
        notifyItemChanged(last)
        notifyItemChanged(checkedPosition)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = colorArray[position].toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.colorView.apply {
            setBackgroundColorRes(colorArray[position])
    
            if (checkedPosition == position) {
                setImageResource(R.drawable.uwu_check)
                val typedValue = TypedValue()
                context.theme.resolveAttribute(R.attr.colorText, typedValue, true)
                val color = ContextCompat.getColor(context, typedValue.resourceId)
                imageTintList = ColorStateList.valueOf(color)
            } else {
                setImageResource(0)
                imageTintList = null
            }
        }
    }

    override fun getItemCount() = colorArray.size

    inner class ViewHolder(val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.colorView.setOnClickListener {
                val last = checkedPosition
                checkedPosition = adapterPosition
                notifyItemChanged(last)
                notifyItemChanged(checkedPosition)
            }
        }
    }
}
