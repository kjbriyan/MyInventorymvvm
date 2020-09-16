package com.conceptdesignarchitect.laporanku.activity_pengawas

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.adapter.RvAdapterPembuktian
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.models.DatafotoItem
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.conceptdesignarchitect.laporanku.models.ResponseDokumentasi
import com.conceptdesignarchitect.laporanku.util.Helper
import kotlinx.android.synthetic.main.activity_bukti.*
import kotlinx.android.synthetic.main.activity_upload_foto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class UploadFotoActivity : AppCompatActivity() {
    private val btnfloat: ImageView by lazy { findViewById<ImageView>(R.id.btn_floataddfoto) }


    private val TAG: String = "Uploadfoto"
    val REQUEST_CODE_CAMERA = 10
    val REQUEST_CODE_GALLERY = 11
    var filegambar: File? = null
    var imgUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_foto)

        checkPermissionsAndOpenFilePicker()

    }

    fun onItemClicked(data: DatafotoItem?) {
        val i = Intent(this, BuktiActivity::class.java)
        i.putExtra("id", data?.idfoto)
        i.putExtra("img", data?.foto)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
//        finish()
//        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()

        Log.d(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        val a = intent.getStringExtra("idming")
        val b = intent.getStringExtra("sec")
        val c = intent.getStringExtra("sub")
        lihatfoto(a, b, c)
        Log.d(TAG, "data id " + a + b + c)

        btnfloat.setOnClickListener {
            val dialogitem =
                arrayOf<CharSequence>("Camera", "Galery")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih")

            builder.setItems(dialogitem, DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    0 -> open_camera()
                    1 -> open_galey()
                }
            })
            builder.create().show()
        }
    }

    private fun open_camera() {
        aksikamera()
    }

    fun aksikamera(){
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imgUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun checkPermissionsAndOpenFilePicker() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
//            Toast.makeText(this, "Disetujui", Toast.LENGTH_SHORT).show()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showError()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
                Toast.makeText(this, "Disetujui", Toast.LENGTH_SHORT).show()
            } else {
                showError()
            }
        }
    }

    private fun open_galey() {
        val galery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galery, REQUEST_CODE_GALLERY)
    }

    fun lihatfoto(id: String, sec: String, sub: String) {
        val init = Initretrofit().getInstance().lihatdokumentasi(id, sub, sec)
        init.enqueue(object : Callback<ResponseDokumentasi> {
            override fun onFailure(call: Call<ResponseDokumentasi>, t: Throwable) {
                Log.d(TAG, t.cause.toString())
            }

            override fun onResponse(
                call: Call<ResponseDokumentasi>,
                response: Response<ResponseDokumentasi>
            ) {
                rv_listbukti.adapter =
                    RvAdapterPembuktian(response.body()?.datafoto, this@UploadFotoActivity)
                RvAdapterPembuktian(
                    response.body()?.datafoto,
                    this@UploadFotoActivity
                ).notifyDataSetChanged()
                val size = response.body()?.datafoto?.size?.toInt()
                if (size != null) {
                    if (size!! == 3) {
                        btn_floataddfoto.visibility = View.GONE
                    }
                }
                Log.d(TAG, "lihat foto" + response.body()?.datafoto?.size)
            }

        })

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    imgUri = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver
                            , imgUri
                        )
                        tmpFile(bitmap)
                        postimg(bitmap)
                        Log.d(TAG, "uri bitmap " + bitmap.toString())
//                        tv_heloo.setImageBitmap(Bitmap.createScaledBitmap(bitmap,400,230,false))
//                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                try {
                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        this.contentResolver, imgUri
                    )
                    tmpFile(thumbnail)
                    postimg(thumbnail)
                    Log.d(TAG, "uri bitmap " + thumbnail.toString())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //    baru
//    fun getRealPathFromURI(contentUri: Uri?): String? {
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
//        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(column_index)
//    }

    private fun tmpFile(bitmap: Bitmap): File? {
        filegambar = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , System.currentTimeMillis().toString() + "_iamge.jpeg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmadata: ByteArray = bos.toByteArray()
        try {
            val fos = FileOutputStream(filegambar)
            fos.write(bitmadata)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filegambar
    }

    fun postimg(bitmap: Bitmap) {
        var newheight = 550

        var aspect = bitmap.height / bitmap.width
        var newight = (newheight * aspect).toInt()

        val bitmapp = Bitmap.createScaledBitmap(bitmap, 450, 260, false)
        val img = Helper().getEncoded64ImageStringFromBitmap(bitmapp)
        val init = Initretrofit().getInstance().upload_foto(img.toString())
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
                    uploadnameimg(res?.status.toString())
                }
            }

        })

    }

    fun uploadnameimg(foto: String) {
        val a = intent.getStringExtra("idming")
        val b = intent.getStringExtra("sec")
        val c = intent.getStringExtra("sub")
        val i = Initretrofit().getInstance().simpanddokumentasi(a, c, b, foto)
        i.enqueue(object : Callback<KirimResponse> {
            override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "fail",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<KirimResponse>, response: Response<KirimResponse>) {
                if (response.body()?.status == "Berhasil") {
                    Toast.makeText(
                        applicationContext,
                        "Berhasil upload foto",
                        Toast.LENGTH_LONG
                    ).show()
                    val a = intent.getStringExtra("idming")
                    val b = intent.getStringExtra("sec")
                    val c = intent.getStringExtra("sub")
                    lihatfoto(a, b, c)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Fail upload foto",
                        Toast.LENGTH_LONG
                    ).show()
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