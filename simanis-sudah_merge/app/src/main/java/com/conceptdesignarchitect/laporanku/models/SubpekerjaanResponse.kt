package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class SubpekerjaanResponse(

	@field:SerializedName("data_subpekerjaan")
	val dataSubpekerjaan: List<DataSubpekerjaanItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)