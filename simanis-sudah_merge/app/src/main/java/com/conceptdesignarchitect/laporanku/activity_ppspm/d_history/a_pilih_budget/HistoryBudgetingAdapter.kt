package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import kotlinx.android.synthetic.main.list_detail_pekerjaan.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

class HistoryBudgetingAdapter (val data:List<DataHistoryItem>?, val itemClickListener: HistoryBudgetingActivity):
    RecyclerView.Adapter<HistoryBudgetingAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_detail_pekerjaan, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(get: DataHistoryItem?, clickListener: HistoryBudgetingActivity){
            val formatter: NumberFormat = DecimalFormat("#,###")
            val myNumber = get?.nilai?.toInt()
            val formattedNumber: String = formatter.format(myNumber)

            itemView.id_txt_nama_proyek.text = "Kepada : ${get?.kategori} | Rp. ${formattedNumber} | ${get?.tanggal}"
            val rincian ="${get?.rincian}"
            itemView.id_txt_selisih.text = rincian

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }
}