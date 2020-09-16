package com.conceptdesignarchitect.laporanku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_pengawas.RealisasiActivity
import com.conceptdesignarchitect.laporanku.models.DataDetailpekerjaanItem
import kotlinx.android.synthetic.main.list_detail_pekerjaan.view.*

class PekerjaanAdapter(
    val data: List<DataDetailpekerjaanItem>?,
    val itemClickListener: RealisasiActivity
) :
    RecyclerView.Adapter<PekerjaanAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PekerjaanAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_detail_pekerjaan, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: PekerjaanAdapter.MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(get: DataDetailpekerjaanItem?, clickListener: RealisasiActivity) {
            itemView.id_txt_nama_proyek.text = get?.uraianPekerjaan
            val bobot = "${get?.bobotIndividu} %"
            itemView.id_txt_selisih.text = bobot
            itemView.tv_vol_kontrak.text = "Volume asli : " + get?.volume.toString()
            itemView.tv_bobot_kontrak.visibility = View.VISIBLE
            itemView.tv_bobot_kontrak.text = "Bobot asli : " + get?.bobot.toString()
            itemView.tv_harga_satuan.visibility = View.VISIBLE
            itemView.tv_harga_satuan.text = "Harga satuan : ${get?.hargaSatuan.toString()}"
            itemView.tv_nilai.visibility = View.VISIBLE
            itemView.tv_nilai.text = "Nilai : ${get?.nilai.toString()}"

            itemView.setOnClickListener { clickListener.onItemClicked(get) }
        }
    }

}