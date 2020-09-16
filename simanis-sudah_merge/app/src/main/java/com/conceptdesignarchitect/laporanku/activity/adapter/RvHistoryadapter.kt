package com.conceptdesignarchitect.laporanku.activity.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity.HistoryActivity
import com.conceptdesignarchitect.laporanku.activity.WebViewHistoryActivity
import com.conceptdesignarchitect.laporanku.activity.model.DataHistoryItem
import kotlinx.android.synthetic.main.list_detail_pekerjaan.view.id_txt_nama_proyek
import kotlinx.android.synthetic.main.list_report.view.*


class RvHistoryadapter(val data: List<DataHistoryItem>?, val itemClickListener: HistoryActivity) :
    RecyclerView.Adapter<RvHistoryadapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHistoryadapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_report, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: RvHistoryadapter.MyHolder, position: Int) {
        holder.bind(data?.get(position), itemClickListener)
    }


    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(get: DataHistoryItem?, clickListener: HistoryActivity) {
            itemView.id_txt_nama_proyek.text = get?.proyek
            val rincian = " ${get?.tglAwal} % | ${get?.tglAkhir} \t ${get?.minggu}"
            itemView.id_txt_minggu.text = rincian
            itemView.id_bobot.text = get?.bobotTotal.toString()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WebViewHistoryActivity::class.java)
                intent.putExtra("proyek", get?.proyek)
                intent.putExtra("pdf",get?.pdf)
                intent.putExtra("idmig",get?.id)
                itemView.context.startActivity(intent)
            }

        }

    }
}
