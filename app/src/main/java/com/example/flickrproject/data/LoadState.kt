package com.example.flickrproject.data

sealed class LoadState {

    object Load : LoadState()

    object Finish : LoadState()
}