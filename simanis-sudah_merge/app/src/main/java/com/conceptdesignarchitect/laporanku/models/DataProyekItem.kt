package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DataProyekItem(

	@field:SerializedName("proyek")
	val proyek: String? = null,

	@field:SerializedName("total_bobot")
	val totalBobot: String? = null,

	@field:SerializedName("tersisa")
	val tersisa: Int? = null
)