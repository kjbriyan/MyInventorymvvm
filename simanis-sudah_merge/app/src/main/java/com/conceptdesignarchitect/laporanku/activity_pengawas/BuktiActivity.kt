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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.models.KirimResponse
import com.conceptdesignarchitect.laporanku.util.Helper
import kotlinx.android.synthetic.main.activity_bukti.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class BuktiActivity : AppCompatActivity() {
    val REQUEST_CODE_CAMERA = 10
    val REQUEST_CODE_GALLERY = 11
    var filegambar: File? = null
    private val TAG: String = "buktii"
    var imgUri: Uri? = null
    private val pickImg: ImageView by lazy { findViewById<ImageView>(R.id.iv_bukti) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti)
        Log.d(TAG, "oncreate")
        getView()
        checkPermissionsAndOpenFilePicker()
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

    override fun onStart() {
        super.onStart()
        val a = intent.getStringExtra("idming")
        val b = intent.getStringExtra("sec")
        val c = intent.getStringExtra("sub")

        Log.d(TAG, "onStart" + a + b + c)
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()

    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        pickImg.setOnClickListener {
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

    fun getView() {
        var bitmap: Bitmap
        btn_update.setOnClickListener {
            iv_bukti.setDrawingCacheEnabled(true)
            bitmap = iv_bukti.drawingCache

            postimg(bitmap)
        }
        val uri = intent.getStringExtra("img")
        Glide.with(this@BuktiActivity)
            .load(Initretrofit().imguri + uri)
            .into(iv_bukti)
    }


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun open_camera() {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imgUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)

//        punya kanjeng
//        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(camera, REQUEST_CODE_CAMERA)
//        punya kanjeng

    }

    private fun open_galey() {
        val galery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galery, REQUEST_CODE_GALLERY)
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

                        iv_bukti.setImageBitmap(Bitmap.createBitmap(bitmap))

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {

                try {
                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        contentResolver, imgUri
                    )
                    iv_bukti.setImageBitmap(thumbnail)
                    val imageurl = getRealPathFromURI(imgUri)
                    Log.d(
                        "popopopo",
                        "imageurl " + imageurl.toString() + " imgUri " + imgUri.toString()
                    )
//                    val poto = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
//
//                    tmpFile(poto)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

//                kanj
//                val bitmap = data!!.extras!!["data"] as Bitmap
//                tmpFile(bitmap)
//                iv_bukti.setImageBitmap(bitmap)
//                kanj
            }
        }
    }

    //    baru
    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
//    baru

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


        val aspect = bitmap.height / bitmap.width
        val newight = (newheight * aspect).toInt()

        val bitmapp = Bitmap.createScaledBitmap(bitmap, 450, 260, false)
        Log.d(TAG, bitmapp.toString())
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
                        "fail upload image" + res?.status,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    uploadnameimg(res?.status.toString())
                }
            }

        })

    }

    fun uploadnameimg(foto: String) {
        val a = intent.getStringExtra("id")

        val i = Initretrofit().getInstance().editfoto(a, foto)
        i.enqueue(object : Callback<KirimResponse> {
            override fun onFailure(call: Call<KirimResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "fail",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<KirimResponse>, response: Response<KirimResponse>) {
                if (response.body()?.status == "Berhasil Update") {
                    Toast.makeText(
                        applicationContext,
                        "Berhasil upload foto",
                        Toast.LENGTH_LONG
                    ).show()
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