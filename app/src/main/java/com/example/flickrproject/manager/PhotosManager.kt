package com.example.flickrproject.manager

import android.util.Log
import com.example.flickrproject.data.PhotoItem
import com.example.flickrproject.data.PhotosResponse
import com.example.flickrproject.network.api.FlickrApi
import com.example.flickrproject.network.api.image.IImageApi
import com.example.flickrproject.subscribePro
import com.example.flickrproject.utilities.image.IImageUtil
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PhotosManager(
    private val searchImagesApi: FlickrApi.SearchImagesApi,
    private val imageApi: IImageApi,
    private val imageUtil: IImageUtil
) : IPhotosManager {

    //TEST
    private val limitPages = 15

    private val listReady = PublishSubject.create<List<PhotoItem>>()

    private var currInput: String? = null

    override fun searchPhotos(page: Int): Observable<List<PhotoItem>?>? {
        val loadPages = searchImagesApi.searchImages(currInput.toString(), page.toString())
            .subscribeOn(Schedulers.io())
        val photosList = parseResponseToPhotosItemsList(loadPages)
        return photosList
    }

    override fun searchPhotos(input: String): Disposable {

        this.currInput = input
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.addAll(
            searchPhotos(1)
                ?.subscribePro(),

            Observable
                .range(2, limitPages)
                .concatMap {
                    return@concatMap searchImagesApi.searchImages(input, it.toString())
                        .subscribeOn(Schedulers.io())
                        .concatMap { response ->
                            parseResponseToPhotosItemsList(
                                Observable.just(
                                    response
                                )
                            )
                        }
                }
                .takeWhile {
                    return@takeWhile it.firstOrNull()?.page ?: 0 < limitPages
                }
                .subscribePro(),

            listReady
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribe {
                    it.forEach { model ->
                        imageUtil.loadImage(model.smallPhotoUrl)
                    }
                }
        )
        return compositeDisposable
    }

    override fun getData(): Flowable<List<PhotoItem>>? {
        return listReady
            .hide()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun parseResponseToPhotosItemsList(loadPages: Observable<PhotosResponse>): Observable<List<PhotoItem>?>? {
        return loadPages
            .map { response ->
                val photos = response.photos
                val photosList = photos?.photosList
                val page = photos?.page

                return@map photosList
                    ?.map { model ->
                        imageApi.addParameters(model.server, model.id, model.secret)
                        PhotoItem(
                            model.id.toString(),
                            model.owner.toString(),
                            model.dateTaken.toString(),
                            model.description.toString(),
                            imageApi.buildSmallPhotoUrl(),
                            imageApi.buildMediumPhotoUrl(),
                            page ?: 0
                        )
                    }?.toList()
            }
            .doOnNext { photosItemsList ->
                if (photosItemsList != null) {
                    listReady.onNext(photosItemsList)
                }
            }
    }
}