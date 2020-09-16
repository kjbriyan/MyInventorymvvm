package com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.KonstruksiAdapter
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.DataProyekItem
import com.conceptdesignarchitect.laporanku.models.ProyekResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_budgeting_kontruksi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BudgetingProyekActivity : AppCompatActivity() {

    fun onItemClicked(get: DataProyekItem?){
        val inte = intent
//        Toast.makeText(this, "klick "+get?.proyek, Toast.LENGTH_LONG).show()
        val intent = Intent(this, UploadUangActivity::class.java)
        intent.putExtra("proyek", get?.proyek)
        intent.putExtra("kategori", inte.getStringExtra("kategori"))
        startActivity(intent)
    }

    override fun onStart() {
        RetrofitClient.instance.pilihProyek(SharedPrefManager.getInstance(this).user.id)
            .enqueue(object: Callback<ProyekResponse> {
                override fun onFailure(call: Call<ProyekResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(call: Call<ProyekResponse>, response: Response<ProyekResponse>) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        rvPekerjaan.adapter = KonstruksiAdapter(response.body()?.dataProyek as List<DataProyekItem>?,
                            this@BudgetingProyekActivity
                        )
//                        var list = response.body()?.dataProyek as List<DataProyekItem>?
//
//                        for (i in list!!.indices) {
//                            playerNames.add(list[i].proyek.toString())
//                        }
//                        Toast.makeText(applicationContext, "he " +playerNames[0], Toast.LENGTH_LONG).show()
//                        Toast.makeText(applicationContext, "hek " +list[1], Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }
            })
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgeting_kontruksi)

    }
}
