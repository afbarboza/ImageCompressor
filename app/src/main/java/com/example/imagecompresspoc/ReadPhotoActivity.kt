package com.example.imagecompresspoc

import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.imagecompresspoc.databinding.ActivityReadPhotoBinding
import java.io.File


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

            //viewBinding.imgOriginalImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))

            val size = CelphotusCompressor.getImageSizeInBytes(picturePath)
            Log.d(TAG, "File Size Before Compression: ${size}")

            CelphotusCompressor.compressImage(picturePath)

            val compressedFilePath = "${Environment.getExternalStorageDirectory()}/test.webp"
            val size2 = CelphotusCompressor.getImageSizeInBytes("$compressedFilePath")
            Log.d(TAG, "File Size After Compression: ${size2}")

            Glide.with(this)
                .load(File(compressedFilePath))
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