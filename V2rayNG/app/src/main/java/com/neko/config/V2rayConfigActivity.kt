package com.neko.config

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.widget.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.neko.v2ray.R
import com.neko.v2ray.ui.BaseActivity
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class V2rayConfigActivity : BaseActivity() {

    private lateinit var textConfig: TextView
    private lateinit var textLoading: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnCopy: ImageView
    private lateinit var spinnerServer: Spinner

    private val base64Urls = listOf(
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL01SVC1wcm9qZWN0L3YycmF5LWNvbmZpZ3MvcmVmcy9oZWFkcy9tYWluL0FsbF9Db25maWdzX1N1Yi50eHQ=",
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL01SVC1wcm9qZWN0L3YycmF5LWNvbmZpZ3MvcmVmcy9oZWFkcy9tYWluL1N1YjEudHh0",
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL01SVC1wcm9qZWN0L3YycmF5LWNvbmZpZ3MvcmVmcy9oZWFkcy9tYWluL1N1YjIudHh0",
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL01SVC1wcm9qZWN0L3YycmF5LWNvbmZpZ3MvcmVmcy9oZWFkcy9tYWluL1N1YjMudHh0",
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL01SVC1wcm9qZWN0L3YycmF5LWNvbmZpZ3MvcmVmcy9oZWFkcy9tYWluL1N1YjQudHh0",
        "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL0FMSUlMQVBSTy92MnJheU5HLUNvbmZpZy9tYWluL3NlcnZlci50eHQ="
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v2ray_config)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textConfig = findViewById(R.id.textConfig)
        textLoading = findViewById(R.id.textLoading)
        btnGenerate = findViewById(R.id.btnGenerate)
        btnCopy = findViewById(R.id.btnCopy)
        spinnerServer = findViewById(R.id.spinnerServer)

        val serverNames = base64Urls.indices.map { "Server ${it + 1}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serverNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServer.adapter = adapter

        btnGenerate.setOnClickListener {
            textLoading.text = "Loading..."
            textConfig.text = ""
            val selectedIndex = spinnerServer.selectedItemPosition
            val encodedUrl = base64Urls[selectedIndex]
            val decodedUrl = String(Base64.decode(encodedUrl, Base64.DEFAULT))
            fetchV2rayConfigFrom(decodedUrl)
        }

        btnCopy.setOnClickListener {
            val text = textConfig.text.toString()
            if (text.isNotBlank()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("V2Ray Config", text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No config to copy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchV2rayConfigFrom(rawUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(rawUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val inputStream = connection.inputStream
                val content = inputStream.bufferedReader().use { it.readText() }

                val allowedPrefixes = listOf(
                    "vmess://", "trojan://", "vless://",
                    "ss://", "socks://", "http://",
                    "wireguard://", "hysteria2://"
                )

                val lines = content
                    .lines()
                    .map { it.trim() }
                    .filter { line -> allowedPrefixes.any { prefix -> line.startsWith(prefix, ignoreCase = true) } }

                val randomLine = lines.randomOrNull() ?: "No valid configuration available."

                delay(2000)

                runOnUiThread {
                    textConfig.text = randomLine
                    textLoading.text = ""
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    textConfig.text = "Failed to fetch configuration:\n${e.localizedMessage}"
                    textLoading.text = ""
                }
            }
        }
    }
}
