package com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik

import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik.DataGrafikItem
import com.google.gson.annotations.SerializedName

data class ModelGrafikResponse(

    @field:SerializedName("data_grafik")
	val dataGrafik: List<DataGrafikItem?>? = null,

    @field:SerializedName("status")
	val status: String? = null
)