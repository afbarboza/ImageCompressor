package com.example.imagecompresspoc

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CelphotusCompressor {

    companion object {
        private val TAG = "CelphotusCompressorBmpLogger"
        private val WEBP_EXTENSION = ".webp"
        private val JPEG_EXTENSION = ".jpeg"

        fun getImageSizeInBytes(picturePath: String): Long {
            val file = File(picturePath)
            val sizeInBytes = file.length()
            return sizeInBytes
        }

        fun compressImageToWebp(picturePath: String, context: Context): String {
            var absolutePathToCompressedFile = ""

            val bmp = BitmapFactory.decodeFile(picturePath)
            val directory = getPictureDirectory(context)

            val compressedFileName = createFileName()
            val compressedFilePath = File(directory, "$compressedFileName$WEBP_EXTENSION")

            try {
                val out = FileOutputStream(compressedFilePath)
                bmp.compress(Bitmap.CompressFormat.WEBP, 50, out)
                out.close()
                absolutePathToCompressedFile = compressedFilePath.absolutePath
            } catch (e: Exception) {
                Log.e(TAG, "Error while trying to compress the image\n", e)
            } finally {
                return absolutePathToCompressedFile
            }
        }

        fun compressImageToJpeg(picturePath: String, context: Context): String {
            var absolutePathToCompressedFile = ""

            val bmp = BitmapFactory.decodeFile(picturePath)
            val directory = getPictureDirectory(context)

            Log.d(TAG, ">> Orignal bitmap size: ${getBitmapSize(bmp)}")

            val compressedFileName = createFileName()
            val compressedFilePath = File(directory, "$compressedFileName$JPEG_EXTENSION")

            try {
                val out = FileOutputStream(compressedFilePath)
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, out)
                out.close()
                absolutePathToCompressedFile = compressedFilePath.absolutePath
            } catch (e: Exception) {
                Log.e(TAG, "Error while trying to compress the image\n", e)
            } finally {
                return absolutePathToCompressedFile
            }
        }

        private fun getPictureDirectory(context: Context): File {
            val cw = ContextWrapper(context.applicationContext)
            val directory = cw.getDir("pictures", Context.MODE_PRIVATE)
            if (!directory.exists()) {
                directory.mkdir()
            }

            return directory
        }

        private fun createFileName(): String {
            return System.currentTimeMillis().toString()
        }

        fun compressImageToJpegBitmap(picturePath: String, context: Context): Bitmap {
            val originalImageBmp = BitmapFactory.decodeFile(picturePath)
            val compressedByteArray: ByteArrayOutputStream = ByteArrayOutputStream();

            originalImageBmp.compress(Bitmap.CompressFormat.JPEG, 50, compressedByteArray)
            val byteArray = compressedByteArray.toByteArray()

            val compressedImageBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            Log.d(TAG, ">> Compressed bitmap size: ${byteArray.size}")

            return compressedImageBmp
        }

        private fun getBitmapSize(bmp: Bitmap): Int {
            val originalByteArray: ByteArrayOutputStream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, originalByteArray)
            val byteArray = originalByteArray.toByteArray()
            return byteArray.size
        }
    }
}