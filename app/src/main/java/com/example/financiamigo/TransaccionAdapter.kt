package com.example.financiamigo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransaccionAdapter(private val mContext: Context, private val listaTransaccion: List<TransaccionesConsolidadas>) : ArrayAdapter<TransaccionesConsolidadas>(mContext, 0, listaTransaccion) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_lista_transaccion, parent, false)

        //obtener el dia
        var transaccion = listaTransaccion[position]

        val fechaTransaccion = transaccion.fecha
        val calendar = Calendar.getInstance()
        calendar.time = transaccion.fecha
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        //obtener el nombre del dia
        val formatoDiaNombre = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val dayName = formatoDiaNombre.format(fechaTransaccion)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        layout.findViewById<TextView>(R.id.dia).text = dia.toString()
        layout.findViewById<TextView>(R.id.descripcion).text = dayName
        layout.findViewById<TextView>(R.id.transacciones).text = "Transacciones: " + transaccion.conteo
        layout.findViewById<TextView>(R.id.montoIngreso).text = transaccion.montoIngreso.toString()
        layout.findViewById<TextView>(R.id.montoGasto).text = transaccion.montoGasto.toString()

        layout.setOnClickListener{
            val intent = Intent(mContext, DetailTransactionActivity::class.java)

            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = format.format(transaccion.fecha)

            intent.putExtra("fecha", dateString)
            mContext.startActivity(intent)
        }


        return layout
    }
}