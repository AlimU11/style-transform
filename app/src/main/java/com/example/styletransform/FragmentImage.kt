package com.example.styletransform

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation


class FragmentImage : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var cardViewBack: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageFullScreen)
        cardViewBack = view.findViewById(R.id.cardViewBack)

        imageView.setImageBitmap(InformationHolder.getBitmapTransformed())

        cardViewBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.imageToResult)
        }
    }
}