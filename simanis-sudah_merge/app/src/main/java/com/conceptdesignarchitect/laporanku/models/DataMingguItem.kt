package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DataMingguItem(

	@field:SerializedName("tgl_akhir")
	val tglAkhir: String? = null,

	@field:SerializedName("minggu")
	val minggu: String? = null,

	@field:SerializedName("tgl_awal")
	val tglAwal: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)