package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class BobotmingguResponse(

	@field:SerializedName("data_total_bobot")
	val dataTotalBobot: List<DataTotalBobotItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)