package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.b_detail_budget.DetailBudgetingActivity
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import kotlinx.android.synthetic.main.activity_budgeting_kontruksi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryBudgetingActivity : AppCompatActivity() {

    fun onItemClicked(get: DataHistoryItem?){
//        Toast.makeText(this, "klick "+get?.proyek, Toast.LENGTH_LONG).show()
        val intent = Intent(this, DetailBudgetingActivity::class.java)
        intent.putExtra("proyek", get?.proyek)
        intent.putExtra("tanggal", get?.tanggal)
        intent.putExtra("rincian", get?.rincian)
        intent.putExtra("nilai", get?.nilai)
        intent.putExtra("kategori", get?.kategori)
        intent.putExtra("surat", get?.surat)
        intent.putExtra("id", get?.id)

        startActivity(intent)
    }

    override fun onStart() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.historybudgeting(intent.getStringExtra("proyek"))
            .enqueue(object: Callback<HistoryResponse> {
                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                    Log.d("Response", response.body().toString())
                    if(response.isSuccessful){
                        rvPekerjaan.adapter =
                            HistoryBudgetingAdapter(
                                response.body()?.dataHistory as List<DataHistoryItem>?,
                                this@HistoryBudgetingActivity
                            )

                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }
            })
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_budgeting)

    }
}
