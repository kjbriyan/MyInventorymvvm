package com.conceptdesignarchitect.laporanku.activity_pengawas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.BudgetingActivity
import com.conceptdesignarchitect.laporanku.activity.HistoryActivity
import com.conceptdesignarchitect.laporanku.activity.ProfilActivity
import com.conceptdesignarchitect.laporanku.activity.LoginActivity
import com.conceptdesignarchitect.laporanku.activity_ppk.MainPPKActivity
import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik.PilihLaporanActivity
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*


class MainPengawasActivity : AppCompatActivity() {
    val CUSTOM_PREF_NAME = "User_data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        logicLogin()
        idUserEmail.text = SharedPrefManager.getInstance(this).user.email

        val prefs = MainPPKActivity.PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)

        btnini.setOnClickListener {
            intent = Intent(applicationContext, PilihLaporanActivity::class.java)
            startActivity(intent)
        }
        btniniprog.setOnClickListener {
            intent = Intent(applicationContext, BudgetingActivity::class.java)
            startActivity(intent)
        }

        btnopo.setOnClickListener {
            intent = Intent(applicationContext, HistoryActivity::class.java)
            startActivity(intent)
        }

        ln_report.setOnClickListener {
            intent = Intent(applicationContext, PekerjaanActivity::class.java)
            startActivity(intent)
        }
        ln_profil.setOnClickListener {
            intent = Intent(applicationContext, ProfilActivity::class.java)
            startActivity(intent)
        }
    }
//    fun logicLogin(){
//        if (!SharedPrefManager.getInstance(this).isLoggedIn) {
//            val intent = Intent(applicationContext, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }

}
