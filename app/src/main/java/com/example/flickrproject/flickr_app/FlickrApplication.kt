package com.example.flickrproject.flickr_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FlickrApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(applicationContext)
            // modules
            modules(FlickrModules().createModules(applicationContext))
        }
    }
}