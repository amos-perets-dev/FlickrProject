package com.example.flickrproject.screens.main

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class MainViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}