package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class CeknotifResponse(

	@field:SerializedName("notif")
	val notif: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)