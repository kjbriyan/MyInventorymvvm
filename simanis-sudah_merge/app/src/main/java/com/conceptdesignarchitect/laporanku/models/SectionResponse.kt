package com.conceptdesignarchitect.laporanku.models

import com.google.gson.annotations.SerializedName

data class SectionResponse(

	@field:SerializedName("data_section")
	val dataSection: List<DataSectionItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)