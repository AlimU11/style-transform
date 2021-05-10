package com.example.styletransform

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.IOException

class InformationHolder {
    companion object {
        private var bitmapImg: Bitmap? = null
        private var bitmapTransformed: Bitmap? = null
        private var styleUri: String? = null
        private var dominantColor = 0
        private var position = -1

        fun getPosition(): Int {
            return position
        }

        fun setPosition(position: Int) {
            InformationHolder.position = position
        }

        fun getDominantColor(): Int {
            return dominantColor
        }

        fun setDominantColor(dominantColor: Int) {
            InformationHolder.dominantColor = dominantColor
        }

        @Throws(IOException::class)
        fun setBitmap(contentResolver: ContentResolver?, url: Uri?) {
            bitmapImg = MediaStore.Images.Media.getBitmap(contentResolver, url)
        }

        fun setBitmapTransformed(bitmapTransformed: Bitmap) {
            InformationHolder.bitmapTransformed = bitmapTransformed
        }

        fun getBitmapImg(): Bitmap? {
            return bitmapImg
        }

        fun getBitmapTransformed(): Bitmap? {
            return bitmapTransformed
        }

        fun getStyleUri(): String? {
            return styleUri
        }

        fun setStyleUri(styleUri: String) {
            InformationHolder.styleUri = styleUri
        }
    }
}