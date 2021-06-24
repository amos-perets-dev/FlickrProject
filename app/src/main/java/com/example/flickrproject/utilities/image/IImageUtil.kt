package com.example.flickrproject.utilities.image

import android.graphics.Bitmap
import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface IImageUtil {

    /**
     * Load tne image from the server
     */
    fun loadImage(
        url: String?
    )
}