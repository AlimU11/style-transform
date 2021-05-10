package com.example.styletransform

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import java.io.OutputStream


class FragmentResult : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var imageViewRepeat: ImageView
    private lateinit var cardViewSave: CardView
    private lateinit var cardViewRepeat: CardView
    private lateinit var imageViewSave: ImageView
    private lateinit var textSave: TextView
    private lateinit var cardViewConstraintResult: ConstraintLayout
    private var isSaved: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageViewResult)
        imageView.setImageBitmap(InformationHolder.getBitmapTransformed())

        imageViewRepeat = view.findViewById(R.id.imageViewRepeat)
        cardViewSave = view.findViewById(R.id.cardViewSave)
        cardViewRepeat = view.findViewById(R.id.cardViewRepeat)
        imageViewSave = view.findViewById(R.id.imageViewSave)
        textSave = view.findViewById(R.id.textSave)
        cardViewConstraintResult = view.findViewById(R.id.cardViewConstraintResult)

        val animationRepeat: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate)

        animationRepeat.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                Navigation.findNavController(view).navigate(R.id.resultToMain)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })


        cardViewRepeat.setOnClickListener {
            imageViewRepeat.startAnimation(animationRepeat)
        }

        cardViewSave.setOnClickListener{
            if (!isSaved) {

                val filename = "img.jpeg"

                var fos: OutputStream? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context?.contentResolver?.also { resolver ->
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                        }

                        val imageUri: Uri? =
                                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                        fos = imageUri?.let { resolver.openOutputStream(it) }
                    }
                } else {
                    val imagesDir =
                            android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
                    val image = java.io.File(imagesDir, filename)
                    fos = java.io.FileOutputStream(image)

                }

                fos?.use {
                    InformationHolder.getBitmapTransformed()?.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, it)
                }
                fos?.close()
                Log.d("TAG", "SAVED TO GALLERY")
                isSaved = true
                imageViewSave.setBackgroundResource(R.drawable.ic_check)
                textSave.text = "Saved"
            }
        }

        cardViewConstraintResult.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.resultToImage)
        }
    }

}