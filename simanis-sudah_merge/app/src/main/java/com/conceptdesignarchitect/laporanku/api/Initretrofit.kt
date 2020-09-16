package com.conceptdesignarchitect.laporanku.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Initretrofit {

    val NAMA_DOMAIN = "http://simanis.konseparsitek.com/"
    val imguri = NAMA_DOMAIN+"upload/dokumentasi/"
    private val BASE_URL = NAMA_DOMAIN+"api/"

    fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstance(): Api {
        return initRetrofit().create(Api::class.java )
    }
}