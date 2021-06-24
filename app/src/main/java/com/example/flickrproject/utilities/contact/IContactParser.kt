package com.example.flickrproject.utilities.contact

import android.content.ContentResolver
import android.content.Intent

interface IContactParser {
    fun parseData(data: Intent?, contentResolver: ContentResolver): String
}