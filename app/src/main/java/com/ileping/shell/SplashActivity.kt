package com.ileping.shell

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.JsPromptResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.lang.Thread.sleep


class SplashActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var container: LinearLayout
    var threadBootMain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        container = findViewById<LinearLayout>(R.id.splash_container)

        webView = WebView(this).apply {
            settings.apply {
                domStorageEnabled = true
                javaScriptEnabled = true
                blockNetworkImage = false
            }
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onJsPrompt(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    defaultValue: String?,
                    result: JsPromptResult?
                ): Boolean {
                    val uri = Uri.parse(message)
                    if (url != null && message != null) {
                        if (message.startsWith("native://")) {

                            when (uri.host) {
                                "skipSplash" -> {
                                    goHome()
                                    result?.confirm("call natvie api success")
                                }
                            }
                            return true
                        }
                    }
                    return super.onJsPrompt(view, url, message, defaultValue, result)
                }
            }

            loadUrl("file:///android_asset/splash.html")
        }

        container.addView(
            webView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        Thread {
            try {
                sleep(5000)
                goHome()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun goHome() {
        if (threadBootMain) {
            return
        }
        threadBootMain = true
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}