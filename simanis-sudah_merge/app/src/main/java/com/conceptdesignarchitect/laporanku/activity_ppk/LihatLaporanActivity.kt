package com.conceptdesignarchitect.laporanku.activity_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.WebViewHistoryActivity
import com.conceptdesignarchitect.laporanku.adapter.LihatlaporanAdapter
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.DataLihatlaporanItem
import com.conceptdesignarchitect.laporanku.models.LihatlaporanResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_lihat_laporan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LihatLaporanActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        RetrofitClient.instance.lihatlaporan(SharedPrefManager.getInstance(this).user.id)
            .enqueue(object: Callback<LihatlaporanResponse> {
                override fun onFailure(call: Call<LihatlaporanResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LihatlaporanResponse>,
                    response: Response<LihatlaporanResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        @Suppress("UNCHECKED_CAST")
                        rvLihatlaporan.adapter = LihatlaporanAdapter(response.body()?.dataLihatlaporan as List<DataLihatlaporanItem>?,
                            this@LihatLaporanActivity
                        )
                    }else{
                        Toast.makeText(applicationContext, "Maaf terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    fun onItemClicked(get: DataLihatlaporanItem?){
//        Toast.makeText(this, "klick "+get?.namaProyek, Toast.LENGTH_LONG).show()
        val intent = Intent(this, WebViewReportppk::class.java)
        intent.putExtra("idmig", get?.idMinggu)
        intent.putExtra("minggu", get?.minggu)
        intent.putExtra("proyek", get?.namaProyek)
        intent.putExtra("activ","ppk")
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_laporan)



    }


}
