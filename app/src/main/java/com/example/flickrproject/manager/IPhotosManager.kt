package com.example.flickrproject.manager

import com.example.flickrproject.data.PhotoItem
import com.example.flickrproject.data.PhotosResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface IPhotosManager {
    /**
     * Search photos by input
     */
    fun searchPhotos(input : String) : Disposable

    /**
     * Search photos by page
     */
    fun searchPhotos(page: Int): Observable<List<PhotoItem>?>?

    /**
     * Get the photos data
     */
    fun getData(): Flowable<List<PhotoItem>>?
}