package com.conceptdesignarchitect.laporanku.activity_ppk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.DokumentasiActivity
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import com.conceptdesignarchitect.laporanku.util.Helper
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_web_view_report_ppk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebViewReportppk : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_report_ppk)

    }
    override fun onResume() {
        super.onResume()
        val uri = intent.getStringExtra("idmig")
        val pdf = "http://simanis.konseparsitek.com/coba/cobapdf/"
        val proyek = intent.getStringExtra("proyek")

        flbtn_dokumentasi.setOnClickListener {
            val intent = Intent(this, DokumentasiActivity::class.java)
            intent.putExtra("idminggu", uri)
            startActivity(intent)
        }

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
                    flbtn_validasi.visibility = View.VISIBLE
                    flbtn_validasi.setOnClickListener {
                        alertDialogSend()
                    }

                }
            }
        }
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf$uri")
        Log.d("PDF", "https://docs.google.com/gview?embedded=true&url=$pdf$uri")
    }

    fun alertDialogSend() {
        val uri = intent.getStringExtra("idmig")
        val proyek = intent.getStringExtra("proyek")
        val builder = AlertDialog.Builder(this@WebViewReportppk)
        val view = layoutInflater.inflate(R.layout.signature_send_dialog, null)
        builder.setView(view)
        builder.setTitle("Tanda Tangan")
        val signaturePad = view.findViewById<SignaturePad>(R.id.signaturePad)

        view.findViewById<Button>(R.id.btn_send_signature).setOnClickListener {
            val signature = signaturePad.signatureBitmap
            postttd(signature)
            setResult(Activity.RESULT_OK)
            sudahdilihat(proyek,uri)
        }
        view.findViewById<Button>(R.id.btn_clear_signature).setOnClickListener {
            signaturePad.clearView()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

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

    fun postttd(bitmap: Bitmap) {

        val img = Helper().getEncoded64ImageStringFromBitmap(bitmap)
        val init = Initretrofit().getInstance().upload_ttd(img.toString())
        init.enqueue(object : Callback<KirimResponse> {
            override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "faild network" + t.message + "-" + t.cause,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<KirimResponse>, response: Response<KirimResponse>) {
                val res = response.body()
                if (response.body()?.status == "Gagal") {
                    Toast.makeText(
                        applicationContext,
                        "fail post ttd" + res?.status,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Setatus ok ttd",
                        Toast.LENGTH_SHORT
                    ).show()
                    kirimlaporan(res?.status.toString())
                }
            }

        })
    }
    private fun kirimlaporan(ttd: String) {
        RetrofitClient.instance.Validasilaporan(
            intent.getStringExtra("proyek"),
            intent.getStringExtra("idmig"),
            ttd
        )
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "" + response.body()?.status,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

}