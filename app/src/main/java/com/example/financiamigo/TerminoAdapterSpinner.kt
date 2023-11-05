package com.example.financiamigo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class TerminoAdapterSpinner(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<Termino_Credito>
) : ArrayAdapter<Termino_Credito>(context, resource, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val termino = getItem(position)
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = termino?.Nombre_Termino
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val termino = getItem(position)
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = termino?.Nombre_Termino
        return view
    }
}