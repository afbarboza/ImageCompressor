package com.example.imagecompresspoc

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.imagecompresspoc.databinding.ActivityReadPhotoBinding
import java.io.File
import kotlin.math.roundToInt


class ReadPhotoActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityReadPhotoBinding

    private val TAG = "CelphotusCompressorTag"

    private val launcher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data != null
        ) {
            val photoUri: Uri = result.data!!.data!!
            Toast.makeText(this, "Photo Loaded Successfully from ${photoUri?.path}", Toast.LENGTH_LONG).show()


            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentResolver.query(
                photoUri,
                filePathColumn,
                null, null, null)
            cursor?.moveToFirst()

            val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()


            /* original picture */
            Log.d(TAG, picturePath)
            val originalSize = CelphotusCompressor.getImageSizeInBytes(picturePath)
            Log.d(TAG, "File Size Before Compression: ${originalSize}")

            /* webp picture */
            val pathToCompressedImageWebp = CelphotusCompressor.compressImageToWebp(picturePath, this)
            val sizeWebp = CelphotusCompressor.getImageSizeInBytes("$pathToCompressedImageWebp")
            val percentageWebp = ((sizeWebp / (1.0 * originalSize)) * 100)
            Log.d(TAG, "File Size After Compression (WEBP): $sizeWebp (${String.format("%.2f", percentageWebp)}%)")

            /* png picture */
            val pathToCompressedImagePng = CelphotusCompressor.compressImageToPng(picturePath, this)
            val sizePng = CelphotusCompressor.getImageSizeInBytes("$pathToCompressedImagePng")
            val percentagePng = ((sizePng / (1.0 * originalSize)) * 100)
            Log.d(TAG, "File Size After Compression (PNG): ${sizePng} (${String.format("%.2f", percentagePng)}%)")

            /* jpeg picture */
            val pathToCompressedImageJpeg = CelphotusCompressor.compressImageToJpeg(picturePath, this)
            val sizeJpeg = CelphotusCompressor.getImageSizeInBytes("$pathToCompressedImageJpeg")
            val percentageJpeg = ((sizeJpeg / (1.0 * originalSize)) * 100)
            Log.d(TAG, "File Size After Compression (JPEG): ${sizeJpeg} (${String.format("%.2f", percentageJpeg)}%)")

             Glide.with(this)
                .load(File(pathToCompressedImageWebp))
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(viewBinding.imgOriginalImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityReadPhotoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initWidgets()

    }

    private fun initWidgets() {
        viewBinding.btnOpenGallery.setOnClickListener {
            val photoPickerFromGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(photoPickerFromGalleryIntent)
        }
    }
}