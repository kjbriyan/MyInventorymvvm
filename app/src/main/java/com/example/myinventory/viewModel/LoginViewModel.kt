package com.example.myinventory.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.myinventory.R
import com.example.myinventory.callback.LoginResultsCallback
import com.example.myinventory.model.LoginModel
import com.example.myinventory.model.ResponseLogin
import com.example.myinventory.network.Initretrofit
import kotlinx.android.synthetic.main.activity_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val listener: LoginResultsCallback) : ViewModel() {
    private val user: LoginModel

    init {
        this.user = LoginModel("", "")
    }

    val emailTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                user.setEmail(p0.toString())
            }

        }

    val passwordTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                user.setPassword(p0.toString())
            }

        }



//    fun onLoginClicked(v: View) {
//        getdataa()
//
//    }

//    fun getdata(){
//        var job = Job()
//        val scope = CoroutineScope(job +Dispatchers.Main)
//        scope.launch{
//           try {
//               val init = Initretrofit().getInstance()
//                   .userLogin(user.getEmail().toString(), user.getPassword().toString())
//
//               if (init.isExecuted == true){
//                   listener.onSuccess("success login")
//               }else{
//                   listener.onFailur("wrong email or password")
//               }
//           }catch (e : Throwable){
//               listener.onFailur(e.cause.toString())
//           }
//        }
//    }

    fun getdataa(emm : String , pass : String) {

        val init =
            Initretrofit().getInstance()
                .userLogin(emm, pass)
        Log.d("kirim", user.getEmail().toString())
        init.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val res = response.body()
                Log.d("getdata", res?.status.toString())
                if (res?.status == 200) {
                    var pas = res.data?.get(0)?.password.toString()
                    var em = res.data?.get(0)?.name.toString()
                    Log.d("getdata", "suk")
                    listener.onSuccess("success login")
                } else {
                    Log.d("getdata", "fail network")
                    listener.onFailur("wrong email or password")
                }

            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
//                Log.d("getdata", "failure " + t.message)
                listener.onFailur(t.cause.toString())
            }

        })
    }


}