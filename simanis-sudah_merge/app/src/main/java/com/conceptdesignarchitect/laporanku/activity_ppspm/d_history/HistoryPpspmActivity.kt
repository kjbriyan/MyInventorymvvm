package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget.HistoryAdapter
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget.HistoryBudgetingActivity
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.DataProyekItem
import com.conceptdesignarchitect.laporanku.models.ProyekResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_history_ppspm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryPpspmActivity : AppCompatActivity() {

    fun onItemClicked(get: DataProyekItem?){
//        Toast.makeText(this, "klick "+get?.proyek, Toast.LENGTH_LONG).show()
        val intent = Intent(this, HistoryBudgetingActivity::class.java)
        intent.putExtra("proyek", get?.proyek)
        intent.putExtra("totalpekerjaan", get?.totalBobot)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_ppspm)

        RetrofitClient.instance.pilihProyek(SharedPrefManager.getInstance(this).user.id)
            .enqueue(object: Callback<ProyekResponse> {
                override fun onFailure(call: Call<ProyekResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(call: Call<ProyekResponse>, response: Response<ProyekResponse>) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        rvPekerjaan.adapter =
                            HistoryAdapter(
                                response.body()?.dataProyek as List<DataProyekItem>?,
                                this@HistoryPpspmActivity
                            )

                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}
