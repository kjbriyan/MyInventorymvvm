package com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.HistoryPpspmActivity
import com.conceptdesignarchitect.laporanku.models.DataProyekItem
import kotlinx.android.synthetic.main.list_detail_pekerjaan.view.*

class HistoryAdapter(val data:List<DataProyekItem>?, val itemClickListener: HistoryPpspmActivity):
    RecyclerView.Adapter<HistoryAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_detail_pekerjaan, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(get: DataProyekItem?, clickListener: HistoryPpspmActivity){
            itemView.id_txt_nama_proyek.text = get?.proyek
            val rincian ="Pekerjaan ${get?.totalBobot} % | ${get?.tersisa} hari tersisa"
            itemView.id_txt_selisih.text = rincian

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }
}