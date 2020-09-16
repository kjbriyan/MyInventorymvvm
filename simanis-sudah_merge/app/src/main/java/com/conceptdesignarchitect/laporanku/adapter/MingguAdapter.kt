package com.conceptdesignarchitect.laporanku.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_pengawas.MingguActivity
import com.conceptdesignarchitect.laporanku.models.DataMingguItem
import kotlinx.android.synthetic.main.list_minggu.view.*

class MingguAdapter(val data: List<DataMingguItem>?, val itemClickListener: MingguActivity) :
    RecyclerView.Adapter<MingguAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MingguAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_minggu,parent,false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: MingguAdapter.MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }
    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(get: DataMingguItem?, clickListener: MingguActivity){
            itemView.id_txt_mingguke.text = get?.minggu
            val tgl = "${get?.tglAwal} sampai ${get?.tglAkhir}"
            val id = get?.id
            itemView.id_txt_tgl_minggu.text = tgl

//            if (id == "144"){
//                itemView.rl.setBackgroundColor(Color.BLUE)
//            }
//            else if(id=="145"){
//                itemView.rl.setBackgroundColor(Color.RED)
//                }

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }
}