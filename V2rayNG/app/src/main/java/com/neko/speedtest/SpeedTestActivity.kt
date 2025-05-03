package com.neko.speedtest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.github.anastr.speedviewlib.PointerSpeedometer
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.neko.v2ray.R
import com.neko.v2ray.ui.BaseActivity
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.InetAddress
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

class SpeedTestActivity : BaseActivity() {

    private lateinit var speedometer: PointerSpeedometer
    private lateinit var textPing: TextView
    private lateinit var textJitter: TextView
    private lateinit var textDownload: TextView
    private lateinit var textUpload: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var testJob: Job? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uwu_speedtest)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        speedometer = findViewById(R.id.speedometer)
        textPing = findViewById(R.id.textPing)
        textJitter = findViewById(R.id.textJitter)
        textDownload = findViewById(R.id.textDownload)
        textUpload = findViewById(R.id.textUpload)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        startButton.setOnClickListener { startSpeedTest() }
        stopButton.setOnClickListener { stopSpeedTest() }

        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun startSpeedTest() {
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Please check your connection internet", Toast.LENGTH_SHORT).show()
            return
        }

        startButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE

        textPing.text = "Testing..."
        textJitter.text = "Testing..."
        textDownload.text = "Testing..."
        textUpload.text = "Testing..."
        speedometer.visibility = View.VISIBLE
        speedometer.alpha = 1f
        speedometer.speedTo(0f)

        testJob = scope.launch {
            try {
                val (ping, jitter) = measurePingAndJitter("8.8.8.8", 5)
                textPing.text = "$ping ms"
                textJitter.text = "$jitter ms"

                val downloadSpeed = measureDownloadSpeed()
                textDownload.text = "%.2f Mbps".format(downloadSpeed)

                delay(1500)

                val uploadSpeed = measureUploadSpeed()
                textUpload.text = "%.2f Mbps".format(uploadSpeed)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SpeedTestActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    textPing.text = "Error"
                    textJitter.text = "Error"
                    textDownload.text = "Error"
                    textUpload.text = "Error"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    speedometer.animate().alpha(0f).setDuration(600).withEndAction {
                        speedometer.visibility = View.GONE
                    }.start()

                    startButton.visibility = View.VISIBLE
                    stopButton.visibility = View.GONE
                }
            }
        }
    }

    private fun stopSpeedTest() {
        testJob?.cancel()
        testJob = null
        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE
    }

    private suspend fun measureDownloadSpeed(): Double = withContext(Dispatchers.IO) {
        val url = "https://github.com/topjohnwu/Magisk/releases/download/canary-28103/app-release.apk"
        val request = Request.Builder().url(url).build()
        var totalBytes = 0L
        var speedMbps = 0.0

        try {
            val response = client.newCall(request).execute()
            val body = response.body
            val stream = body?.byteStream()
            val buffer = ByteArray(8192)
            var bytesRead: Int
            val startTime = System.nanoTime()
            var lastUpdateTime = System.currentTimeMillis()

            if (stream != null) {
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    totalBytes += bytesRead

                    val now = System.currentTimeMillis()
                    if (now - lastUpdateTime > 300) {
                        val elapsed = (System.nanoTime() - startTime) / 1_000_000_000.0
                        val currentSpeed = (totalBytes * 8) / (elapsed * 1000 * 1000)
                        speedMbps = currentSpeed
                        withContext(Dispatchers.Main) {
                            speedometer.speedTo(currentSpeed.toFloat())
                        }
                        lastUpdateTime = now
                    }
                }
                stream.close()
            }

        } catch (e: Exception) {
            Log.e("SpeedTest", "Download error: ${e.message}")
        }

        return@withContext speedMbps
    }

    private suspend fun measureUploadSpeed(): Double = withContext(Dispatchers.IO) {
        val uploadUrl = "https://httpbin.org/post"
        val dummyData = ByteArray(5 * 1024 * 1024)
        val mediaType = "application/octet-stream".toMediaType()
        var speedMbps = 0.0

        try {
            val requestBody = object : RequestBody() {
                override fun contentType() = mediaType
                override fun contentLength() = dummyData.size.toLong()

                override fun writeTo(sink: okio.BufferedSink) {
                    val chunkSize = 8192
                    var uploaded = 0L
                    val startTime = System.nanoTime()
                    var lastUpdate = System.currentTimeMillis()

                    while (uploaded < dummyData.size) {
                        val size = min(chunkSize, dummyData.size - uploaded.toInt())
                        sink.write(dummyData, uploaded.toInt(), size)
                        uploaded += size

                        val now = System.currentTimeMillis()
                        if (now - lastUpdate > 300) {
                            val elapsed = (System.nanoTime() - startTime) / 1_000_000_000.0
                            val currentSpeed = (uploaded * 8) / (elapsed * 1000 * 1000)
                            speedMbps = currentSpeed
                            scope.launch(Dispatchers.Main) {
                                speedometer.speedTo(currentSpeed.toFloat())
                            }
                            lastUpdate = now
                        }
                    }
                }
            }

            val request = Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext 0.0

        } catch (e: Exception) {
            Log.e("Upload", "Upload error: ${e.message}")
        }

        return@withContext speedMbps
    }

    private suspend fun measurePingAndJitter(host: String, count: Int): Pair<Int, Int> = withContext(Dispatchers.IO) {
        val times = mutableListOf<Long>()
        repeat(count) {
            val start = System.nanoTime()
            try {
                if (InetAddress.getByName(host).isReachable(1000)) {
                    val time = (System.nanoTime() - start) / 1_000_000
                    times.add(time)
                }
            } catch (_: Exception) {
                times.add(1000)
            }
            delay(300)
        }

        val avg = times.average().roundToInt()
        val jitter = if (times.size > 1) times.zipWithNext { a, b -> abs(a - b) }.average().roundToInt() else 0
        Pair(avg, jitter)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
