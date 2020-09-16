package com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_web_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebViewReportActivity : AppCompatActivity() {
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
                    sudahdilihat(proyek,uri)
                }
            }
        }
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf$uri")
        Log.d("PDF", "https://docs.google.com/gview?embedded=true&url=$pdf$uri")
    }


    private fun sudahdilihat(proyek:String,idminggu:String) {
        RetrofitClient.instance.Cekvalidasi(SharedPrefManager.getInstance(this).user.id,proyek, idminggu)
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, ""+response.body()?.status, Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Maaf terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }

            })
    }
}