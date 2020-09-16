package com.conceptdesignarchitect.laporanku.activity.model

import com.google.gson.annotations.SerializedName

data class ResponseHistory(

	@field:SerializedName("data_history")
	val dataHistory: List<DataHistoryItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataHistoryItem(

	@field:SerializedName("tgl_akhir")
	val tglAkhir: String? = null,

	@field:SerializedName("bobot_total")
	val bobotTotal: String? = null,

	@field:SerializedName("pdf")
	val pdf: String? = null,

	@field:SerializedName("minggu")
	val minggu: String? = null,

	@field:SerializedName("tgl_awal")
	val tglAwal: String? = null,

	@field:SerializedName("volume_total")
	val volumeTotal: String? = null,

	@field:SerializedName("proyek")
	val proyek: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
