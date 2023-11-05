package com.example.financiamigo

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class CategoriaAdapter(private val mContext: Context, private val listaCategorias: List<Categoria>) : ArrayAdapter<Categoria>(mContext, 0, listaCategorias) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_lista_categoria, parent, false)

        var categoria = listaCategorias[position]

        var icono = layout.findViewById<ImageView>(R.id.Icono)

        val resourceId = mContext.resources.getIdentifier(categoria.Icono, "drawable", mContext.packageName)
        val colorIcon = mContext.resources.getIdentifier("bg_area", "color", mContext.packageName)

        if (resourceId != 0) {
            icono.setImageResource(resourceId)
            icono.setColorFilter(ContextCompat.getColor(context, R.color.text_color), PorterDuff.Mode.SRC_IN)
        }

        val colorId = mContext.resources.getIdentifier(categoria.Color, "color", mContext.packageName)
        if (colorId != 0) {
            val color = ContextCompat.getColor(mContext, colorId)
            icono.setBackgroundColor(color)
        }

        layout.findViewById<TextView>(R.id.Nombre).text = categoria.Nombre

        return layout
    }


}