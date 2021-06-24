package com.example.flickrproject.repository

import android.util.Log
import com.example.flickrproject.data.PhotoItem
import com.example.flickrproject.manager.IPhotosManager
import com.example.flickrproject.subscribePro
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PhotosRepository(private val photosManager: IPhotosManager) : IPhotosRepository {

    private val photosList = BehaviorSubject.create<List<PhotoItem>>()

    private var currIndexPage = 1

    private val cacheMap = HashMap<Int, List<PhotoItem>>()

    private var currInput: String? = null

    override fun fetchPhotosBySearchTerm(input: String): Disposable? {

        this.currInput = input
        this.cacheMap.clear()
        currIndexPage = 1
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.addAll(
            photosManager.searchPhotos(input),

            photosManager.getData()
                ?.doOnNext { list ->
                    val page = list?.firstOrNull()?.page
                    cacheMap[page ?: 0] = list ?: arrayListOf()
                }
                ?.doOnNext { list ->

                    if (list?.firstOrNull()?.page == 1) {
                        photosList.onNext(list)
                    }
                }
                ?.subscribePro()

        )

        return compositeDisposable

    }

    override fun getPhotosDataAsync(): Observable<List<PhotoItem>> {
        return photosList
            .hide()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
    }

    override fun fetchPhotosBySearchTerm(): Observable<List<PhotoItem>?>? {
        currIndexPage++
        val list = cacheMap[currIndexPage]
        return if (list != null) {
            photosList.onNext(list)
            Observable.just(arrayListOf())
        } else {
            photosManager
                .searchPhotos(currIndexPage)
                ?.doOnNext {
                    if (it != null) {
                        photosList.onNext(it)
                    }
                }
        }

    }
}
