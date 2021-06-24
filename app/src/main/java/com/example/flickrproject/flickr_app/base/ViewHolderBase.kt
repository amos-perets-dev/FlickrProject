package com.example.flickrproject.flickr_app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolderBase<Model>(parent: ViewGroup, @LayoutRes layout: Int) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
    ) {

    abstract fun bindData(model: Model)
}