package com.conceptdesignarchitect.laporanku.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.models.LoginResponse
import com.conceptdesignarchitect.laporanku.session.SharedPrefManager

import kotlinx.android.synthetic.main.activity_profil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilActivity : AppCompatActivity() {

    var id : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val name = SharedPrefManager.getInstance(this).user.nama
        val email = SharedPrefManager.getInstance(this).user.email
        val nip = SharedPrefManager.getInstance(this).user.nip
        id = SharedPrefManager.getInstance(this).user.id.toString()

        et_nama.setText(""+name)
        et_nip.setText(""+nip)
        et_email.setText(""+email)

        tolbar.setNavigationOnClickListener {
            finish()
        }

        btn_pasword.setOnClickListener {
            alertDialogPass()
        }

        tolbar.setOnMenuItemClickListener {menuItem->
            when(menuItem.itemId){
                R.id.itm_logout->{
               alertDialogLogout()
                    true
                }
                else->false
            }
        }
        btnSave()
    }

    fun alertDialogLogout(){
        val builder = AlertDialog.Builder(this@ProfilActivity)

        // Set the alert dialog title
        builder.setTitle("Logout")

        // Display a message on alert dialog
        builder.setMessage("Are you want to Logout?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES"){dialog, which ->
            SharedPrefManager.getInstance(this).clear()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
            Log.d("ProfilActivity","Cancel Dialog")
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    fun alertDialogPass(){
        val builder = AlertDialog.Builder(this@ProfilActivity)
        val view = layoutInflater.inflate(R.layout.edit_password_dialog, null)
        builder.setView(view)
        // Set the alert dialog title
        builder.setTitle("Password")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save"){dialog, which ->
            checkPass(view)
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
            Log.d("ProfilActivity","Cancel Dialog")
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    fun btnSave(){
        btn_save.setOnClickListener {
            postProfil()
        }
    }

    fun postProfil(){

        val name = et_nama.text.toString()
        val email = et_email.text.toString()
        val nip = et_nip.text.toString()
        Log.d("kntl"," "+id+ nip+ name+ email)
        val init = Initretrofit().getInstance().ubahProfil(id,nip,name,email)
        init.enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("ProfilActivity",""+t.cause)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
               if (response.isSuccessful){
                   if (response.body()?.status.toString().equals("Berhasil Update")){
                       Toast.makeText(applicationContext, "Berhasil Update", Toast.LENGTH_LONG).show()
                       SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)
                       finish()
                   }
               }else{
                   Toast.makeText(applicationContext, "Fail Update", Toast.LENGTH_LONG).show()
               }
            }

        })
    }

    fun checkPass(view : View){
        val pass = view.findViewById<EditText>(R.id.et_password).text.toString()
        val checkpass = view.findViewById<EditText>(R.id.et_checkpass).text.toString()
        val oldPass = SharedPrefManager.getInstance(this).user.password.toString()
        val etOldpass = view.findViewById<EditText>(R.id.et_oldpass).text.toString()

        if (oldPass == etOldpass) {
            if (pass != checkpass) {
                Toast.makeText(applicationContext, "Password not match"+pass + checkpass, Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(applicationContext, "Password changed", Toast.LENGTH_LONG).show()
                postPass(pass.toString())
            }
        }else{
            Toast.makeText(applicationContext, "Old password not match", Toast.LENGTH_LONG).show()
        }
    }

    fun postPass(pass : String){

        val init = Initretrofit().getInstance().ubahpass(id,pass)
        init.enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("ProfilActivity",""+t.cause)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val res  = response.body()
                if (response.isSuccessful){
                       if (res?.status.toString() == "Berhasil Update"){
                           SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)
                           Log.d("ProfilActivity","succes")
                       }else{
                           Log.d("ProfilActivity","gak sukes")
                       }
                }
            }

        })
    }
}
