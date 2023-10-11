package com.example.financiamigo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CuentaAdapter(private val mContext: Context, private val listaCuentas: List<Cuenta>) : ArrayAdapter<Cuenta>(mContext, 0, listaCuentas) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_lista_cuenta, parent, false)

        var cuenta = listaCuentas[position]

        var icono = layout.findViewById<ImageView>(R.id.icono)

        layout.findViewById<TextView>(R.id.NombreCuenta).text = cuenta.Nombre
        layout.findViewById<TextView>(R.id.DescripcionCuenta).text = cuenta.Descripcion
        layout.findViewById<TextView>(R.id.saldo).text = cuenta.Saldo

        return layout
    }
}