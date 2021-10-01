package com.example.rxjava.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjava.model.Video


class VideoAdapter(val videosList: ArrayList<Video>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    companion object {
        lateinit var clickListener: ClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var serialNoTV: TextView
        var nameTV: TextView
        var downloadImg: ImageView

        init {
            itemView.setOnClickListener(this)
            serialNoTV = view.findViewById(com.example.rxjava.R.id.index_tv)
            nameTV = view.findViewById(com.example.rxjava.R.id.name_tv)
            downloadImg = view.findViewById(com.example.rxjava.R.id.download_img)
        }


        override fun onClick(view: View) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(com.example.rxjava.R.layout.video_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTV.text = videosList[position].videoName
        holder.serialNoTV.text = videosList[position].serialNo.toString()

    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        VideoAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }

    override fun getItemCount() = videosList.size

}