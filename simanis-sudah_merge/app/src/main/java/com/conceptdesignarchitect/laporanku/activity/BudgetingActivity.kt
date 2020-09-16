package com.conceptdesignarchitect.laporanku.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik.PilihLaporanActivity
import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.BudgetingProyekActivity
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_budgeting.*
import kotlinx.android.synthetic.main.activity_budgeting.btniniprog
import kotlinx.android.synthetic.main.activity_budgeting.btnopo
import kotlinx.android.synthetic.main.activity_budgeting.idUserEmail
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_kpa.*

class BudgetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgeting)

        idUserEmail.text = SharedPrefManager.getInstance(this).user.email
        ln_perencana.setOnClickListener {
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "perencana")
            startActivity(intent)
        }

        ln_pelaksana.setOnClickListener {
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "pelaksana")
            startActivity(intent)
        }

        ln_pengawas.setOnClickListener {
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "pengawas")
            startActivity(intent)
        }

        btniniprog.setOnClickListener {
//            honor
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "honorium")
            startActivity(intent)
        }

        btnopo.setOnClickListener {
//            dinas
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "perjalanan_dinas")
            startActivity(intent)
        }

        ln_biayahabispakai.setOnClickListener {
//            habis pakai
            val intent = Intent(this, BudgetingProyekActivity::class.java)
            intent.putExtra("kategori", "habis_pakai")
            startActivity(intent)
        }

        ln_laporan_grafik.setOnClickListener {
//            grafik
            val intent = Intent(this, PilihLaporanActivity::class.java)
            startActivity(intent)
        }
    }
}
