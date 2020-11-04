package ru.skillbranch.skillarticles.data.providers

import android.net.Uri
import androidx.core.content.FileProvider

class ImageFileProvider : FileProvider() {
    override fun getType(uri: Uri): String? {
        //TODO IMPROVEMENT: read JPEG signature from file?
        return "image/jpeg"
    }
}