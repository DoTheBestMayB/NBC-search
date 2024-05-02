package com.dothebestmayb.nbc_search.presentation.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dothebestmayb.nbc_search.R
import com.dothebestmayb.nbc_search.databinding.ItemSearchImageBinding
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import java.util.Locale

class StoreAdapter(
    private val onClick: (SearchListItem) -> Unit,
) : ListAdapter<SearchListItem, RecyclerView.ViewHolder>(diff) {

    class ImageViewHolder(
        private val binding: ItemSearchImageBinding,
        private val onClick: (SearchListItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: SearchListItem
        private val simpleDataFormat =
            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

        init {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }

        fun bind(item: SearchListItem) {
            this.item = item

            binding.ivThumbnail.load(item.thumbnail) {
                placeholder(R.drawable.placeholder_for_load)
            }
            binding.tvSite.text = item.siteName
            binding.tvPostDate.text = simpleDataFormat.format(item.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ImageViewHolder -> holder.bind(item)
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<SearchListItem>() {
            override fun areItemsTheSame(
                oldItem: SearchListItem,
                newItem: SearchListItem
            ): Boolean {
                return oldItem.thumbnail == newItem.thumbnail
            }

            override fun areContentsTheSame(
                oldItem: SearchListItem,
                newItem: SearchListItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}