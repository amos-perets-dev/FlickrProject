package com.example.flickrproject.flickr_app

import android.content.Context
import com.example.flickrproject.manager.IPhotosManager
import com.example.flickrproject.manager.PhotosManager
import com.example.flickrproject.network.api.FlickrApi
import com.example.flickrproject.network.api.image.IImageApi
import com.example.flickrproject.network.api.image.ImageApi
import com.example.flickrproject.network.base.BaseNetworkManager
import com.example.flickrproject.network.base.IBaseNetworkManager
import com.example.flickrproject.network.error.HandleNetworkError
import com.example.flickrproject.network.error.IHandleNetworkError
import com.example.flickrproject.repository.IPhotosRepository
import com.example.flickrproject.repository.PhotosRepository
import com.example.flickrproject.screens.home.HomePageViewModel
import com.example.flickrproject.screens.home.PhotosAdapter
import com.example.flickrproject.screens.main.MainViewModel
import com.example.flickrproject.utilities.contact.ContactParser
import com.example.flickrproject.utilities.contact.IContactParser
import com.example.flickrproject.utilities.image.IImageUtil
import com.example.flickrproject.utilities.image.ImageUtil
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class FlickrModules {
    fun createModules(context: Context): List<Module> {

        val appModules = createAppModules(context)
        val homePageModule = createHomePageModule()
        val mainModule = createMainModule()

        return listOf(appModules, mainModule, homePageModule)
    }

    private fun createAppModules(context: Context): Module {

        return module {
            single<IHandleNetworkError> { HandleNetworkError() }
            single<IBaseNetworkManager> { BaseNetworkManager() }

            factory<IImageApi> { ImageApi() }
            factory { PhotosAdapter() }
            factory<IContactParser> { ContactParser() }

            factory<IImageUtil> { ImageUtil(context) }

            factory<IPhotosManager> {
                val retrofit = get<IBaseNetworkManager>().buildRetrofit()
                PhotosManager(
                    retrofit
                        .create(FlickrApi.SearchImagesApi::class.java),
                    get(),
                    get()
                )
            }
            single<IPhotosRepository> { PhotosRepository(get()) }
        }
    }

    private fun createHomePageModule(): Module {
        return module {
            viewModel { HomePageViewModel(get(), get()) }
        }
    }

    private fun createMainModule(): Module {
        return module {
            viewModel { MainViewModel() }
        }
    }

}
