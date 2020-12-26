package com.nanamare.sample

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import com.nanamare.sample.databinding.ItemMusicAdsBinding
import com.nanamare.sample.databinding.ItemMusicContentBinding
import com.nanamare.sample.databinding.ItemMusicContentFooterBinding
import com.nanamare.sample.databinding.ItemMusicContentHeaderBinding
import kotlinx.parcelize.Parcelize

class MusicViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class MusicAdapter(
        val onHeaderClicked: (Item.Header) -> Unit,
        val onContentClicked: (Item.Content) -> Unit,
        val onFooterClicked: (Item.Footer) -> Unit,
        val onAdsClicked: () -> Unit
) : ListAdapter<Item, MusicViewHolder>(DiffUtilItemCallback) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = when (val item = getItem(position)) {
        is Item.Header -> item.music.id.toLong()
        is Item.Content -> item.music.id.toLong()
        is Item.Footer -> item.music.id.toLong()
        is Item.Ads -> item.id.toLong()
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is Item.Header -> VIEW_TYPE_HEADER
        is Item.Content -> VIEW_TYPE_CONTENT
        is Item.Footer -> VIEW_TYPE_FOOTER
        is Item.Ads -> VIEW_TYPE_ADS
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MusicViewHolder = when (viewType) {
        VIEW_TYPE_HEADER -> {
            MusicViewHolder(binding<ItemMusicContentHeaderBinding>(parent, VIEW_TYPE_HEADER))
        }
        VIEW_TYPE_CONTENT -> {
            MusicViewHolder(binding<ItemMusicContentBinding>(parent, VIEW_TYPE_CONTENT))
        }
        VIEW_TYPE_FOOTER -> {
            MusicViewHolder(binding<ItemMusicContentFooterBinding>(parent, VIEW_TYPE_FOOTER))
        }
        VIEW_TYPE_ADS -> {
            MusicViewHolder(binding<ItemMusicAdsBinding>(parent, VIEW_TYPE_ADS))
        }
        else -> error("invalid viewType")
    }

    private fun <T : ViewDataBinding> binding(parent: ViewGroup, viewType: Int) =
            DataBindingUtil.inflate<T>(LayoutInflater.from(parent.context), viewType, parent, false)

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        when (val binding = holder.binding) {
            is ItemMusicContentHeaderBinding -> {
                val item = convertType<Item.Header>(position)
                binding.header = item
                holder.itemView.setOnClickListener { onHeaderClicked(item) }
            }
            is ItemMusicContentBinding -> {
                val item = convertType<Item.Content>(position)
                binding.content = convertType<Item.Content>(position)
                holder.itemView.setOnClickListener { onContentClicked(item) }
            }
            is ItemMusicContentFooterBinding -> {
                val item = convertType<Item.Footer>(position)
                binding.footer = convertType<Item.Footer>(position)
                holder.itemView.setOnClickListener { onFooterClicked(item) }
            }
            is ItemMusicAdsBinding -> {
                binding.ads = convertType<Item.Ads>(position)
                holder.itemView.setOnClickListener { onAdsClicked() }
            }
        }
    }

    // 확실한 타입일 때 사용
    @Suppress("UNCHECKED_CAST")
    private fun <T> convertType(position: Int) = getItem(position) as T

    override fun onViewRecycled(holder: MusicViewHolder) {
        super.onViewRecycled(holder)
        // animation 등 제거
    }


    companion object {
        private const val VIEW_TYPE_HEADER = R.layout.item_music_content_header
        private const val VIEW_TYPE_CONTENT = R.layout.item_music_content
        private const val VIEW_TYPE_FOOTER = R.layout.item_music_content_footer
        private const val VIEW_TYPE_ADS = R.layout.item_music_ads

        private val DiffUtilItemCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                    oldItem: Item,
                    newItem: Item
            ) = oldItem == newItem

            override fun areContentsTheSame(
                    oldItem: Item,
                    newItem: Item
            ) = oldItem.hashCode() == newItem.hashCode()
        }
    }
}

sealed class Item {
    data class Header(val title: String, val subTitle: String, val music: Music) : Item() {
        override fun toString() = "${music.title}-${music.category}"
    }

    data class Content(val music: Music) : Item() {
        override fun toString() = "${music.title}-${music.category}"
    }

    data class Footer(val title: String, val music: Music) : Item() {
        override fun toString() = "${music.title}-${music.category}"
    }

    data class Ads(val id: Int, val advertisementUrl: String) : Item()
}

@BindingAdapter(value = ["loadImageUrl"])
fun ImageView.loadImageUrl(url: String?) {
    url?.let {
        Glide.with(this)
                .load(it)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(this)
    }
}

@Parcelize
data class Music(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("albumUrl") val albumUrl: String,
        @SerializedName("writer") val writer: String,
        @SerializedName("category") val category: String,
) : Parcelable

