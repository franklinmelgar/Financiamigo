package com.example.financiamigo

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class CuentaAdapter(private val mContext: Context, private val listaCuentas: List<Cuenta>) : ArrayAdapter<Cuenta>(mContext, 0, listaCuentas) {

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
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_lista_cuenta, parent, false)

        var cuenta = listaCuentas[position]

        var icono = layout.findViewById<ImageView>(R.id.icono)

        layout.findViewById<TextView>(R.id.NombreCuenta).text = cuenta.Nombre
        layout.findViewById<TextView>(R.id.DescripcionCuenta).text = cuenta.Descripcion
        layout.findViewById<TextView>(R.id.saldo).text = cuenta.Saldo

        icono.background = color

        icono.setColorFilter(ContextCompat.getColor(context, R.color.text_color), PorterDuff.Mode.SRC_IN)

        return layout
    }
}