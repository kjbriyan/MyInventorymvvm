package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class ResponseDokumentasi(

	@field:SerializedName("datafoto")
	val datafoto: List<DatafotoItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DatafotoItem(

	@field:SerializedName("idfoto")
	val idfoto: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("subpekerjaan")
	val subpekerjaan: String? = null,

	@field:SerializedName("section")
	val section: String? = null,

	@field:SerializedName("idminggu")
	val idminggu: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null
)
