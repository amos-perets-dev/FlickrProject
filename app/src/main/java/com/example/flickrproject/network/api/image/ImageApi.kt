package com.example.flickrproject.network.api.image

import com.example.flickrproject.BuildConfig

class ImageApi : IImageApi {
    private var serverId: String = ""
    private var id: String = ""
    private var secret: String = ""

    companion object {
        private const val PHOTO_SIZE_SMALL: String = "_m"
        private const val PHOTO_SIZE_MEDIUM: String = ""
    }

    override fun addParameters(serverId: String?, id: String?, secret: String?): ImageApi {
        this.serverId = serverId.toString()
        this.id = id.toString()
        this.secret = secret.toString()
        return this
    }


    override fun buildSmallPhotoUrl(): String {
        return "${BuildConfig.BASE_IMAGE_URL}$serverId/${id}_${secret}$PHOTO_SIZE_SMALL.jpg"
    }

    override fun buildMediumPhotoUrl(): String {
        return "${BuildConfig.BASE_IMAGE_URL}$serverId/${id}_${secret}$PHOTO_SIZE_MEDIUM.jpg"
    }
}
