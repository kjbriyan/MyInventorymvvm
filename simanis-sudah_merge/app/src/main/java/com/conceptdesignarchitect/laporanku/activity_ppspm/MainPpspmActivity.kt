package com.conceptdesignarchitect.laporanku.activity_ppspm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.activity.ProfilActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.BudgetingActivity
import com.conceptdesignarchitect.laporanku.activity.HistoryActivity
import com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit.CekLaporanActivity
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.HistoryPpspmActivity
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.CeknotifResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_kpa.idUserEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPpspmActivity : AppCompatActivity() {

    override fun onStart() {
        cek_report()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idUserEmail.text = SharedPrefManager.getInstance(this).user.email

        btnini.setOnClickListener {
//            budget
            intent = Intent(applicationContext, BudgetingActivity::class.java)
            startActivity(intent)
        }

        ln_profil.setOnClickListener {
            intent = Intent(applicationContext, ProfilActivity::class.java)
            startActivity(intent)
        }

        btnopo.setOnClickListener {
//            histori
            alertDialog()
        }

        ln_report.setOnClickListener {
            intent = Intent(applicationContext, CekLaporanActivity::class.java)
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
                            Log.d("laptop","status "+response.body()?.status+" notif "+response.body()?.notif)
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
    fun alertDialog() {
        val builder = AlertDialog.Builder(this@MainPpspmActivity)
        val view = layoutInflater.inflate(R.layout.option_history_dialog, null)
        builder.setView(view)
        builder.setTitle("History")

        view.findViewById<Button>(R.id.btn_pekerjaan).setOnClickListener {
            intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.btn_keuangan).setOnClickListener {
            intent = Intent(this, HistoryPpspmActivity::class.java)
            startActivity(intent)
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
    }
}
