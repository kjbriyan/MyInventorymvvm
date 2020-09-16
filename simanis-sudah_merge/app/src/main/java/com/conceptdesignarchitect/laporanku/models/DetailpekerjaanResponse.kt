package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DetailpekerjaanResponse(

	@field:SerializedName("data_detailpekerjaan")
	val dataDetailpekerjaan: List<DataDetailpekerjaanItem?>? = null,


	@field:SerializedName("status")
	val status: String? = null
)