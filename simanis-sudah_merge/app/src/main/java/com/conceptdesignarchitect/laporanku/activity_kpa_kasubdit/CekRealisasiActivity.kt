package com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.CekPekerjaanAdapter
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.*
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import kotlinx.android.synthetic.main.activity_cek_realisasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CekRealisasiActivity : AppCompatActivity() {
    private val sectionArray = ArrayList<String>()
    private val subKerjaArray = ArrayList<String>()

    fun onItemClicked(get: DataDetailpekerjaanItem?){
        Toast.makeText(this, "klik "+get?.uraianPekerjaan, Toast.LENGTH_LONG).show()
//        val intent = Intent(this, LihatRealisasiActivity::class.java)
//        intent.putExtra("proyek", get?.uraianPekerjaan)
//        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cek_realisasi)

        sectionArray.add("Pilih Section / Bagian")
        lihatbobotminggu()
        lihatSection()
        sudahdilihat()

        id_btn_valid.setOnClickListener { kirimlaporan() }
    }

    private fun sudahdilihat() {
        RetrofitClient.instance.Cekvalidasi(SharedPrefManager.getInstance(this).user.id,intent.getStringExtra("proyek"), intent.getStringExtra("idminggu"))
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, ""+response.body()?.status, Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Maaf terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun lihatbobotminggu() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatbobotminggu(intent.getStringExtra("proyek"), intent.getStringExtra("idminggu"), intent.getStringExtra("minggu"))
            .enqueue(object: Callback<BobotmingguResponse> {
                override fun onFailure(call: Call<BobotmingguResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<BobotmingguResponse>,
                    response: Response<BobotmingguResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        val list = response.body()?.dataTotalBobot as List<DataTotalBobotItem>?
                        for (i in list!!.indices) {
                            idtotal_minggulalu.text = list[i].mingguLalu.toString()
                            idtotal_mingguini.text = list[i].mingguIni.toString()
                            idtotal_sdmingguini.text = list[i].sdMingguIni.toString()
                        }
                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun lihatSection() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatsection(intent.getStringExtra("proyek"))
            .enqueue(object: Callback<SectionResponse> {
                override fun onFailure(call: Call<SectionResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                @Suppress("UNCHECKED_CAST")
                override fun onResponse(call: Call<SectionResponse>, response: Response<SectionResponse>) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        val list = response.body()?.dataSection as List<DataSectionItem>?
                        for (i in list!!.indices) {
                            sectionArray.add(list[i].section.toString())
                        }
                        val spinnerAdapter = ArrayAdapter(this@CekRealisasiActivity, android.R.layout.simple_spinner_item, sectionArray)
                        idspinsection.adapter = spinnerAdapter
                        idspinsection.onItemSelectedListener = object :

                            AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (sectionArray[position] !== "Pilih Section / Bagian"){
                                    lihatSubPekerjaan(sectionArray[position])
                                    Toast.makeText(applicationContext, "."+sectionArray[position] , Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun lihatSubPekerjaan(section : String) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatsubpekerjaan(section, intent.getStringExtra("proyek"))
            .enqueue(object: Callback<SubpekerjaanResponse> {
                override fun onFailure(call: Call<SubpekerjaanResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(
                    call: Call<SubpekerjaanResponse>,
                    response: Response<SubpekerjaanResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){

                        subKerjaArray.clear()
                        subKerjaArray.add("Pilih sub pekerjaan")

                        val list2 = response.body()?.dataSubpekerjaan as List<DataSubpekerjaanItem>?
                        for (i in list2!!.indices) {
                            subKerjaArray.add(list2[i].pekerjaan.toString())
                        }
                        val spinnerAdapter2 = ArrayAdapter(this@CekRealisasiActivity, android.R.layout.simple_spinner_item, subKerjaArray)
                        idspinsubpekerjaan.adapter = spinnerAdapter2
                        idspinsubpekerjaan.onItemSelectedListener = object :

                            AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (subKerjaArray[position] !== "Pilih sub pekerjaan"){
                                    lihatDetailPekerjaan(section, subKerjaArray[position])
                                    Toast.makeText(applicationContext, "."+subKerjaArray[position] , Toast.LENGTH_LONG).show()

                                }
                            }

                        }
                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun lihatDetailPekerjaan(section: String, pekerjaan: String) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatdetailpekerjaan(intent.getStringExtra("proyek"), section, intent.getStringExtra("idminggu"), pekerjaan)
            .enqueue(object : Callback<DetailpekerjaanResponse> {
                override fun onFailure(call: Call<DetailpekerjaanResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<DetailpekerjaanResponse>,
                    response: Response<DetailpekerjaanResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful){
                        if (response.body()?.status == "Berhasil"){
//                            Toast.makeText(applicationContext, "", Toast.LENGTH_LONG).show()
                            @Suppress("UNCHECKED_CAST")
                            idRvRealisasi.adapter = CekPekerjaanAdapter(response.body()?.dataDetailpekerjaan as List<DataDetailpekerjaanItem>?,
                                this@CekRealisasiActivity)
                        }else{
                            Toast.makeText(applicationContext, "Gagal mendapat response", Toast.LENGTH_LONG).show()
                        }
                    }
                    else {
                        Toast.makeText(applicationContext, "Maaf terjadi kesalahan..", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun kirimlaporan(){
        Toast.makeText(applicationContext, "Masih dalam proses coding..", Toast.LENGTH_SHORT).show()
    }
}
