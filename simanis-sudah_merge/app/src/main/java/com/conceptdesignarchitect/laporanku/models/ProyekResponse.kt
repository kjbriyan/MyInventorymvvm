package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class ProyekResponse(

	@field:SerializedName("data_proyek")
	val dataProyek: List<DataProyekItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)