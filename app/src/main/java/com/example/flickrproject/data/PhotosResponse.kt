package com.example.flickrproject.data

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos")
    val photos: Photos? = null
) {
        data class Photos(
            @SerializedName("page")
            val page: Int? = null,

            @SerializedName("pages")
            val pages: Int? = null,

            @SerializedName("perpage")
            val perPage: Int? = null,

            @SerializedName("total")
            val totalPages: Int? = null,

            @SerializedName("photo")
            val photosList: List<PhotoData>? = null

        ) {
                data class PhotoData(
                    @SerializedName("id")
                    val id: String? = null,

                    @SerializedName("owner")
                    val owner: String? = null,

                    @SerializedName("secret")
                    val secret: String? = null,

                    @SerializedName("server")
                    val server: String? = null,

                    @SerializedName("datetaken")
                    val dateTaken: String? = null,

                    @SerializedName("description")
                    val description: Description? = null

                ) {
                        data class Description(
                            @SerializedName("_content")
                            val content: String? = null
                        )
                }
        }
}
