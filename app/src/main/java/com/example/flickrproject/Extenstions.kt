package com.example.flickrproject

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.subscribePro(): Disposable {
    return subscribe({}, Throwable::printStackTrace)
}

fun <T> Flowable<T>.subscribePro(): Disposable {
    return subscribe({}, Throwable::printStackTrace)
}
