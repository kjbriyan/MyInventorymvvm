package com.conceptdesignarchitect.laporanku.activity_pengawas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.MingguAdapter
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.DataMingguItem
import com.conceptdesignarchitect.laporanku.models.MingguResponse
import kotlinx.android.synthetic.main.activity_minggu.*
import kotlinx.android.synthetic.main.activity_pekerjaan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MingguActivity : AppCompatActivity() {

    fun onItemClicked(get: DataMingguItem?){
//        Toast.makeText(this@MingguActivity, "klick "+intent.getStringExtra("proyek"), Toast.LENGTH_LONG).show()
        val intent = Intent(this, RealisasiActivity::class.java)
        intent.putExtra("idminggu", get?.id)
        intent.putExtra("minggu", get?.minggu)
        intent.putExtra("proyek", intent.getStringExtra("proyek"))
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minggu)
        pb_minggu.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.mingguKe(intent.getStringExtra("proyek"))
            .enqueue(object: Callback<MingguResponse> {
                override fun onFailure(call: Call<MingguResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    pb_minggu.visibility = View.GONE
                    tv_status_pekerjaan.visibility = View.VISIBLE
                    tv_status_pekerjaan.text = "Gagal jaringan mengambil data pekerjaan minggu"
                }
                @Suppress("UNCHECKED_CAST")
                override fun onResponse(call: Call<MingguResponse>, response: Response<MingguResponse>) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        pb_minggu.visibility = View.GONE
                        rvMinggu.adapter = MingguAdapter(response.body()?.dataMinggu as List<DataMingguItem>?,
                            this@MingguActivity
                        )
                        if (response.body()?.dataMinggu?.get(0)?.minggu == null){
                            tv_status_pekerjaan.visibility = View.VISIBLE
                            tv_status_pekerjaan.text = "Tidak ada data pekerjaan minggu"
                        }
                    }else{
                        pb_minggu.visibility = View.GONE
//                        Toast.makeText(applicationContext, "Maaf terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                        tv_status_minggu.visibility = View.VISIBLE
                        tv_status_minggu.text = "Gagal mengambil data pekerjaan minggu"
                    }
                }
            })
    }
}
