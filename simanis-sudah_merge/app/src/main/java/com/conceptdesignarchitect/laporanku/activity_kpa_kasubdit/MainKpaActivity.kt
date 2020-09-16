package com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.HistoryActivity
import com.conceptdesignarchitect.laporanku.activity.ProfilActivity
import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik.PilihLaporanActivity
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.CeknotifResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_kpa.idUserEmail
import kotlinx.android.synthetic.main.activity_main_kpa.id_badge_report
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainKpaActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        cek_report()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idUserEmail.text = SharedPrefManager.getInstance(this).user.email

        btniniprog.visibility = View.GONE

        btnopo.setOnClickListener {
            intent = Intent(applicationContext,HistoryActivity::class.java)
            startActivity(intent)
        }

        ln_report.setOnClickListener {
            intent = Intent(applicationContext, CekLaporanActivity::class.java)
            startActivity(intent)
        }

        ln_profil.setOnClickListener {
            intent = Intent(applicationContext, ProfilActivity::class.java)
            startActivity(intent)
        }

        btnini.setOnClickListener {
//            budget
            intent = Intent(applicationContext, PilihLaporanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cek_report() {
        RetrofitClient.instance.ceknotif(SharedPrefManager.getInstance(this).user.id)
            .enqueue(object : Callback<CeknotifResponse> {
                override fun onFailure(call: Call<CeknotifResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CeknotifResponse>,
                    response: Response<CeknotifResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()?.status == "Berhasil" && response.body()?.notif.toString() != "0"){
                            response.body()?.notif?.let { id_badge_report.setBadgeValue(it) }
                        }else{
                            Toast.makeText(applicationContext, ""+response.body()?.status, Toast.LENGTH_SHORT).show()
                            response.body()?.notif?.let { id_badge_report.setBadgeValue(it) }
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Gagal mendapatkan pembaruan." , Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
    }
}
