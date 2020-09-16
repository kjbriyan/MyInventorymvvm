package com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_upload_uang.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.regex.Pattern


open class UploadUangActivity : AppCompatActivity() {
    private val pickButton: Button by lazy { findViewById<Button>(R.id.btn_pilih_surat) }

    private var pdfPath: String? = null

    var nilai : String = ""
    var namapdf = "Gagal"
    var rincian :  String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_uang)

        pickButton.setOnClickListener {
            checkPermissionsAndOpenFilePicker()
        }

        id_btn_kirimuang.setOnClickListener {
//            pdfPath = "/storage/emulated/0/documents/ResumeTomy2020ok.pdf"
            nilai = idtext_nilai.text.toString()
            rincian = idtext_rincian.text.toString()

            if (namapdf != "Gagal" && nilai != "" && rincian != ""){
                kirimuang_update()
            }else{
                Toast.makeText(applicationContext, "Data kurang lengkap", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun kirimuang_update() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RetrofitClient.instance.Kirimuang(intent.getStringExtra("proyek"), intent.getStringExtra("kategori"), rincian, nilai, namapdf)
            .enqueue(object : Callback<KirimResponse> {
                override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.cause.toString(), Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<KirimResponse>,
                    response: Response<KirimResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()?.status == "Berhasil"){
                            Toast.makeText(applicationContext, "Berhasil mengirim", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(applicationContext, "Gagal mendapatkan file, silahkan coba lagi" , Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Gagal mendapatkan file, silahkan coba lagi" , Toast.LENGTH_LONG).show()
                    }
                }
            })
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
                kirimuang()
                Log.d("Path: ", path)
                Toast.makeText(this, "Picked file: $path", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun kirimuang() {
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
                    if (response.isSuccessful){
                        if (response.body()?.status != "Gagal"){
                            namapdf = response.body()?.status.toString()
                            idtxt_path.text = namapdf
//                            Toast.makeText(applicationContext, ""+response.body()?.status, Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, "Gagal mendapatkan file, silahkan coba lagi" , Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Gagal mendapatkan file, silahkan coba lagi" , Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
        private const val FILE_PICKER_REQUEST_CODE = 1
        private const val ALARMS_EXTERNAL_STORAGE_FOLDER = "Alarms"
    }
}
