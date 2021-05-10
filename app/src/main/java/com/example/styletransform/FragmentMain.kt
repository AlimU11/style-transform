package com.example.styletransform

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.styletransform.ml.MagentaArbitraryImageStylizationV1256Fp16Prediction1
import com.example.styletransform.ml.MagentaArbitraryImageStylizationV1256Fp16Transfer1
import org.tensorflow.lite.support.image.TensorImage


class FragmentMain : Fragment() {
    private lateinit var styles: MutableList<Style>
    private lateinit var stylesViewPager: ViewPager2
    private lateinit var button: Button
    private lateinit var layout: ConstraintLayout
    private lateinit var imageSelected: ImageView
    private lateinit var imageView: ImageView
    private lateinit var textSelected: TextView
    private lateinit var addButton: ImageButton
    private var isSelected: Boolean = false
    var isCustomExist: Boolean = false
    var idx: Int = -1
    var dominantColor = Color.parseColor("#417DBC")
    private var styleCustom = Style("add custom style", "Something special", Uri.parse("android.resource://com.example.styletransform/" + R.color.white)
            .toString())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stylesViewPager = view.findViewById(R.id.stylesViewPager)
        button = view.findViewById(R.id.buttonProcess)
        layout = view.findViewById(R.id.cardViewConstraint)
        imageSelected = view.findViewById(R.id.imageSelected)
        imageView = view.findViewById(R.id.imageView)
        textSelected = view.findViewById(R.id.textViewSelect)
        addButton = view.findViewById(R.id.addButton)


        if (InformationHolder.getBitmapImg() != null) {
            imageSelected.setImageBitmap(InformationHolder.getBitmapImg())
            imageView.visibility =  View.INVISIBLE
            textSelected.visibility = View.INVISIBLE
        }

