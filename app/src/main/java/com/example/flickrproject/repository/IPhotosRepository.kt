package com.example.flickrproject.repository

import com.example.flickrproject.data.PhotoItem
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface IPhotosRepository {
    /**
     * Get the photos data
     */
    fun getPhotosDataAsync(): Observable<List<PhotoItem>>

    /**
     * Fetch data by input
     */
    fun fetchPhotosBySearchTerm(input : String): Disposable?

    /**
     * Fetch data
     */
    fun fetchPhotosBySearchTerm(): Observable<List<PhotoItem>?>?
}