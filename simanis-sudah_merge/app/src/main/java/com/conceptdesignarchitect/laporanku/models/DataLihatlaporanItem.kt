package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class DataLihatlaporanItem(

	@field:SerializedName("tgl_pertama")
	val tglPertama: String? = null,

	@field:SerializedName("id_minggu")
	val idMinggu: String? = null,

	@field:SerializedName("minggu")
	val minggu: String? = null,

	@field:SerializedName("tgl_kedua")
	val tglKedua: String? = null,

	@field:SerializedName("total_bobot")
	val totalBobot: String? = null,

	@field:SerializedName("nama_proyek")
	val namaProyek: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)