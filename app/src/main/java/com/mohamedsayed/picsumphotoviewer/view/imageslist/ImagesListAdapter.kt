package com.mohamedsayed.picsumphotoviewer.view.imageslist

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mohamedsayed.picsumphotoviewer.R
import com.mohamedsayed.picsumphotoviewer.databinding.ImagesListAdBinding
import com.mohamedsayed.picsumphotoviewer.databinding.ImagesListItemBinding
import com.mohamedsayed.picsumphotoviewer.model.objects.AdObject
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage


class ImagesListAdapter(
    private val context: Context,
    private val onItemClick: (Any?) -> Unit,
) : PagingDataAdapter<Any, RecyclerView.ViewHolder>(PicsumImageComparator) {

    val ITEM_TYPE_IMAGE = 0
    val ITEM_TYPE_AD = 1

    inner class ImagesListAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ImagesListItemBinding.bind(itemView)

        val imageAuthorTV = binding.imageAuthorTV
        val imageIV = binding.imageIV
        val imagePBar = binding.imagePBar
    }

    inner class AdListAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ImagesListAdBinding.bind(itemView)

        val adIdTV = binding.adIdTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_IMAGE -> ImagesListAdapterHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.images_list_item, parent, false)
            )
            else -> AdListAdapterHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.images_list_ad, parent, false)
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == ITEM_TYPE_IMAGE) {
            val item: PicsumImage = getItem(position) as PicsumImage
            val holder: ImagesListAdapterHolder = viewHolder as ImagesListAdapterHolder

            holder.imageAuthorTV.text = item?.author ?: ""

            Glide.with(context).applyDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.ic_refresh)
            )
                .load(item?.downloadUrl)
                .fitCenter()
                .addListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        e?.printStackTrace()
                        holder.imagePBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.imagePBar.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.imageIV)

            holder.itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        } else {
            val item: AdObject = getItem(position) as AdObject
            val holder: AdListAdapterHolder = viewHolder as AdListAdapterHolder
            holder.adIdTV.text = item.id
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != 0 && (position+1) % 6 == 0) {
            ITEM_TYPE_AD
        } else {
            ITEM_TYPE_IMAGE
        }
    }

    object PicsumImageComparator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is PicsumImage && newItem is PicsumImage) {
                oldItem.id == newItem.id
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is PicsumImage && newItem is PicsumImage) {
                oldItem == newItem
            } else {
                false
            }
        }
    }
}