package com.example.myinventory.network

import com.example.myinventory.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("login/login")
    fun userLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>
}