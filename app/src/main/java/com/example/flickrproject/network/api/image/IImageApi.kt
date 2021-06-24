package com.example.flickrproject.network.api.image

interface IImageApi {

    /**
     * Create small photo url
     */
    fun buildSmallPhotoUrl(): String

    /**
     * Create medium photo url
     */
    fun buildMediumPhotoUrl(): String

    /**
     * Add parameters to create Url
     */
    fun addParameters(serverId: String?, id: String?, secret: String?): ImageApi
}