package com.example.myinventory.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myinventory.callback.LoginResultsCallback

class LoginViewModelFactory(private val listener: LoginResultsCallback): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>):T{
        return LoginViewModel(listener) as T
    }
}