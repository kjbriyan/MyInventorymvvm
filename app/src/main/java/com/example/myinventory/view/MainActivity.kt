package com.example.myinventory.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.myinventory.R
import com.example.myinventory.callback.LoginResultsCallback
import com.example.myinventory.databinding.ActivityMainBinding
import com.example.myinventory.util.Helper
import com.example.myinventory.viewModel.LoginViewModel
import com.example.myinventory.viewModel.LoginViewModelFactory
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LoginResultsCallback {
//    private lateinit var  viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val activityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        activityMainBinding.viewModel = ViewModelProviders.of(this, LoginViewModelFactory(this@MainActivity))
            .get(LoginViewModel::class.java)

        
        //ActivityMainBinding ini akan dibuild otomatis saat anda generate app
    }
    override fun onSuccess(messege: String) {
        Toasty.success(this, messege, Toast.LENGTH_SHORT).show()
        Helper().moveActivity(this,MainHomeActivity::class.java)
    }

    override fun onFailur(messege: String) {
        Toasty.error(this, messege, Toast.LENGTH_SHORT).show()
    }
}