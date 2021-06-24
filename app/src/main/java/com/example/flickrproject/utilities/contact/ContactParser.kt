package com.example.flickrproject.utilities.contact

import android.content.ContentResolver
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log

class ContactParser : IContactParser {
    override fun parseData(data: Intent?, contentResolver: ContentResolver): String {
        val contactUri = data?.data ?: return ""
        var name = ""

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = contentResolver.query(
            contactUri, projection,
            null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            name = cursor.getString(nameIndex)
        }
        cursor?.close()

        return name
    }
}