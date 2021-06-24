package com.example.flickrproject.screens.home

import android.content.ContentResolver
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flickrproject.data.LoadState
import com.example.flickrproject.data.PhotoItem
import com.example.flickrproject.repository.IPhotosRepository
import com.example.flickrproject.subscribePro
import com.example.flickrproject.utilities.contact.IContactParser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePageViewModel(
    private val photosRepository: IPhotosRepository,
    private val contactParser: IContactParser
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val photosList = MutableLiveData<List<PhotoItem>>()
    val openContactList = MutableLiveData<Boolean>()
    val photoUrl = MutableLiveData<String>()
    val loadState = MutableLiveData<LoadState>()

    init {

        compositeDisposable.add(
            photosRepository
                .getPhotosDataAsync()
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    photosList.postValue(it)
                    loadState.postValue(LoadState.Finish)
                }
                .subscribePro()
        )

    }

    fun onClickSearch(input: String) {
        if (input.isEmpty()) return
        loadState.postValue(LoadState.Load)
        photosRepository.fetchPhotosBySearchTerm(input)?.let {
            compositeDisposable.add(
                it
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun loadData() {
        loadState.postValue(LoadState.Load)
        photosRepository
            .fetchPhotosBySearchTerm()
            ?.subscribePro()?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

    fun onClickPickContact() {
        openContactList.postValue(true)
    }

    fun contactResult(data: Intent?, contentResolver: ContentResolver) {
        val name = contactParser.parseData(data, contentResolver)
        onClickSearch(name)
    }

    fun onClickPhoto(url: String?) {
        photoUrl.postValue(url)
    }

}