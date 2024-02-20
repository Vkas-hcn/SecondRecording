package com.flie.best.tato.secondrecording

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.flie.best.tato.secondrecording.databinding.ActivitySettingBinding
import com.flie.best.tato.secondrecording.databinding.ActivityWebBinding

class PolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickFun()
        initWebView()
    }

    private fun clickFun() {
        binding.imageView2.setOnClickListener {
            finish()
        }
    }

    private fun initWebView() {
        binding.webView.apply {
            loadUrl(SjUtils.getPolicyUrl())
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }
            }
        }
    }
}