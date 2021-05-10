package com.example.styletransform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flaviofaria.kenburnsview.KenBurnsView
import com.squareup.picasso.Picasso

internal class StylesAdapter(private val styles: MutableList<Style>) :
    RecyclerView.Adapter<StylesAdapter.StylesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StylesViewHolder {

        view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_container,
                parent, false
        )
        return StylesViewHolder(view)
    }

    private lateinit var view: View

    override fun onBindViewHolder(holder: StylesViewHolder, position: Int) {
        holder.setStyle(styles[position])
    }

    override fun getItemCount(): Int {
        return styles.size
    }

    internal class StylesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kbvPaint: KenBurnsView = itemView.findViewById(R.id.kbvPaint)
        private val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        private val textPaint: TextView = itemView.findViewById(R.id.textPaint)

        fun setStyle(style: Style) {
            Picasso.get().load(style.imageUrl).into(kbvPaint)
            textAuthor.text = style.author
            textPaint.text = style.paint
        }
    }
}