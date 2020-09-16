package com.conceptdesignarchitect.laporanku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_kpa_kasubdit.CekRealisasiActivity
import com.conceptdesignarchitect.laporanku.models.DataDetailpekerjaanItem
import kotlinx.android.synthetic.main.list_detail_pekerjaan.view.*

class CekPekerjaanAdapter (val data:List<DataDetailpekerjaanItem>?, val itemClickListener: CekRealisasiActivity):
    RecyclerView.Adapter<CekPekerjaanAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CekPekerjaanAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_detail_pekerjaan, parent,false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int =data?.size?:0

    override fun onBindViewHolder(holder: CekPekerjaanAdapter.MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }

    inner class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(get:  DataDetailpekerjaanItem?, clickListener: CekRealisasiActivity){
            itemView.id_txt_nama_proyek.text = get?.uraianPekerjaan
            val bobot = "${get?.bobotIndividu} %"
            itemView.id_txt_selisih.text = bobot

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }
}