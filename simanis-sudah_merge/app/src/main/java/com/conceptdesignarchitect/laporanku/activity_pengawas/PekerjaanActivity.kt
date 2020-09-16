package com.conceptdesignarchitect.laporanku.activity_pengawas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.ProyeksAdapter
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.DataProyekItem
import com.conceptdesignarchitect.laporanku.models.ProyekResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_pekerjaan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PekerjaanActivity : AppCompatActivity() {

    fun onItemClicked(get: DataProyekItem?) {
//        Toast.makeText(this@PekerjaanActivity, "klick "+get?.proyek, Toast.LENGTH_LONG).show()
        val intent = Intent(this, MingguActivity::class.java)
        intent.putExtra("proyek", get?.proyek)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pekerjaan)

//        Log.d("kontoru", SharedPrefManager.getInstance(this).user.id.toString())
        pb_pekerjaan.visibility = View.VISIBLE
        RetrofitClient.instance.pilihProyek(SharedPrefManager.getInstance(this).user.id)
            .enqueue(object : Callback<ProyekResponse> {
                override fun onFailure(call: Call<ProyekResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    pb_pekerjaan.visibility = View.GONE
                    tv_status_pekerjaan.visibility = View.VISIBLE
                    tv_status_pekerjaan.text = "Gagal jaringan mengambil data pekerjaan"
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(
                    call: Call<ProyekResponse>,
                    response: Response<ProyekResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful) {
                        rvPekerjaan.adapter = ProyeksAdapter(
                            response.body()?.dataProyek as List<DataProyekItem>?,
                            this@PekerjaanActivity
                        )
                        pb_pekerjaan.visibility = View.GONE
                        if (response.body()?.dataProyek?.get(0)?.proyek == null) {
                            tv_status_pekerjaan.visibility = View.VISIBLE
                            tv_status_pekerjaan.text = "Tidak ada data pekerjaan"
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf Terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                        pb_pekerjaan.visibility = View.GONE
                        tv_status_pekerjaan.visibility = View.VISIBLE
                        tv_status_pekerjaan.text = "Gagal mengambil data pekerjaan"

                    }
                }
            })
    }
}
