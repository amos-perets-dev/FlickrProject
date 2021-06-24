package com.example.flickrproject.screens.home

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.flickrproject.data.PhotoItem
import com.example.flickrproject.flickr_app.base.ViewHolderBase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.photo_item.view.*
import java.util.*


class PhotoViewHolder(
    parent: ViewGroup,
    @LayoutRes layout: Int,
    private val onClick: PublishSubject<String>
) :
    ViewHolderBase<PhotoItem>(parent, layout) {

    override fun bindData(model: PhotoItem) {
        itemView.text_view_date.text = model.datesTaken
        itemView.text_view_owners.text = model.ownersNames
        itemView.text_view_descriptions.text = model.descriptions

        Glide.with(itemView.context)
            .load(model.smallPhotoUrl)
            .into(itemView.image_view_photo)

        itemView.setOnClickListener {
            onClick.onNext(model.mediumPhotoUrl)
        }
    }

    private fun getText(@StringRes resString: Int): String {
        return itemView.resources.getString(resString)
    }

}