package com.dicoding.dicodingeventsubmission.ui.finished

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.databinding.HorizontalEventRowBinding
import com.dicoding.dicodingeventsubmission.ui.detail.DetailEventActivity
import com.dicoding.dicodingeventsubmission.ui.detail.DetailEventActivity.Companion.DETAIL_EVENT

class FinishedAdapter : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickedCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HorizontalEventRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: HorizontalEventRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listEvent: ListEventsItem) {
            val image = listEvent.imageLogo
            Glide.with(binding.imgEventPhotoH)
                .load(image)
                .into(binding.imgEventPhotoH)
            binding.tvEventH.text = listEvent.name
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailEventActivity::class.java)
            intentDetail.putExtra(DETAIL_EVENT, list.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    companion object {
        val DIFF_CALLBACK = object :  DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }
}