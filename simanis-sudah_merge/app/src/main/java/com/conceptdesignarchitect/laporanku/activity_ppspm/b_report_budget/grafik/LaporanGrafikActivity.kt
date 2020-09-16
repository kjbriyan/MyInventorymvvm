package com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.grafik.ResponseGrafikdetail
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.activity_laporan_grafik.*
import kotlinx.android.synthetic.main.dialog_detail_diagram.*
import params.com.stepprogressview.StepProgressView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class LaporanGrafikActivity : AppCompatActivity() {

    var total_uang_sudah_dibayar = ""
    val digit = DecimalFormat("0.00")
    var budpel :Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_grafik)

        tv_detaildiagram.setOnClickListener {
            getDetailpie()
        }

        val proyek = intent.getStringExtra("proyek")
        val bobot_pekerjaan = intent.getStringExtra("totalpekerjaan")

        val koma = ","
        val bobot_pekerjaan_asli = bobot_pekerjaan!!.replace(koma.toRegex(), ".")

        val total_bobot_sudah_dikerjakan = bobot_pekerjaan_asli
        val total_bobot_belum_dikerjakan = (100 - bobot_pekerjaan_asli.toDouble())

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.lihatgrafik(proyek.toString())
            .enqueue(object: Callback<ModelGrafikResponse> {
                override fun onFailure(call: Call<ModelGrafikResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ModelGrafikResponse>,
                    response: Response<ModelGrafikResponse>
                ) {
                    Log.i("Response", response.body().toString())
                    if(response.isSuccessful){
                        val list = response.body()?.dataGrafik as List<DataGrafikItem>?
                        for (i in list!!.indices) {
                            total_uang_sudah_dibayar = list[i].prosesPembayaran!!.toString()
//                            disini ada nilai
                            idtotalpekerjaan.text = list[i].totalPekerjaan!!.toString()
                            idtotalsubpekerjaan.text = list[i].totalSubPekerjaan!!.toString()
                            idnilaikontrak.text = list[i].totalNilaiKontrak!!.toString()
                            buat_grafik(total_uang_sudah_dibayar, list[i].pelaksana!!.toDouble(),list[i].pengawas!!.toDouble(),list[i].perencana!!.toDouble(),list[i].honorium!!.toDouble()
                                ,list[i].perjalananDinas!!.toDouble(),list[i].habisPakai!!.toDouble())
//                            buat_grafik(total_uang_sudah_dibayar, 4.4,5.5,5.7,7.7
//                                ,8.8,9.9)
                        }
                    }else{
                        Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                    }
                }
            })

        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(total_bobot_sudah_dikerjakan.toFloat(), "Dikerjakan"))
        listColors.add(resources.getColor(R.color.colorAccent))
        listPie.add(PieEntry(total_bobot_belum_dikerjakan.toFloat(), "Belum Dikerjakan"))
        listColors.add(resources.getColor(R.color.kuning))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        idpie_kontruksi.data = pieData
        pieData.setValueFormatter(PercentFormatter(idpie_kontruksi))

        idpie_kontruksi.setUsePercentValues(true)
        idpie_kontruksi.isDrawHoleEnabled = false
        idpie_kontruksi.setDrawEntryLabels(false)
        idpie_kontruksi.holeRadius = 15F
        idpie_kontruksi.disableScroll()
        idpie_kontruksi.description.isEnabled = false
        idpie_kontruksi.isDrawHoleEnabled = true
        idpie_kontruksi.setUsePercentValues(true)
        idpie_kontruksi.setEntryLabelColor(R.color.contentTextColor)
        idpie_kontruksi.animateY(900, Easing.EaseInOutQuad)
        idpie_kontruksi.isRotationEnabled = false

        idpie_kontruksi.setDrawSliceText(false); // To remove slice text
        idpie_kontruksi.setDrawMarkers(false); // To remove markers when click
        idpie_kontruksi.setDrawEntryLabels(false); // To remove labels from piece of pie
        idpie_kontruksi.getDescription().setEnabled(false); // To remove description of pie

