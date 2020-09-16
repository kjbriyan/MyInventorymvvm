package com.conceptdesignarchitect.laporanku.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_web_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WebViewHistoryActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val uri = intent.getStringExtra("idmig")
        val pdf = "http://simanis.konseparsitek.com/coba/cobapdf/"
        val proyek = intent.getStringExtra("proyek")

        webview.settings.javaScriptEnabled = true
        webview.settings.builtInZoomControls = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    pbar.visibility = View.GONE
                }
            }
        }
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf$uri")
        Log.d("PDF", "https://docs.google.com/gview?embedded=true&url=$pdf$uri")

        flbtn_validasi.setOnClickListener {
            val intent = Intent(this, DokumentasiActivity::class.java)
            intent.putExtra("idminggu", uri)
            startActivity(intent)
        }
    }
}



