package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DataTotalBobotItem(

	@field:SerializedName("minggu_ini")
	val mingguIni: String? = null,

	@field:SerializedName("minggu_lalu")
	val mingguLalu: String? = null,

	@field:SerializedName("sd_minggu_ini")
	val sdMingguIni: String? = null
)