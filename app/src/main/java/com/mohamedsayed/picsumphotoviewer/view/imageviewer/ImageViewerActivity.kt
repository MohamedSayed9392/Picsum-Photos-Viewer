package com.mohamedsayed.picsumphotoviewer.view.imageviewer

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.mohamedsayed.picsumphotoviewer.R
import com.mohamedsayed.picsumphotoviewer.databinding.ActivityImageViewerBinding
import com.mohamedsayed.picsumphotoviewer.helpers.GlideApp
import com.mohamedsayed.picsumphotoviewer.helpers.Q
import com.mohamedsayed.picsumphotoviewer.helpers.visible


class ImageViewerActivity : Activity() {

    private lateinit var binding: ActivityImageViewerBinding
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUrl = intent.extras?.getString(Q.INTENT_KEY_IMAGE_URL)

        GlideApp.with(this)
            .asBitmap()
            .apply(RequestOptions().override(1000, 1000))
            .fitCenter()
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_launcher)
            .listener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    model: Any,
                    target: Target<Bitmap?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any,
                    target: Target<Bitmap?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        val p = Palette.from(resource).generate()
                        // Use generated instance
                        val dominantColor = p.getDarkMutedColor(
                            ContextCompat.getColor(this@ImageViewerActivity, R.color.black)
                        )
                        binding.imageViewerCL.setBackgroundColor(dominantColor)
                    }
                    binding.imagePBar.visible(false)
                    return false
                }
            })
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.imageZI.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}