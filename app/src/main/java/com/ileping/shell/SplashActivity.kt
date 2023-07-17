package com.ileping.shell

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.JsPromptResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var container: LinearLayout
    private val tag = this::class.simpleName

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
                                    openMainActivity()
                                    // result?.confirm("0")
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


    }

    // 阻止此页面下的退出
    override fun onBackPressed() {
    }

    override fun onPause() {
        super.onPause()

        webView.destroy();
    }

    fun openMainActivity() {
        val fromMain = intent.getBooleanExtra("FROM_MAIN", false)
        if (fromMain) {
            setResult(Activity.RESULT_OK)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }

        finish()
    }
}