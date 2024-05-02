package com.dothebestmayb.nbc_search.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dothebestmayb.nbc_search.R
import com.dothebestmayb.nbc_search.databinding.ItemSearchImageBinding
import com.dothebestmayb.nbc_search.presentation.model.PayloadType
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import java.util.Locale

class SearchAdapter(private val itemClickListener: SearchItemClickListener) :
    ListAdapter<SearchListItem, RecyclerView.ViewHolder>(diff) {

    class ImageViewHolder(
        private val binding: ItemSearchImageBinding,
        private val itemClickListener: SearchItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: SearchListItem
        private val simpleDataFormat =
            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

        init {
            binding.root.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }

        fun bind(item: SearchListItem) = with(binding) {
            this@ImageViewHolder.item = item

            ivThumbnail.load(item.thumbnail) {
                placeholder(R.drawable.placeholder_for_load)
            }
            tvSite.text = item.siteName
            tvPostDate.text = simpleDataFormat.format(item.date)

            changeBookmarkInfo(item)
        }

        fun changeBookmarkInfo(item: SearchListItem) = with(binding) {
            this@ImageViewHolder.item = item
            ivBookmark.visibility = if (item.bookmarked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(getItem(position) as SearchListItem)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        for (payload in payloads) {
            when (payload) {
                PayloadType.BOOKMARK -> {
                    when (holder) {
                        is ImageViewHolder -> holder.changeBookmarkInfo(getItem(position))
                        else -> {
                            super.onBindViewHolder(holder, position, payloads)
                            return
                        }
                    }
                }
                else -> {
                    super.onBindViewHolder(holder, position, payloads)
                    return
                }
            }
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

            override fun getChangePayload(
                oldItem: SearchListItem,
                newItem: SearchListItem
            ): Any? {
                return if (oldItem.thumbnail == newItem.thumbnail && oldItem.date == newItem.date && oldItem.siteName == newItem.siteName && oldItem.bookmarked != newItem.bookmarked) {
                    PayloadType.BOOKMARK
                } else {
                    null
                }
            }
        }
    }
}