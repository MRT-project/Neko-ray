package com.neko.expandable.layout

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout

class ExpandableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    interface State {
        companion object {
            const val COLLAPSED = 0
            const val COLLAPSING = 1
            const val EXPANDING = 2
            const val EXPANDED = 3
        }
    }

    interface OnExpansionUpdateListener {
        fun onExpansionUpdate(expansionFraction: Float, state: Int)
    }

    companion object {
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANSION = "expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val DEFAULT_DURATION = 1000
    }

    private val ff = floatArrayOf(
        0.0000f, 0.0001f, 0.0002f, 0.0005f, 0.0009f, 0.0014f, 0.0020f,
        0.0027f, 0.0036f, 0.0046f, 0.0058f, 0.0071f, 0.0085f, 0.0101f,
        0.0118f, 0.0137f, 0.0158f, 0.0180f, 0.0205f, 0.0231f, 0.0259f,
        0.0289f, 0.0321f, 0.0355f, 0.0391f, 0.0430f, 0.0471f, 0.0514f,
        0.0560f, 0.0608f, 0.0660f, 0.0714f, 0.0771f, 0.0830f, 0.0893f,
        0.0959f, 0.1029f, 0.1101f, 0.1177f, 0.1257f, 0.1339f, 0.1426f,
        0.1516f, 0.1610f, 0.1707f, 0.1808f, 0.1913f, 0.2021f, 0.2133f,
        0.2248f, 0.2366f, 0.2487f, 0.2611f, 0.2738f, 0.2867f, 0.2998f,
        0.3131f, 0.3265f, 0.3400f, 0.3536f, 0.3673f, 0.3810f, 0.3946f,
        0.4082f, 0.4217f, 0.4352f, 0.4485f, 0.4616f, 0.4746f, 0.4874f,
        0.5000f, 0.5124f, 0.5246f, 0.5365f, 0.5482f, 0.5597f, 0.5710f,
        0.5820f, 0.5928f, 0.6033f, 0.6136f, 0.6237f, 0.6335f, 0.6431f,
        0.6525f, 0.6616f, 0.6706f, 0.6793f, 0.6878f, 0.6961f, 0.7043f,
        0.7122f, 0.7199f, 0.7275f, 0.7349f, 0.7421f, 0.7491f, 0.7559f,
        0.7626f, 0.7692f, 0.7756f, 0.7818f, 0.7879f, 0.7938f, 0.7996f,
        0.8053f, 0.8108f, 0.8162f, 0.8215f, 0.8266f, 0.8317f, 0.8366f,
        0.8414f, 0.8461f, 0.8507f, 0.8551f, 0.8595f, 0.8638f, 0.8679f,
        0.8720f, 0.8760f, 0.8798f, 0.8836f, 0.8873f, 0.8909f, 0.8945f,
        0.8979f, 0.9013f, 0.9046f, 0.9078f, 0.9109f, 0.9139f, 0.9169f,
        0.9198f, 0.9227f, 0.9254f, 0.9281f, 0.9307f, 0.9333f, 0.9358f,
        0.9382f, 0.9406f, 0.9429f, 0.9452f, 0.9474f, 0.9495f, 0.9516f,
        0.9536f, 0.9556f, 0.9575f, 0.9594f, 0.9612f, 0.9629f, 0.9646f,
        0.9663f, 0.9679f, 0.9695f, 0.9710f, 0.9725f, 0.9739f, 0.9753f,
        0.9766f, 0.9779f, 0.9791f, 0.9803f, 0.9815f, 0.9826f, 0.9837f,
        0.9848f, 0.9858f, 0.9867f, 0.9877f, 0.9885f, 0.9894f, 0.9902f,
        0.9910f, 0.9917f, 0.9924f, 0.9931f, 0.9937f, 0.9944f, 0.9949f,
        0.9955f, 0.9960f, 0.9964f, 0.9969f, 0.9973f, 0.9977f, 0.9980f,
        0.9984f, 0.9986f, 0.9989f, 0.9991f, 0.9993f, 0.9995f, 0.9997f,
        0.9998f, 0.9999f, 0.9999f, 1.0000f, 1.0000f
    )

    private var duration = DEFAULT_DURATION
    private var parallax = 0f
    private var expansion = 0f
    public var orientation = VERTICAL
    private var state = State.COLLAPSED
    private var animator: ValueAnimator? = null
    private var interpolator: Interpolator = FastOutSlowInInterpolator(ff)
    private var listener: OnExpansionUpdateListener? = null

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putFloat(KEY_EXPANSION, if (isExpanded) 1f else 0f)
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        this.state = if (expansion == 1f) State.EXPANDED else State.COLLAPSED
        super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = if (orientation == HORIZONTAL) width else height

        visibility = if (expansion == 0f && size == 0) GONE else VISIBLE

        val expansionDelta = size - (size * expansion).toInt()
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    child.translationX = -parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }

        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    fun setOrientatin(orientation: Int) {
        this.orientation = orientation
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        animator?.cancel()
        super.onConfigurationChanged(newConfig)
    }

    val isExpanded: Boolean
        get() = state == State.EXPANDING || state == State.EXPANDED

    fun toggle(animate: Boolean = true) {
        if (isExpanded) collapse(animate) else expand(animate)
    }

    fun expand(animate: Boolean = true) = setExpanded(true, animate)

    fun collapse(animate: Boolean = true) = setExpanded(false, animate)

    fun setExpanded(expand: Boolean, animate: Boolean = true) {
        if (expand == isExpanded) return
        val targetExpansion = if (expand) 1f else 0f
        if (animate) animateSize(targetExpansion) else setExpansion(targetExpansion)
    }

    var expansionFraction: Float
        get() = expansion
        set(value) = setExpansion(value)

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) return

        val delta = expansion - this.expansion
        state = when {
            expansion == 0f -> State.COLLAPSED
            expansion == 1f -> State.EXPANDED
            delta < 0 -> State.COLLAPSING
            else -> State.EXPANDING
        }

        visibility = if (state == State.COLLAPSED) GONE else VISIBLE
        this.expansion = expansion
        requestLayout()
        listener?.onExpansionUpdate(expansion, state)
    }

    fun setExpansionInstant(yes: Boolean) {
        expansion = if (yes) 1f else 0f
        state = if (yes) State.EXPANDED else State.COLLAPSED
        visibility = if (!yes) GONE else VISIBLE
        requestLayout()
        listener?.onExpansionUpdate(expansion, state)
    }

    fun setParallax(parallax: Float) {
        this.parallax = parallax.coerceIn(0f, 1f)
    }

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener?) {
        this.listener = listener
    }

    private fun animateSize(targetExpansion: Float) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(expansion, targetExpansion).apply {
            interpolator = this@ExpandableView.interpolator
            duration = this@ExpandableView.duration.toLong()

            addUpdateListener {
                setExpansion(it.animatedValue as Float)
            }

            addListener(object : Animator.AnimatorListener {
                var canceled = false

                override fun onAnimationStart(animation: Animator) {
                    state = if (targetExpansion == 0f) State.COLLAPSING else State.EXPANDING
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!canceled) {
                        state = if (targetExpansion == 0f) State.COLLAPSED else State.EXPANDED
                        setExpansion(targetExpansion)
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    canceled = true
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })

            start()
        }
    }

    open class FastOutSlowInInterpolator(values: FloatArray) : LookupTableInterpolator(values)

    open class LookupTableInterpolator(private val mValues: FloatArray) : Interpolator {
        private val mStepSize = 1f / (mValues.size - 1)

        override fun getInterpolation(input: Float): Float {
            if (input >= 1f) return 1f
            if (input <= 0f) return 0f

            val position = (input * (mValues.size - 1)).toInt().coerceAtMost(mValues.size - 2)
            val quantized = position * mStepSize
            val diff = input - quantized
            val weight = diff / mStepSize

            return mValues[position] + weight * (mValues[position + 1] - mValues[position])
        }
    }
}
