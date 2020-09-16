package com.conceptdesignarchitect.laporanku.activity_ppk

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.LihatPekerjaanAdapter
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.*
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_lihat_realisasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class LihatRealisasiActivity : AppCompatActivity() {
    private val sectionArray = ArrayList<String>()
    private val subKerjaArray = ArrayList<String>()

    fun onItemClicked(get: DataDetailpekerjaanItem?) {
        Toast.makeText(this, "klik " + get?.uraianPekerjaan, Toast.LENGTH_LONG).show()
//        val intent = Intent(this, MingguActivity::class.java)
//        intent.putExtra("proyek", get?.uraianPekerjaan)
//        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_realisasi)

        sectionArray.add("Pilih Section / Bagian")
        lihatbobotminggu()
        lihatSection()

        id_btn_valid.setOnClickListener {
            alertDialogSend()

        }


    }


    fun alertDialogSend() {
        val builder = AlertDialog.Builder(this@LihatRealisasiActivity)
        val view = layoutInflater.inflate(R.layout.signature_send_dialog, null)
        builder.setView(view)
        builder.setTitle("Tanda Tangan")
        val signaturePad = view.findViewById<SignaturePad>(R.id.signaturePad)

        view.findViewById<Button>(R.id.btn_send_signature).setOnClickListener {
            val signature = signaturePad.signatureBitmap
            postttd(signature)
            setResult(Activity.RESULT_OK)

        }
        view.findViewById<Button>(R.id.btn_clear_signature).setOnClickListener {
            signaturePad.clearView()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    fun postttd(bitmap: Bitmap) {

        val img = getEncoded64ImageStringFromBitmap(bitmap)
        val init = Initretrofit().getInstance().upload_ttd(img.toString())
        init.enqueue(object : Callback<KirimResponse> {
            override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "faild network" + t.message + "-" + t.cause,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<KirimResponse>, response: Response<KirimResponse>) {
                val res = response.body()
                if (response.body()?.status == "Gagal") {
                    Toast.makeText(
                        applicationContext,
                        "fail post ttd" + res?.status,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "status " + res?.status,
                        Toast.LENGTH_SHORT
                    ).show()
                    kirimlaporan(res?.status.toString())
                    finish()
                }
            }

        })
    }

    private fun lihatbobotminggu() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatbobotminggu(
            intent.getStringExtra("proyek"),
            intent.getStringExtra("idminggu"),
            intent.getStringExtra("minggu")
        )
            .enqueue(object : Callback<BobotmingguResponse> {
                override fun onFailure(call: Call<BobotmingguResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<BobotmingguResponse>,
                    response: Response<BobotmingguResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.dataTotalBobot as List<DataTotalBobotItem>?
                        for (i in list!!.indices) {
                            idtotal_minggulalu.text = list[i].mingguLalu.toString()
                            idtotal_mingguini.text = list[i].mingguIni.toString()
                            idtotal_sdmingguini.text = list[i].sdMingguIni.toString()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf Terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    private fun lihatSection() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatsection(intent.getStringExtra("proyek"))
            .enqueue(object : Callback<SectionResponse> {
                override fun onFailure(call: Call<SectionResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(
                    call: Call<SectionResponse>,
                    response: Response<SectionResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.dataSection as List<DataSectionItem>?
                        for (i in list!!.indices) {
                            sectionArray.add(list[i].section.toString())
                        }
                        val spinnerAdapter = ArrayAdapter(
                            this@LihatRealisasiActivity,
                            android.R.layout.simple_spinner_item,
                            sectionArray
                        )
                        idspinsection.adapter = spinnerAdapter
                        idspinsection.onItemSelectedListener = object :

                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (sectionArray[position] !== "Pilih Section / Bagian") {
                                    lihatSubPekerjaan(sectionArray[position])
                                    Toast.makeText(
                                        applicationContext,
                                        "." + sectionArray[position],
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf Terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun lihatSubPekerjaan(section: String) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatsubpekerjaan(section, intent.getStringExtra("proyek"))
            .enqueue(object : Callback<SubpekerjaanResponse> {
                override fun onFailure(call: Call<SubpekerjaanResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                @Suppress("UNCHECKED_CAST")
                override fun onResponse(
                    call: Call<SubpekerjaanResponse>,
                    response: Response<SubpekerjaanResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful) {

                        subKerjaArray.clear()
                        subKerjaArray.add("Pilih sub pekerjaan")

                        val list2 = response.body()?.dataSubpekerjaan as List<DataSubpekerjaanItem>?
                        for (i in list2!!.indices) {
                            subKerjaArray.add(list2[i].pekerjaan.toString())
                        }
                        val spinnerAdapter2 = ArrayAdapter(
                            this@LihatRealisasiActivity,
                            android.R.layout.simple_spinner_item,
                            subKerjaArray
                        )
                        idspinsubpekerjaan.adapter = spinnerAdapter2
                        idspinsubpekerjaan.onItemSelectedListener = object :

                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (subKerjaArray[position] !== "Pilih sub pekerjaan") {
                                    lihatDetailPekerjaan(section, subKerjaArray[position])
                                    Toast.makeText(
                                        applicationContext,
                                        "." + subKerjaArray[position],
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }

                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf Terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    private fun lihatDetailPekerjaan(section: String, pekerjaan: String) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatdetailpekerjaan(
            intent.getStringExtra("proyek"),
            section,
            intent.getStringExtra("idminggu"),
            pekerjaan
        )
            .enqueue(object : Callback<DetailpekerjaanResponse> {
                override fun onFailure(call: Call<DetailpekerjaanResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<DetailpekerjaanResponse>,
                    response: Response<DetailpekerjaanResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if (response.isSuccessful) {
                        if (response.body()?.status == "Berhasil") {
//                            Toast.makeText(applicationContext, "", Toast.LENGTH_LONG).show()
                            @Suppress("UNCHECKED_CAST")
                            idRvRealisasi.adapter = LihatPekerjaanAdapter(
                                response.body()?.dataDetailpekerjaan as List<DataDetailpekerjaanItem>?,
                                this@LihatRealisasiActivity
                            )
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Gagal mendapat response",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf terjadi kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    private fun kirimlaporan(ttd: String) {
        RetrofitClient.instance.Validasilaporan(
            intent.getStringExtra("proyek"),
            intent.getStringExtra("idminggu"),
            ttd
        )
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "" + response.body()?.status,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maaf terjadi Kesalahan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }
}
