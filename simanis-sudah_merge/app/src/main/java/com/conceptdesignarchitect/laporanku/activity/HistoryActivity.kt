package com.conceptdesignarchitect.laporanku.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.adapter.RvHistoryadapter
import com.conceptdesignarchitect.laporanku.activity.model.DataHistoryItem
import com.conceptdesignarchitect.laporanku.activity.model.ResponseHistory
import com.conceptdesignarchitect.laporanku.activity_pengawas.MingguActivity
import com.conceptdesignarchitect.laporanku.adapter.ProyeksAdapter
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.models.DataProyekItem
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_pekerjaan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        getList()
    }

    fun getList(){
        val init = Initretrofit().getInstance().getHistorypengawas(SharedPrefManager.getInstance(this).user.id.toString())
        init.enqueue(object : Callback<ResponseHistory>{
            override fun onFailure(call: Call<ResponseHistory>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseHistory>,
                response: Response<ResponseHistory>
            ) {
                Log.i("Response", response.body().toString())
                if (response.isSuccessful) {
                    rv_history.adapter = RvHistoryadapter(
                        response.body()?.dataHistory as List<DataHistoryItem>,
                        this@HistoryActivity
                    )

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Maaf Terjadi Kesalahan.."+response.body()?.dataHistory?.get(0)?.bobotTotal,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }
}
