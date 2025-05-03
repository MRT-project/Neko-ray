package com.neko.expandable.layout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView

class ExpandableMainLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), View.OnClickListener {
    
    private val animationDuration = 300L
    
    private val arrowIcon: ImageView by lazy { findViewById(getResourceId("id/arrow_button")) }
    private val cardbg: MaterialCardView by lazy { findViewById(getResourceId("id/card_bg")) }
    private val expandableContent: ExpandableView by lazy { findViewById(getResourceId("id/expandable_view")) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        listOf(arrowIcon, cardbg).forEach { it.setOnClickListener(this) }
        initializeLogic()
    }

    override fun onClick(view: View) {
        toggleExpansion()
    }

    private fun toggleExpansion() {
        expandableContent.apply {
            if (isExpanded) collapse() else expand()
            orientation = ExpandableView.VERTICAL
        }
        arrowIcon.animate().setDuration(animationDuration)
            .rotation(if (expandableContent.isExpanded) 90.0f else 0.0f)
    }

    private fun initializeLogic() {
        expandableContent.setExpansion(0f)
        arrowIcon.apply {
            background = RippleDrawable(ColorStateList.valueOf(-0x8a8a8b), null, null)
            isClickable = true
        }
    }

    private fun getResourceId(name: String): Int {
        return context.resources.getIdentifier(name, "id", context.packageName)
    }
}
