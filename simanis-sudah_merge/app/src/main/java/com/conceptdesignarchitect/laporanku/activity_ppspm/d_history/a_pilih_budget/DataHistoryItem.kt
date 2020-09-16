package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget

import com.google.gson.annotations.SerializedName

data class DataHistoryItem(

	@field:SerializedName("rincian")
	val rincian: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("surat")
	val surat: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("proyek")
	val proyek: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("fk_id_kontruksi_history")
	val fkIdKontruksiHistory: String? = null
)