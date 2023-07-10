package com.ileping.shell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    var _exitTime = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = WebView(this).apply {
            settings.apply {
                domStorageEnabled = true
                javaScriptEnabled = true
                blockNetworkImage = false
            }
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            loadUrl("file:///android_asset/index.html")
        }

        findViewById<LinearLayout>(R.id.layout).addView(
            webView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else if (System.currentTimeMillis() - _exitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            _exitTime = System.currentTimeMillis()
        } else {
            finish();
//            exitProcess(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }
}