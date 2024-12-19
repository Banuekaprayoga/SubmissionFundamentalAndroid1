package com.dicoding.dicodingeventsubmission.ui.upcoming

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventsubmission.R
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.databinding.VerticalEventRowBinding
import com.dicoding.dicodingeventsubmission.ui.detail.DetailEventActivity
import com.dicoding.dicodingeventsubmission.ui.detail.DetailEventActivity.Companion.DETAIL_EVENT

class UpcomingAdapter : ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VerticalEventRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: VerticalEventRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listEvent: ListEventsItem){
            binding.tvTitleEvent.text = listEvent.name
            val image = listEvent.imageLogo
            Glide.with(binding.imageView)
                .load(image)
                .into(binding.imageView)
            binding.tvCategory.text = listEvent.category
            val quota = if (listEvent.registrants != null) {
                listEvent.quota?.minus(listEvent.registrants)
            } else {
                listEvent.quota
            }
            binding.tvQuota.text = itemView.context.getString(R.string.quota, quota)
            binding.tvOwner.text = itemView.context.getString(R.string.owner, listEvent.ownerName)
            binding.tvSummary.text = listEvent.summary
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