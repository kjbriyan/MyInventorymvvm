package com.conceptdesignarchitect.laporanku.activity_pengawas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.PekerjaanAdapter
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.*
import com.conceptdesignarchitect.laporanku.models.PekMinglalu.ResponseDetailPekerjaanlalu
import com.conceptdesignarchitect.laporanku.util.Helper
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_realisasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class RealisasiActivity : AppCompatActivity() {

    private var pdfPath: String? = null

    private val sectionArray = ArrayList<String>()
    private val subKerjaArray = ArrayList<String>()
    var title: String = ""
    var volLalu: String = null.toString()
    var datasec: String = ""
    var datasub: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realisasi)
        sectionArray.add("Pilih Section / Bagian")
        lihatbobotminggu()
        lihatSection()
        id_btn_kirim.setOnClickListener {
            alertDialogSend()
        }

        val b =intent.getStringExtra("idminggu")

        btn_bukti.setOnClickListener {
            val i = Intent(this,UploadFotoActivity::class.java)
            i.putExtra("idming",b)
            i.putExtra("sec",datasec)
            i.putExtra("sub",datasub)
            Log.d("kirim",b+datasec+datasub)
            startActivity(i)
        }
    }

    fun alertDialogSend() {
        val builder = AlertDialog.Builder(this@RealisasiActivity)
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
        bitmap.compress(CompressFormat.JPEG, 70, stream)
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
                        "Setatus ok ttd",
                        Toast.LENGTH_SHORT
                    ).show()
                    kirimlaporan(res?.status.toString())
                }
            }

        })
    }


    fun onItemClicked(get: DataDetailpekerjaanItem?) {
//        Toast.makeText(this@RealisasiActivity, "klik " + get?.bobot, Toast.LENGTH_LONG).show()
        title = get?.uraianPekerjaan.toString()
        val minggu = intent.getStringExtra("minggu")


        getVolMinglalu(
            get?.idkerja.toString(),
            get?.fkIdMinggu.toString(),
            get?.proyek.toString(),
            get?.fkIdMinggu.toString(),
            minggu,
            get?.idkerja.toString(),
            get?.volumeDetail.toString(),
            get?.volume.toString(),
            get?.bobot.toString(),
            get?.volumeAkhir.toString()
        )
    }

    fun alertdialog(
        proyek: String,
        idming: String, mingke: String, idurai: String,
        edvol: String, vasli: String, bobot: String, vsd: String
    ) {
        val builder = AlertDialog.Builder(this@RealisasiActivity)
        val view = layoutInflater.inflate(R.layout.edit_item_pekerjaan, null)
        builder.setView(view)
//        Toast.makeText(this@RealisasiActivity, "klik " + bobot, Toast.LENGTH_LONG).show()
        view.findViewById<TextView>(R.id.tv_title).text = title
        view.findViewById<TextView>(R.id.tv_vol_sdmingini).text = vsd
        view.findViewById<TextView>(R.id.tv_vol_minglalu).text = volLalu

        val etVOlume = view.findViewById<EditText>(R.id.et_vol_mingini)
        etVOlume.setText(edvol)

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save") { dialog, which ->
            // Do something when user press the positive button
            val datvol = etVOlume.text.toString()
            Log.d(
                "etanol",
                proyek + " idming " + idming + " - " + mingke + " - " + idurai + " - " + datvol + " - " + vsd + " - " + bobot
            )
            val init = Initretrofit().getInstance()
                .simpanvolume(proyek, " ", idming, mingke, idurai, datvol, vasli, bobot)
            init.enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "faild network" + t.message + "-" + t.cause,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    val res = response.body()
                    if (response.body()?.status == "Berhasil Tersimpan") {
                        Toast.makeText(
                            applicationContext,
                            "Setatus " + res?.status,
                            Toast.LENGTH_SHORT
                        ).show()
                        lihatDetailPekerjaan(datasec, datasub)
                        lihatbobotminggu()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal, Volume melebihi batas",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }

            })
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel") { _, _ ->

        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun getVolMinglalu(
        idKerja: String, idMinggu: String, proyek: String,
        idming: String, mingke: String, idurai: String,
        edvol: String, vasli: String, bobot: String, vsd: String
    ) {
        val init = Initretrofit().getInstance().detailLalu(idKerja, idMinggu)
        init.enqueue(object : Callback<ResponseDetailPekerjaanlalu> {
            override fun onFailure(call: Call<ResponseDetailPekerjaanlalu>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "faild network" + t.message + "-" + t.cause,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<ResponseDetailPekerjaanlalu>,
                response: Response<ResponseDetailPekerjaanlalu>
            ) {
                val res = response.body()
                if (response.body()?.status == "Berhasil") {
                    volLalu = res?.dataDetailpekerjaanlalu?.get(0)?.volumeAkhir.toString()
                    alertdialog(proyek, idming, mingke, idurai, edvol, vasli, bobot, vsd)

                } else {
                    Toast.makeText(applicationContext, "failed get data", Toast.LENGTH_SHORT)
                        .show()
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
                            "Terjadi Kesalahan Bobot Minggu",
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
                            this@RealisasiActivity,
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
                                idRvRealisasi.adapter = PekerjaanAdapter(
                                    null as List<DataDetailpekerjaanItem>?,
                                    this@RealisasiActivity
                                )
                                btn_bukti.visibility = View.GONE
                                if (sectionArray[position] !== "Pilih Section / Bagian") {
                                    lihatSubPekerjaan(sectionArray[position])

                                }
                            }

                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Terjadi Kesalahan Lihat Section",
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
                            this@RealisasiActivity,
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

                                    btn_bukti.visibility = View.VISIBLE
                                }
                            }

                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Terjadi Kesalahan Sub Pekerjaan",
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
                            @Suppress("UNCHECKED_CAST")
                            idRvRealisasi.adapter = PekerjaanAdapter(
                                response.body()?.dataDetailpekerjaan as List<DataDetailpekerjaanItem>?,
                                this@RealisasiActivity
                            )
                            datasec = section
                            datasub = pekerjaan

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
                            "terjadi kesalahan detail pekerjaan",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    private fun kirimlaporan(ttd: String) {
        Log.d(
            "ganol", intent.getStringExtra("proyek") +
                    intent.getStringExtra("idminggu") +
                    ttd
        )
        RetrofitClient.instance.Kirimlaporan(
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
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Terjadi Kesalahan TTD",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            var answer: String =  current.format(formatter)
            dateTextView.text = answer
            Log.d("answer",answer)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("MMM-dd-yyyy")
            val answer: String = formatter.format(date)
            dateTextView.text = answer
            Log.d("answer",answer)
        }
    }
}


