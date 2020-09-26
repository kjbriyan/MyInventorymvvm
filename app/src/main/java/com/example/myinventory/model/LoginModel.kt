package com.example.myinventory.model

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.constraintlayout.solver.widgets.Helper
import androidx.databinding.BaseObservable
import com.example.myinventory.network.Initretrofit
import com.example.myinventory.view.HomeActivity
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginModel(private var email: String?, private var password: String?) : BaseObservable() {


    fun getPassword(): String? {
        return password
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

}