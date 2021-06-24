package com.example.flickrproject.network.error

interface IHandleNetworkError {
    /**
     * Call to generate the error from the server
     */
    fun generateErrorID(it: Throwable): Int
}