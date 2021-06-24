package com.example.flickrproject.network.api

import com.example.flickrproject.BuildConfig
import com.example.flickrproject.data.PhotosResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {
    companion object {
        private const val API_KEY = "&api_key=${BuildConfig.FLICKR_API_KEY}"
        private const val METHOD = "method"
        private const val EXTRAS = "extras"
        private const val SEARCH_PHOTOS_METHOD = "?$METHOD=flickr.photos.search"
        private const val SUFFIX_URL = "&format=json&nojsoncallback=1"
    }

    interface SearchImagesApi {

        companion object {
            private const val EXTRAS_INFORMATION = "&$EXTRAS=date_taken,description"
            private const val SEARCH_TERM = "text"
            private const val PAGE = "page"
            const val SEARCH_PHOTOS_URL =
                "$SEARCH_PHOTOS_METHOD$API_KEY$EXTRAS_INFORMATION$SUFFIX_URL"
        }

        /**
         * Get the images by the input
         *
         * @param input - search term
         * @param page - page number
         * @return [Observable][PhotosResponse]
         */
        @GET(SEARCH_PHOTOS_URL)
        fun searchImages(@Query(SEARCH_TERM) input: String, @Query(PAGE) page: String): Observable<PhotosResponse>

    }


}