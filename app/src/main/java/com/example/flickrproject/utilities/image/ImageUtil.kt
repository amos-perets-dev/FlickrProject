package com.example.flickrproject.utilities.image

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flickrproject.network.api.image.IImageApi
import com.example.flickrproject.network.api.image.ImageApi
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class ImageUtil(
    private val context: Context
) : IImageUtil {

    override fun loadImage(url: String?) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .preload()
    }


}