        val rotateClockwise: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)
        val rotateAntiClockwise: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)

        rotateClockwise.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                addButton.setImageResource(R.drawable.ic_remove)
                addButton.setColorFilter(ContextCompat.getColor(context!!, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        rotateAntiClockwise.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                addButton.setImageResource(R.drawable.ic_add)
                addButton.setColorFilter(ContextCompat.getColor(context!!, R.color.teal_700), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        addButton.setOnClickListener{

            if (!isCustomExist && styles.size < 6) {
                addButton.startAnimation(rotateClockwise)
                styles.add(styleCustom)
                stylesViewPager.adapter?.notifyItemInserted(5)

                stylesViewPager.currentItem = 5

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivityForResult(intent, 2)
                    isCustomExist = true
                }, 500)

            } else if (isCustomExist && styles.size == 6) {
                addButton.startAnimation(rotateAntiClockwise)

                stylesViewPager.currentItem = 4

                Handler(Looper.getMainLooper()).postDelayed({
                    styles.removeAt(5)
                    stylesViewPager.adapter?.notifyItemRemoved(5)
                }, 150)


                isCustomExist = false
                addButton.visibility = View.VISIBLE
            }
        }

        stylesViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                idx = position
                InformationHolder.setPosition(position)
                when (position) {
                    0 -> {
                        button.setBackgroundColor(ContextCompat.getColor(view.context, R.color.starry_night))
                        activity?.window?.statusBarColor = ContextCompat.getColor(view.context, R.color.starry_night)
                        addButton.visibility = if (isCustomExist) View.INVISIBLE else View.VISIBLE
                    }
                    1 -> {
                        button.setBackgroundColor(ContextCompat.getColor(view.context, R.color.great_wave))
                        activity?.window?.statusBarColor = ContextCompat.getColor(view.context, R.color.great_wave)
                        addButton.visibility = if (isCustomExist) View.INVISIBLE else View.VISIBLE
                    }
                    2 -> {
                        button.setBackgroundColor(ContextCompat.getColor(view.context, R.color.black_spot))
                        activity?.window?.statusBarColor = ContextCompat.getColor(view.context, R.color.black_spot)
                        addButton.visibility = if (isCustomExist) View.INVISIBLE else View.VISIBLE
                    }
                    3 -> {
                        button.setBackgroundColor(ContextCompat.getColor(view.context, R.color.jalousie))
                        activity?.window?.statusBarColor = ContextCompat.getColor(view.context, R.color.jalousie)
                        addButton.visibility = if (isCustomExist) View.INVISIBLE else View.VISIBLE
                    }
                    4 -> {
                        button.setBackgroundColor(ContextCompat.getColor(view.context, R.color.profumo))
                        activity?.window?.statusBarColor = ContextCompat.getColor(view.context, R.color.profumo)
                        addButton.visibility = if (isCustomExist) View.INVISIBLE else View.VISIBLE
                    }
                    5 -> {
                        button.setBackgroundColor(dominantColor)
                        activity?.window?.statusBarColor = dominantColor
                        addButton.visibility = if (isCustomExist) View.VISIBLE else View.INVISIBLE
                    }
                }

            }
        })

        button.setOnClickListener {
            if (isSelected || InformationHolder.getBitmapImg() != null) {
                transfer(view)
            }
        }
        
        layout.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        setStyles()
        if (InformationHolder.getStyleUri() != null) {
            styleCustom.imageUrl = InformationHolder.getStyleUri()!!
            styles.add(styleCustom)
            isCustomExist = true
            dominantColor = InformationHolder.getDominantColor()
            button.setBackgroundColor(dominantColor)
            addButton.setImageResource(R.drawable.ic_remove)
            addButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            activity?.window?.statusBarColor = dominantColor
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            isSelected = true

            imageSelected.setImageURI(data?.data)
            InformationHolder.setBitmap(activity?.contentResolver, data?.data)

            imageView.visibility =  View.INVISIBLE
            textSelected.visibility = View.INVISIBLE
        }

        if (requestCode == 2) {
            data?.dataString?.let { InformationHolder.setStyleUri(it) }
            styles[5].imageUrl = data?.dataString!!
            styles[5].author = "your custom style"
            stylesViewPager.adapter?.notifyItemChanged(5)

            Palette.from(MediaStore.Images.Media.getBitmap(activity?.contentResolver, data.data))
                    .generate { p ->
                        val defaultValue = 0x000000
                        p?.let { dominantColor = it.getDominantColor(defaultValue) }
                        p?.let { button.setBackgroundColor(it.getDominantColor(defaultValue)) }
                        p?.let { activity?.window?.statusBarColor = it.getDominantColor(defaultValue) }
                        p?.let { InformationHolder.setDominantColor(it.getDominantColor(defaultValue)) }
                    }
        }
    }

    private fun setStyles() {
        styles = ArrayList()

        val styleVanGogh = Style(
                "by Vincent Van Gogh", "Starry Night",
                Uri.parse("android.resource://com.example.styletransform/" + R.drawable.style_starry_night)
                        .toString()
        )

        val styleHokusai = Style(
                "by Hokusai", "The Great Wave off Kanagawa",
                Uri.parse("android.resource://com.example.styletransform/" + R.drawable.style_great_wave)
                        .toString()
        )

        val styleKandinsky = Style(
                "by Vasilij Kandinsky", "Black Spot",
                Uri.parse("android.resource://com.example.styletransform/" + R.drawable.style_black_spot)
                        .toString()
        )

        val styleGrasset = Style(
                "by Eugène Grasset", "Jalousie",
                Uri.parse("android.resource://com.example.styletransform/" + R.drawable.style_jalousie)
                        .toString()
        )

        val styleRussolo = Style(
                "by Luigi Russolo", "Profumo",
                Uri.parse("android.resource://com.example.styletransform/" + R.drawable.style_profumo)
                        .toString()
        )

        styles.add(styleVanGogh)
        styles.add(styleHokusai)
        styles.add(styleKandinsky)
        styles.add(styleGrasset)
        styles.add(styleRussolo)

        stylesViewPager.adapter = StylesAdapter(styles)

        stylesViewPager.offscreenPageLimit = 3
        stylesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        stylesViewPager.setPageTransformer { page, position ->
            val myOffset: Float = position * -(2 * 5 + 25)
            if (stylesViewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(stylesViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            } else {
                page.translationY = myOffset
            }
        }
    }

    private fun transfer(view: View) {
        val bitmapStyle = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(styles[idx].imageUrl))
        val resizedStyle: Bitmap = Bitmap.createScaledBitmap(bitmapStyle, 256, 256, true)

        val modelPredict = MagentaArbitraryImageStylizationV1256Fp16Prediction1.newInstance(view.context)

        // Creates inputs for reference.
        val styleImage = TensorImage.fromBitmap(resizedStyle)

        // Runs model inference and gets result.
        val outputsPredict = modelPredict.process(styleImage)
        val styleBottleneck = outputsPredict.styleBottleneckAsTensorBuffer

        // Releases model resources if no longer used.
        modelPredict.close()

        val resizedImage: Bitmap = Bitmap.createScaledBitmap(
                InformationHolder.getBitmapImg()!!,
                384,
                384,
                true
        )

        val modelStyleTransfer = MagentaArbitraryImageStylizationV1256Fp16Transfer1.newInstance(view.context)

        // Creates inputs for reference.
        val contentImage = TensorImage.fromBitmap(resizedImage)

        //styleBottleneck.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputsTransform = modelStyleTransfer.process(contentImage, styleBottleneck)
        val styledImage = outputsTransform.styledImageAsTensorImage
        val styledImageBitmap = styledImage.bitmap

        // Releases model resources if no longer used.
        modelStyleTransfer.close()

        InformationHolder.setBitmapTransformed(Bitmap.createScaledBitmap(styledImageBitmap,
                InformationHolder.getBitmapImg()!!.width, InformationHolder.getBitmapImg()!!.height, true))

        Navigation.findNavController(view).navigate(R.id.mainToResult)
    }
}