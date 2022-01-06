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
import com.mohamedsayed.picsumphotoviewer.databinding.ImagesListItemBinding
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

class ImagesListAdapter(
    private val context: Context,
    private val onItemClick: (PicsumImage?) -> Unit,
) : PagingDataAdapter<PicsumImage,ImagesListAdapter.ImagesListAdapterHolder>(PicsumImageComparator) {

    inner class ImagesListAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ImagesListItemBinding.bind(itemView)

        val imageAuthorTV = binding.imageAuthorTV
        val imageIV = binding.imageIV
        val imagePBar = binding.imagePBar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesListAdapterHolder {
        return ImagesListAdapterHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.images_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesListAdapterHolder, position: Int) {


        val item = getItem(position)

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

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    object PicsumImageComparator : DiffUtil.ItemCallback<PicsumImage>() {
        override fun areItemsTheSame(oldItem: PicsumImage, newItem: PicsumImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PicsumImage, newItem: PicsumImage): Boolean {
            return oldItem == newItem
        }
    }
}