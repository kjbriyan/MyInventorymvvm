package com.conceptdesignarchitect.laporanku.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.conceptdesignarchitect.laporanku.R
import com.conceptdesignarchitect.laporanku.activity_pengawas.UploadFotoActivity
import com.conceptdesignarchitect.laporanku.api.Initretrofit
import com.conceptdesignarchitect.laporanku.models.DatafotoItem

class RvAdapterPembuktian(private val data :List<DatafotoItem?>?,val itemClickListener: UploadFotoActivity) : RecyclerView.Adapter<Myholder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_img_bukti,parent,false)
        return Myholder(v)
    }

    override fun getItemCount(): Int  = data?.size ?: 0

    override fun onBindViewHolder(holder: Myholder, position: Int) {
        holder.bindview(data?.get(position)!!,itemClickListener)
    }

}

class Myholder(itemview : View):RecyclerView.ViewHolder(itemview) {

    fun bindview(data : DatafotoItem, clickListener: UploadFotoActivity){
        val img = Initretrofit().imguri+data.foto
        val tv = itemView.findViewById<ImageView>(R.id.iv_bukti)
        val circularProgressDrawable = CircularProgressDrawable(itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(itemView.context)
            .load(img)
            .placeholder(circularProgressDrawable)
            .into(tv)
        Log.d("sianu",img)
        tv.setOnClickListener { clickListener.onItemClicked(data) }
    }
}
