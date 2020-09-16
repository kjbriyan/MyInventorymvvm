package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DataDetailpekerjaanItem(

	@field:SerializedName("bobot_detail")
	val bobotDetail: String? = null,

	@field:SerializedName("bobot")
	val bobot: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("uraianid")
	val uraianid: String? = null,

	@field:SerializedName("section")
	val section: String? = null,

	@field:SerializedName("proyek")
	val proyek: String? = null,

	@field:SerializedName("volume_akhir")
	val volumeAkhir: String? = null,

	@field:SerializedName("volume")
	val volume: String? = null,

	@field:SerializedName("idkerja")
	val idkerja: String? = null,

	@field:SerializedName("bobot_akhir")
	val bobotAkhir: String? = null,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String? = null,

	@field:SerializedName("harga_satuan")
	val hargaSatuan: String? = null,

	@field:SerializedName("uraian_pekerjaan")
	val uraianPekerjaan: String? = null,

	@field:SerializedName("satuan")
	val satuan: String? = null,

	@field:SerializedName("fk_id_new_pekerjaan")
	val fkIdNewPekerjaan: String? = null,

	@field:SerializedName("volume_detail")
	val volumeDetail: String? = null,

	@field:SerializedName("fk_id_minggu")
	val fkIdMinggu: String? = null,

	@field:SerializedName("bobot_individu")
	val bobotIndividu: String? = null



)