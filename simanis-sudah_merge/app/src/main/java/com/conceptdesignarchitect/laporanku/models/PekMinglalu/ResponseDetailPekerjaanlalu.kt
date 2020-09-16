package com.conceptdesignarchitect.laporanku.models.PekMinglalu

import com.google.gson.annotations.SerializedName

data class ResponseDetailPekerjaanlalu(

	@field:SerializedName("data_detailpekerjaanlalu")
	val dataDetailpekerjaanlalu: List<DataDetailpekerjaanlaluItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataDetailpekerjaanlaluItem(

	@field:SerializedName("bobot_detail")
	val bobotDetail: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("bobot_akhir")
	val bobotAkhir: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("proyek")
	val proyek: String? = null,

	@field:SerializedName("volume_akhir")
	val volumeAkhir: String? = null,

	@field:SerializedName("fk_id_new_pekerjaan")
	val fkIdNewPekerjaan: String? = null,

	@field:SerializedName("volume_detail")
	val volumeDetail: String? = null,

	@field:SerializedName("fk_id_minggu")
	val fkIdMinggu: String? = null
)
