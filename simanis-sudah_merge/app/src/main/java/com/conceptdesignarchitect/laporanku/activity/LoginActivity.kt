package com.conceptdesignarchitect.laporanku.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.RetrofitClient
import com.conceptdesignarchitect.laporanku.models.LoginResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit.MainKpaActivity
import com.conceptdesignarchitect.laporanku.activity_pengawas.MainPengawasActivity
import com.conceptdesignarchitect.laporanku.activity_ppk.MainPPKActivity
import com.conceptdesignarchitect.laporanku.activity_ppspm.MainPpspmActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logicUser()
    }

    override fun onResume() {
        super.onResume()
        btnLogin.setOnClickListener {
            val email = etNama.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if(email.isEmpty()){
                etNama.error = "Email required"
                etNama.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                etPassword.error = "Password required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(email, password)
                .enqueue(object: Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        Log.i("Response", response.body().toString())
                        if(response.body()?.status=="Berhasil"){
                            SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)
                            when(response.body()?.posisi){
                                "Pengawas" -> pengawas()
                                "PPK" -> ppk()
                                "KPA" -> kpa()
                                "PPSPM" -> ppspm()
                                "KASUBDIT" -> kasubdit()
                                else -> Toast.makeText(applicationContext, "Gagal Mendeteksi Posisi", Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(applicationContext, response.body()?.status, Toast.LENGTH_LONG).show()
                        }

                    }

                })

        }
    }

    fun logicUser(){
        when (SharedPrefManager.getInstance(this).user.posisi) {
            "Pengawas" -> pengawas()
            "PPK" -> ppk()
            "KPA" -> kpa()
            "PPSPM" -> ppspm()
            "KASUBDIT" -> kpa()
            else -> print("Gagal mendeteksi posisi")
        }
    }

    fun pengawas(){
        val intent = Intent(applicationContext, MainPengawasActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun ppk() {
        val intent = Intent(applicationContext, MainPPKActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun kpa() {
        val intent = Intent(applicationContext, MainKpaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun ppspm() {
        val intent = Intent(applicationContext, MainPpspmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun kasubdit() {
        val intent = Intent(applicationContext, MainKpaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
