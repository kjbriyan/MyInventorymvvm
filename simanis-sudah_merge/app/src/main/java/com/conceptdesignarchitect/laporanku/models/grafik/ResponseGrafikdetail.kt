package com.conceptdesignarchitect.laporanku.models.grafik

import com.google.gson.annotations.SerializedName

data class ResponseGrafikdetail(

	@field:SerializedName("data_grafik_detail")
	val dataGrafikDetail: List<DataGrafikDetailItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataGrafikDetailItem(

	@field:SerializedName("rp_kurang_pengawas")
	val rpKurangPengawas: String? = null,

	@field:SerializedName("rp_perjalanan")
	val rpPerjalanan: String? = null,

	@field:SerializedName("rp_asli_perencana")
	val rpAsliPerencana: String? = null,

	@field:SerializedName("persen_pelaksana")
	val persenPelaksana: String? = null,

	@field:SerializedName("persen_perjalanan")
	val persenPerjalanan: String? = null,

	@field:SerializedName("rp_kurang_perencana")
	val rpKurangPerencana: String? = null,

	@field:SerializedName("rp_honorium")
	val rpHonorium: String? = null,

	@field:SerializedName("persen_pengawas")
	val persenPengawas: String? = null,

	@field:SerializedName("rp_kurang_honorium")
	val rpKurangHonorium: String? = null,

	@field:SerializedName("rp_kurang_habis")
	val rpKurangHabis: String? = null,

	@field:SerializedName("rp_asli_honorium")
	val rpAsliHonorium: String? = null,

	@field:SerializedName("rp_asli_perjalanan")
	val rpAsliPerjalanan: String? = null,

	@field:SerializedName("rp_kurang_pelaksana")
	val rpKurangPelaksana: String? = null,

	@field:SerializedName("rp_kurang_perjalanan")
	val rpKurangPerjalanan: String? = null,

	@field:SerializedName("rp_habis")
	val rpHabis: String? = null,

	@field:SerializedName("rp_pengawas")
	val rpPengawas: String? = null,

	@field:SerializedName("rp_pelaksana")
	val rpPelaksana: String? = null,

	@field:SerializedName("persen_perencana")
	val persenPerencana: String? = null,

	@field:SerializedName("rp_asli_habis")
	val rpAsliHabis: String? = null,

	@field:SerializedName("rp_perencana")
	val rpPerencana: String? = null,

	@field:SerializedName("rp_asli_pelaksana")
	val rpAsliPelaksana: String? = null,

	@field:SerializedName("rp_asli_pengawas")
	val rpAsliPengawas: String? = null,

	@field:SerializedName("persen_honorium")
	val persenHonorium: String? = null,

	@field:SerializedName("persen_habis")
	val persenHabis: String? = null
)
