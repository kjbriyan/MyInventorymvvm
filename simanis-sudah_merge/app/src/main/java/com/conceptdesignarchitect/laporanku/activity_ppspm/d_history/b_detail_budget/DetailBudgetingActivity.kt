package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.b_detail_budget

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.api.RetrofitClient.NAMA_DOMAIN
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_detail_budgeting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

class DetailBudgetingActivity : AppCompatActivity() {

    private val pickButton: Button by lazy { findViewById<Button>(R.id.btn_ganti_pdf) }

    private var pdfPath: String? = null

    var nilai: String = ""
    var namapdf = "Gagal"
    var rincian: String = ""

    @SuppressLint("SetJavaScriptEnabled", "WebViewApiAvailability")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_budgeting)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val kategori = arrayOf(
            "perencana", "pelaksana", "pengawas", "honorium", "perjalanan_dinas", "habis_pakai"
        )

        val adapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            kategori // Array
        )

        ed_kepada.setAdapter(adapter)
        ed_kepada.threshold = 1

        ed_kepada.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                ed_kepada.showDropDown()
            }
        }

        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = intent.getStringExtra("nilai")?.toInt()
        val formattedNumber: String = formatter.format(myNumber)

        textView4.text = intent.getStringExtra("proyek")

        tv_tgl.text = intent.getStringExtra("tanggal")
        tv_kepada.text = intent.getStringExtra("kategori")
        tv_rincian.text = intent.getStringExtra("rincian")
        tv_nilai.text = "Rp. " + formattedNumber
        idtxt_path.text = intent.getStringExtra("surat")
//        tv_surat.text = intent.getStringExtra("surat")

        btneditbudget.setOnClickListener { Editkan() }
        pickButton.setOnClickListener { checkPermissionsAndOpenFilePicker() }

        ed_tgl.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val klik = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        ed_tgl.setText("" + year + "-" + (month + 1) + "-" + dayOfMonth)
                    },
                    year,
                    month,
                    day
                )
                klik.show()
            } else {
                Log.d("eror version android", "wah eror")
            }
        }

        ed_tgl.setText(intent.getStringExtra("tanggal"))
        ed_kepada.setText(intent.getStringExtra("kategori"))
        ed_rincian.setText(intent.getStringExtra("rincian"))
        ed_nilai.setText(intent.getStringExtra("nilai"))
//        ed_surat.setText(intent.getStringExtra("surat"))
//        pdf
        val file = "upload/surat/" + intent.getStringExtra("surat")

        val url = NAMA_DOMAIN + file
//        val url="http://simanis.konseparsitek.com/coba/cobapdf/75"
//        Toast.makeText(applicationContext,"link :"+url, Toast.LENGTH_SHORT).show()

        WebviewPelaksana.getSettings().setJavaScriptEnabled(true)
        WebviewPelaksana.getSettings().setBuiltInZoomControls(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WebviewPelaksana.webViewClient
        }
        WebviewPelaksana.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url)
//        WebviewPelaksana.loadUrl(url)
    }

    private fun checkPermissionsAndOpenFilePicker() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            openFilePicker()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showError()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openFilePicker()
            } else {
                showError()
            }
        }
    }

    private fun openFilePicker() {
        val externalStorage = Environment.getExternalStorageDirectory()
        val alarmsFolder = File(externalStorage, ALARMS_EXTERNAL_STORAGE_FOLDER)

        MaterialFilePicker()
            // Pass a source of context. Can be:
            //    .withActivity(Activity activity)
            //    .withFragment(Fragment fragment)
            //    .withSupportFragment(androidx.fragment.app.Fragment fragment)
            .withActivity(this)
            // With cross icon on the right side of toolbar for closing picker straight away
            .withCloseMenu(true)
            // Entry point path (user will start from it)
//            .withPath(alarmsFolder.absolutePath)
            // Root path (user won't be able to come higher than it)
            .withRootPath("/storage/")
            // Showing hidden files
            .withHiddenFiles(true)
            // Want to choose only jpg images
            .withFilter(Pattern.compile(".*\\.(pdf)$"))
            // Don't apply filter to directories names
            .withFilterDirectories(false)
            .withTitle("Sample title")
            .withRequestCode(FILE_PICKER_REQUEST_CODE)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data ?: throw IllegalArgumentException("data must not be null")

            val path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

            if (path != null) {
                pdfPath = path
                kirimsurat()
                Log.d("Path: ", path)
                Toast.makeText(this, "Picked file: $path", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun kirimsurat() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val file = File(pdfPath)

        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )
        Log.d("saske", file.name)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.upload(filePart)
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.cause.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status != "Gagal") {
                            namapdf = response.body()?.status.toString()
                            idtxt_path.text = namapdf
//                            Toast.makeText(applicationContext, ""+response.body()?.status, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Gagal mendapatkan file, silahkan coba lagi",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal mendapatkan file, silahkan coba lagi",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun Editkan() {

        tv_tgl.visibility = View.GONE
        tv_kepada.visibility = View.GONE
        tv_rincian.visibility = View.GONE
        tv_nilai.visibility = View.GONE
        btn_ganti_pdf.visibility = View.GONE
        btneditbudget.visibility = View.GONE

        ed_tgl.visibility = View.VISIBLE
        ed_kepada.visibility = View.VISIBLE
        ed_rincian.visibility = View.VISIBLE
        ed_nilai.visibility = View.VISIBLE
        btn_ganti_pdf.visibility = View.VISIBLE
        btnsavebubget.visibility = View.VISIBLE

        btnsavebubget.setOnClickListener { updateBudget() }
    }

    private fun updateBudget() {
        if (namapdf == "Gagal" && ed_tgl == null && ed_kepada == null && ed_rincian == null && ed_nilai == null && ed_nilai == null) {
            Toast.makeText(applicationContext, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            RetrofitClient.instance.EditBudget(
                intent.getStringExtra("id"),
                ed_tgl.text.toString(),
                ed_kepada.text.toString(),
                ed_rincian.text.toString(),
                ed_nilai.text.toString(),
                idtxt_path.text.toString()
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
                            if (response.body()?.status == "Berhasil Update") {
                                Log.d("laptop", response.body().toString())
                                tv_tgl.visibility = View.VISIBLE
                                tv_kepada.visibility = View.VISIBLE
                                tv_rincian.visibility = View.VISIBLE
                                tv_nilai.visibility = View.VISIBLE
//                                tv_surat.visibility = View.VISIBLE
                                btneditbudget.visibility = View.VISIBLE

                                ed_tgl.visibility = View.GONE
                                ed_kepada.visibility = View.GONE
                                ed_rincian.visibility = View.GONE
                                ed_nilai.visibility = View.GONE
//                                ed_surat.visibility = View.GONE
                                btnsavebubget.visibility = View.GONE

                                finish()
                            }
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

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
        private const val FILE_PICKER_REQUEST_CODE = 1
        private const val ALARMS_EXTERNAL_STORAGE_FOLDER = "Alarms"
    }
}
