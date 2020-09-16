package com.conceptdesignarchitect.laporanku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_ppk.LihatLaporanActivity
import com.conceptdesignarchitect.laporanku.models.DataLihatlaporanItem
import kotlinx.android.synthetic.main.list_report.view.*

class LihatlaporanAdapter(val data:List<DataLihatlaporanItem>?, val itemCLicklistener: LihatLaporanActivity) :
    RecyclerView.Adapter<LihatlaporanAdapter.MyHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LihatlaporanAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_report,parent,false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int  = data?.size ?: 0

    override fun onBindViewHolder(holder: LihatlaporanAdapter.MyHolder, position: Int) {
        holder.bind(data?.get(position), itemCLicklistener)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(get: DataLihatlaporanItem?, clickListener : LihatLaporanActivity){
            itemView.id_txt_nama_proyek.text = get?.namaProyek
            val minggu = "${get?.minggu} (${get?.tglPertama} - ${get?.tglKedua})"
            itemView.id_txt_minggu.text = minggu
            val total = "Total Bobot : ${get?.totalBobot} %"
            itemView.id_bobot.text = total

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }

//    fun update(modelList:ArrayList<String>){
//        myList = modelList
//        LihatlaporanAdapter.MyHolder!!.notifyDataSetChanged()
//    }

}