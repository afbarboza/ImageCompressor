package com.example.imagecompresspoc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class CelphotusCompressor {

    companion object {
        fun getImageSizeInBytes(picturePath: String): Long {
            val file = File(picturePath)
            val sizeInBytes = file.length()
            return sizeInBytes
        }

        fun compressImage(picturePath: String) {
            val bmp = BitmapFactory.decodeFile(picturePath)
            val out = FileOutputStream("${Environment.getExternalStorageDirectory()}/test.webp")
            bmp.compress(Bitmap.CompressFormat.WEBP, 50, out)
            out.close()
        }
    }
}