package com.conceptdesignarchitect.laporanku.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.conceptdesignarchitect.laporanku.R
import kotlinx.android.synthetic.main.activity_dokumentasi.*
import kotlinx.android.synthetic.main.activity_web_view.*

class DokumentasiActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dokumentasi)

        val idminggu = intent.getStringExtra("idminggu")
//        val idminggu = "73"
        val link = "http://simanis.konseparsitek.com/coba/pdfdokumentasi/"

        webview_d.settings.javaScriptEnabled = true
        webview_d.settings.builtInZoomControls = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview_d.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    pbar_d.visibility = View.GONE
                }
            }
        }
        webview_d.loadUrl("https://docs.google.com/gview?embedded=true&url=$link$idminggu")
    }
}