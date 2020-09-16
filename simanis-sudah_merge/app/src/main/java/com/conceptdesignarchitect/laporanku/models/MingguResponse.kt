package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class MingguResponse(

	@field:SerializedName("data_minggu")
	val dataMinggu: List<DataMingguItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)