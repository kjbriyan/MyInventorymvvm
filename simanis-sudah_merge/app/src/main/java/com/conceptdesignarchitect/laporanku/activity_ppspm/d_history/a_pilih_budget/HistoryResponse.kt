package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget

import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget.DataHistoryItem
import com.google.gson.annotations.SerializedName

data class HistoryResponse(

    @field:SerializedName("data_history")
	val dataHistory: List<DataHistoryItem?>? = null,

    @field:SerializedName("status")
	val status: String? = null
)