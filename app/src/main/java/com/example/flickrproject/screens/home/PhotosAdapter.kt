package com.example.flickrproject.screens.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.flickrproject.R
import com.example.flickrproject.data.PhotoItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PhotosAdapter : ListAdapter<PhotoItem, PhotoViewHolder>(BreathItemDiff) {

    private val onClick = PublishSubject.create<String>()

    private val prevList = arrayListOf<PhotoItem>()
    private val currList = arrayListOf<PhotoItem>()

    object BreathItemDiff : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    fun update(list: List<PhotoItem>){
        val newList = ArrayList(prevList)
        newList.addAll(list)
        submitList(newList)
        prevList.addAll(list)
    }

    fun onClick(): Observable<String>? {
        return onClick.hide()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(parent, R.layout.photo_item, onClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    fun clear() {
        prevList.clear()
        submitList(null)
    }

}