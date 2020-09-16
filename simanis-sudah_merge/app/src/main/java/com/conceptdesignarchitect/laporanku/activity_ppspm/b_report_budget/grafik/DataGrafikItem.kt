package com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik

import com.google.gson.annotations.SerializedName

data class DataGrafikItem(

	@field:SerializedName("total_sub_pekerjaan")
	val totalSubPekerjaan: Int? = null,

	@field:SerializedName("proses_pembayaran")
	val prosesPembayaran: String? = null,

	@field:SerializedName("total_nilai_kontrak")
	val totalNilaiKontrak: String? = null,

	@field:SerializedName("total_pekerjaan")
	val totalPekerjaan: String? = null,

	@field:SerializedName("pelaksana")
	val pelaksana: String? = null,

	@field:SerializedName("pengawas")
	val pengawas: String? = null,

	@field:SerializedName("perencana")
	val perencana: String? = null,

	@field:SerializedName("honorium")
	val honorium: String? = null,

	@field:SerializedName("perjalanan_dinas")
	val perjalananDinas: String? = null,

	@field:SerializedName("habis_pakai")
	val habisPakai: String? = null
)