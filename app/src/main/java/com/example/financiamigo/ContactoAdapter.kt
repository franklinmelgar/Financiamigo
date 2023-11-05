package com.example.financiamigo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class ContactoAdapter(private val mContext: Context, private val listaContactos: List<Contacto>) : ArrayAdapter<Contacto>(mContext, 0, listaContactos) {

    private val color: Drawable?
        get() {

            val drawables = arrayOf(
                R.drawable.item_circle_blue,
                R.drawable.item_circle_green,
                R.drawable.item_circle_pink,
                R.drawable.item_circle_yellow
            )

            val randomIndex = (0 until drawables.size).random()

            return ContextCompat.getDrawable(context, drawables[randomIndex])

        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_lista_contacto, parent, false)

        var contacto = listaContactos[position]

        var icono = layout.findViewById<TextView>(R.id.Letra)

        layout.findViewById<TextView>(R.id.Nombre).text = contacto.Nombre

        val letra = contacto.Nombre.first()
        icono.text = letra.toString()
        icono.background = color

        return layout
    }


}