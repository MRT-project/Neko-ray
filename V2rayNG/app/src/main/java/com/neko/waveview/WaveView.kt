package com.neko.waveview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.neko.v2ray.R
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

class WaveView : View {

    companion object {
        private const val DEFAULT_NUMBER_OF_WAVES = 3
        private const val DEFAULT_FREQUENCY = 2.0f
        private const val DEFAULT_AMPLITUDE = 0.15f
        private const val DEFAULT_PHASE_SHIFT = -0.05f
        private const val DEFAULT_DENSITY = 5.0f
        private const val DEFAULT_PRIMARY_LINE_WIDTH = 3.0f
        private const val DEFAULT_SECONDARY_LINE_WIDTH = 1.0f
        private const val DEFAULT_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_WAVE_COLOR = Color.WHITE
        private const val DEFAULT_X_AXIS_POSITION_MULTIPLIER = 0.5f
    }

    private var numberOfWaves = DEFAULT_NUMBER_OF_WAVES
    private var phase = 0f
    private var amplitude = DEFAULT_AMPLITUDE
    private var frequency = DEFAULT_FREQUENCY
    private var phaseShift = DEFAULT_PHASE_SHIFT
    private var density = DEFAULT_DENSITY
    private var primaryWaveLineWidth = DEFAULT_PRIMARY_LINE_WIDTH
    private var secondaryWaveLineWidth = DEFAULT_SECONDARY_LINE_WIDTH
    private var backgroundColor = DEFAULT_BACKGROUND_COLOR
    private var waveColor = DEFAULT_WAVE_COLOR
    private var xAxisPositionMultiplier = DEFAULT_X_AXIS_POSITION_MULTIPLIER

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = waveColor
    }

    private var path: Path = Path()

    private val isPlaying = AtomicBoolean(true)

    constructor(context: Context) : super(context) {
        setUp(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        try {
            setUp(a)
        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        try {
            setUp(a)
        } finally {
            a.recycle()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        try {
            setUp(a)
        } finally {
            a.recycle()
        }
    }

    private fun setUp(typedArray: TypedArray?) {
        if (typedArray != null) {
            numberOfWaves = typedArray.getInt(R.styleable.WaveView_waveNumberOfWaves, DEFAULT_NUMBER_OF_WAVES)
            frequency = typedArray.getFloat(R.styleable.WaveView_waveFrequency, DEFAULT_FREQUENCY)
            amplitude = typedArray.getFloat(R.styleable.WaveView_waveAmplitude, DEFAULT_AMPLITUDE)
            phaseShift = typedArray.getFloat(R.styleable.WaveView_wavePhaseShift, DEFAULT_PHASE_SHIFT)
            density = typedArray.getFloat(R.styleable.WaveView_waveDensity, DEFAULT_DENSITY)
            primaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_wavePrimaryLineWidth, DEFAULT_PRIMARY_LINE_WIDTH)
            secondaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_waveSecondaryLineWidth, DEFAULT_SECONDARY_LINE_WIDTH)
            backgroundColor = typedArray.getColor(R.styleable.WaveView_waveBackgroundColor, DEFAULT_BACKGROUND_COLOR)
            waveColor = typedArray.getColor(R.styleable.WaveView_waveColor, DEFAULT_WAVE_COLOR)
            xAxisPositionMultiplier = typedArray.getFloat(R.styleable.WaveView_waveXAxisPositionMultiplier, DEFAULT_X_AXIS_POSITION_MULTIPLIER)
        }
        boundXAxisPositionMultiplier()
        initPaintPath()
    }

    private fun setUpWithBuilder(builder: Builder) {
        numberOfWaves = builder.numberOfWaves
        frequency = builder.frequency
        amplitude = builder.amplitude
        phase = builder.phase
        phaseShift = builder.phaseShift
        density = builder.density
        primaryWaveLineWidth = builder.primaryWaveLineWidth
        secondaryWaveLineWidth = builder.secondaryWaveLineWidth
        backgroundColor = builder.backgroundColor
        waveColor = builder.waveColor
        xAxisPositionMultiplier = builder.xAxisPositionMultiplier
        initPaintPath()
    }

    private fun initPaintPath() {
        paint.color = waveColor
        path = Path()
    }

    private fun boundXAxisPositionMultiplier() {
        xAxisPositionMultiplier = xAxisPositionMultiplier.coerceIn(0f, 1f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)

        val xAxisPosition = canvas.height * xAxisPositionMultiplier
        val width = canvas.width.toFloat()
        val mid = width / 2

        for (i in 0 until numberOfWaves) {
            paint.strokeWidth = if (i == 0) primaryWaveLineWidth else secondaryWaveLineWidth
            val progress = 1.0f - i.toFloat() / numberOfWaves
            val normedAmplitude = (1.5f * progress - 0.5f) * amplitude

            path.reset()
            var x = 0f
            while (x < width + density) {
                val scaling = (-((1 / mid) * (x - mid)).pow(2) + 1).toFloat()
                val y = (scaling * amplitude * normedAmplitude *
                        sin(2 * PI * (x / width) * frequency + phase * (i + 1)) +
                        xAxisPosition).toFloat()
                if (x == 0f) path.moveTo(x, y) else path.lineTo(x, y)
                x += density
            }

            path.lineTo(width, canvas.height.toFloat())
            path.lineTo(0f, canvas.height.toFloat())
            path.close()

            paint.alpha = if (i == 0) 255 else 255 / (i + 1)
            canvas.drawPath(path, paint)
        }

        if (isPlaying.get()) {
            phase += phaseShift
        }

        invalidate()
    }

    fun isPlaying(): Boolean = isPlaying.get()

    fun play() {
        isPlaying.set(true)
    }

    fun pause() {
        isPlaying.set(false)
    }

    class Builder(private val context: Context) {
        var numberOfWaves = DEFAULT_NUMBER_OF_WAVES
        var phase = 0f
        var amplitude = DEFAULT_AMPLITUDE
        var frequency = DEFAULT_FREQUENCY
        var phaseShift = DEFAULT_PHASE_SHIFT
        var density = DEFAULT_DENSITY
        var primaryWaveLineWidth = DEFAULT_PRIMARY_LINE_WIDTH
        var secondaryWaveLineWidth = DEFAULT_SECONDARY_LINE_WIDTH
        var backgroundColor = DEFAULT_BACKGROUND_COLOR
        var waveColor = DEFAULT_WAVE_COLOR
        var xAxisPositionMultiplier = DEFAULT_X_AXIS_POSITION_MULTIPLIER

        fun numberOfWaves(value: Int) = apply { numberOfWaves = value }
        fun phase(value: Float) = apply { phase = value }
        fun waveAmplitude(value: Float) = apply { amplitude = value }
        fun waveFrequency(value: Float) = apply { frequency = value }
        fun wavePhaseShift(value: Float) = apply { phaseShift = value }
        fun waveDensity(value: Float) = apply { density = value }
        fun primaryWaveLineWidth(value: Float) = apply { primaryWaveLineWidth = value }
        fun secondaryWaveLineWidth(value: Float) = apply { secondaryWaveLineWidth = value }
        fun waveBackgroundColor(value: Int) = apply { backgroundColor = value }
        fun waveColor(value: Int) = apply { waveColor = value }
        fun xAxisPositionMultiplier(value: Float) = apply { xAxisPositionMultiplier = value }

        fun build(): WaveView {
            val view = WaveView(context)
            view.setUpWithBuilder(this)
            return view
        }
    }
}
