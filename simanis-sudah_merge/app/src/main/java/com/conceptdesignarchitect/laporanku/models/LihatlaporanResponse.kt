package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class LihatlaporanResponse(

	@field:SerializedName("data_lihatlaporan")
	val dataLihatlaporan: List<DataLihatlaporanItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)