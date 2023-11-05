package com.example.financiamigo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class CategoriaAdapterSpinner(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<Categoria>
) : ArrayAdapter<Categoria>(context, resource, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val categoria = getItem(position)
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categoria?.Nombre
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val categoria = getItem(position)
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categoria?.Nombre
        return view
    }
}