//        val x = total_bobot_sudah_dikerjakan.toDouble()
//        val angkaSignifikan = 2
//        val temp = Math.pow(10.0, angkaSignifikan.toDouble())
//        val y = Math.round(x * temp).toDouble() / temp

        val wes_dikerjakan : Double = total_bobot_sudah_dikerjakan.toDouble()
        val belum_dikerjakan : Double = total_bobot_belum_dikerjakan.toDouble()

        idtext_dikerjakan.text = digit.format(wes_dikerjakan).toString()+" %"
        idtext_belum_dikerjakan.text = digit.format(belum_dikerjakan).toString()+" %"

    }

    private fun buat_grafik(totalUangSudahDibayar: String, pelaksana: Double,pengawas: Double,perencana: Double,honorium: Double,perjalanan_dinas: Double,habis_pakai:Double) {

//        val total_uang_belum_dibayar = (100 - totalUangSudahDibayar)
//        Log.i("belum", total_uang_belum_dibayar.toString())
        val persen_terbayar = pelaksana+pengawas+perencana+honorium+perjalanan_dinas+habis_pakai
        val utang = (100 - persen_terbayar)
        //disini ada tidak nilai
        Log.i("sudah", total_uang_sudah_dibayar.toString())

        val listPie2 = ArrayList<PieEntry>()
        val listColors2 = ArrayList<Int>()
        listPie2.add(PieEntry(pelaksana.toFloat(), "Pelaksana"))
        listColors2.add(resources.getColor(R.color.pink))
        listPie2.add(PieEntry(pengawas.toFloat(), "Pengawas"))
        listColors2.add(resources.getColor(R.color.holowblue))
        listPie2.add(PieEntry(perencana.toFloat(), "Perencana"))
        listColors2.add(resources.getColor(R.color.lightyellow))
        listPie2.add(PieEntry(honorium.toFloat(), "Honorium"))
        listColors2.add(resources.getColor(R.color.orange))
        listPie2.add(PieEntry(perjalanan_dinas.toFloat(), "Perjalanan Dinas"))
        listColors2.add(resources.getColor(R.color.flowgreen))
        listPie2.add(PieEntry(habis_pakai.toFloat(), "Habis Pakai"))
        listColors2.add(resources.getColor(R.color.hollowyelow))
        listPie2.add(PieEntry(utang.toFloat(), "Belum"))
        listColors2.add(resources.getColor(R.color.bakul))

        val pieDataSet2 = PieDataSet(listPie2, "")
        pieDataSet2.colors = listColors2

        val pieData2 = PieData(pieDataSet2)
        idpie_budgeting.data = pieData2
        pieData2.setValueFormatter(PercentFormatter(idpie_budgeting))

        idpie_budgeting.scrollX
        idpie_budgeting.setUsePercentValues(true)
        idpie_budgeting.isDrawHoleEnabled = false
        idpie_budgeting.setDrawEntryLabels(false)
        idpie_budgeting.holeRadius = 15F
        idpie_budgeting.disableScroll()
        idpie_budgeting.description.isEnabled = false
        idpie_budgeting.isDrawHoleEnabled = true
        idpie_budgeting.setUsePercentValues(true)
        idpie_budgeting.isUsePercentValuesEnabled
        idpie_budgeting.setEntryLabelColor(R.color.contentTextColor)
        idpie_budgeting.animateY(900, Easing.EaseInOutQuad)
        idpie_budgeting.setEntryLabelTextSize(2F)
        idpie_budgeting.isRotationEnabled = false
        idpie_budgeting.description.isEnabled = false

//        idpie_budgeting.getDescription().setEnabled(false); // To remove description of pie

        val wes_dibayar : Double = persen_terbayar.toDouble()
        val belum_dibayar : Double = utang.toDouble()

        idtext_belum_dibayar.text = digit.format(belum_dibayar).toString()+" %"
        idtext_dibayar.text = digit.format(wes_dibayar).toString()+" %"
        tv_progres.text = total_uang_sudah_dibayar.toString()


    }

    private fun getDetailpie(){
        val builder = AlertDialog.Builder(this@LaporanGrafikActivity)
        val view = layoutInflater.inflate(R.layout.dialog_detail_diagram, null)
        builder.setView(view)
        builder.setTitle("Detail Grafik")
        getDetailgrafik(view)

//        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_pelaksana)
//        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_pelaksana)
//        Log.d("anjer",budpel.toString())
//        budpell.text = "/$budpel rupiah"
//        spvpell.totalProgress = 100
//        spvpell.currentProgress = budpel.toInt()


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    private fun getDetailgrafik(view: View) {
        val proyek = intent.getStringExtra("proyek")
        val init = Initretrofit().getInstance().lihatgrafikdetail(proyek.toString())
        init.enqueue(object : Callback<ResponseGrafikdetail>{
            override fun onFailure(call: Call<ResponseGrafikdetail>, t: Throwable) {
                Toast.makeText(applicationContext, "Terjadi gagal di jaringan anda."+t.cause , Toast.LENGTH_LONG).show()
                ln_diagram.visibility = View.GONE
                tv_failgetdata.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<ResponseGrafikdetail>,
                response: Response<ResponseGrafikdetail>
            ) {
                val res = response.body()?.dataGrafikDetail
//                budpel = res?.persenPelaksana!!.toDouble()
                if (response.isSuccessful){
                    Log.d("ggwp",proyek+" "+response.body().toString())
                    setViewalert_pelak(view,res?.get(0)?.rpPelaksana.toString(),
                        res?.get(0)?.rpAsliPelaksana.toString(),
                        res?.get(0)?.persenPelaksana.toString(),
                        res?.get(0)?.rpKurangPelaksana.toString()
                    )
                    setViewalert_peren(view,res?.get(0)?.rpPerencana.toString(),
                        res?.get(0)?.rpAsliPerencana.toString(),
                        res?.get(0)?.persenPerencana.toString(),
                        res?.get(0)?.rpKurangPerencana.toString()
                    )
                    setViewalert_peng(view,res?.get(0)?.rpPengawas.toString(),
                        res?.get(0)?.rpAsliPengawas.toString(),
                        res?.get(0)?.persenPengawas.toString(),
                        res?.get(0)?.rpKurangPengawas.toString()
                    )
                    setViewalert_hon(view,res?.get(0)?.rpHonorium.toString(),
                        res?.get(0)?.rpAsliHonorium.toString(),
                        res?.get(0)?.persenHonorium.toString(),
                        res?.get(0)?.rpKurangHonorium.toString()
                    )
                    setViewalert_perja(view,res?.get(0)?.rpPerjalanan.toString(),
                        res?.get(0)?.rpAsliPerjalanan.toString(),
                        res?.get(0)?.persenPerjalanan.toString(),
                        res?.get(0)?.rpKurangPerjalanan.toString()
                    )
                    setViewalert_habis(view,res?.get(0)?.rpHabis.toString(),
                        res?.get(0)?.rpAsliHabis.toString(),
                        res?.get(0)?.persenHabis.toString(),
                        res?.get(0)?.rpKurangHabis.toString()
                    )

                }else{
                    Toast.makeText(applicationContext, "Maaf Terjadi Kesalahan.." , Toast.LENGTH_LONG).show()
                }
            }

        })
    }
    private fun setViewalert_pelak(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalpelaksana)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_pelaksana)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_pelaksana)
        val kur = view.findViewById<TextView>(R.id.tvkurangpel)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }

    private fun setViewalert_peren(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalperencana)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_perencana)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_perencana)
        val kur = view.findViewById<TextView>(R.id.tvkurangper)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }

    private fun setViewalert_peng(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalpengawas)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_pengawas)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_pengawas)
        val kur = view.findViewById<TextView>(R.id.tvkurangpeng)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }

    private fun setViewalert_hon(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalhonorium)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_honorium)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_honorium)
        val kur = view.findViewById<TextView>(R.id.tvkuranhonor)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }

    private fun setViewalert_perja(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalperjalanan)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_perjalanan)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_perjalanan)
        val kur = view.findViewById<TextView>(R.id.tvkurangperja)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }

    private fun setViewalert_habis(view : View,totpel : String,budpel : String,svppel : String, kurang : String) {
        val totpell = view.findViewById<TextView>(R.id.tv_totalhabis)
        val budpell = view.findViewById<TextView>(R.id.tv_totalbudget_habis)
        val spvpell =  view.findViewById<StepProgressView>(R.id.spv_habis)
        val kur = view.findViewById<TextView>(R.id.tvkuranghabis)
        totpell.text = "$totpel"
        budpell.text = "/$budpel"
        kur.text = "-$kurang"
        spvpell.totalProgress = 100
        spvpell.currentProgress = svppel.toDouble().toInt()
    }
}
