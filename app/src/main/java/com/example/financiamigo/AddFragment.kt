package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mesesLista: RecyclerView
    private lateinit var disminuirMes: ImageView
    private lateinit var aumentarMes: ImageView
    private lateinit var agregarTransaccion: TextView
    private lateinit var lblBalance: TextView
    private lateinit var lblIngresos: TextView
    private lateinit var lblGastos: TextView
    private lateinit var listadoTransacciones: ListView
    private lateinit var iconoNoTransacciones: ImageView
    private lateinit var TextoNoTransacciones: TextView


    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val months = DateFormatSymbols().months
    private var currentMonthPosition = currentMonth

    private lateinit var firebaseauth: FirebaseAuth

    private var totalIngresos: Double = 0.00
    private var totalGastos: Double = 0.00
    private var totalBalance: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_add, container, false)

        mesesLista = view.findViewById(R.id.meseslista)
        disminuirMes = view.findViewById(R.id.disminuirMes)
        aumentarMes = view.findViewById(R.id.aumentarMes)

        agregarTransaccion = view.findViewById(R.id.btnAgregarTransaccion)
        lblBalance = view.findViewById(R.id.balance)
        lblIngresos = view.findViewById(R.id.ingresos)
        lblGastos = view.findViewById(R.id.gastos)
        listadoTransacciones = view.findViewById(R.id.listadoTransacciones)
        iconoNoTransacciones = view.findViewById(R.id.iconoNoTransacciones)
        TextoNoTransacciones = view.findViewById(R.id.TextoNoTransacciones)


        val adapter = MonthAdapter(months.toList(), currentMonthPosition)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.scrollToPosition(currentMonthPosition)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.isAutoMeasureEnabled = true
        layoutManager.stackFromEnd = true

        mesesLista.layoutManager = layoutManager
        mesesLista.adapter = adapter

        mesesLista.smoothScrollToPosition(currentMonthPosition)

        disminuirMes.setOnClickListener{
            if (currentMonthPosition > 0) {
                currentMonthPosition--
                currentMonth--
                mesesLista.smoothScrollToPosition(currentMonthPosition)
                obtenerTransacciones(listadoTransacciones)
            }
        }

        aumentarMes.setOnClickListener{
            if (currentMonthPosition < months.size - 1) {
                currentMonthPosition++
                currentMonth++
                mesesLista.smoothScrollToPosition(currentMonthPosition)
                obtenerTransacciones(listadoTransacciones)
            }
        }

        agregarTransaccion.setOnClickListener {
            val AddTransactionActivity = Intent(activity, AddTransactionActivity::class.java)
            startActivity(AddTransactionActivity)
        }

        obtenerTransacciones(listadoTransacciones)


        return view
    }

    private fun obtenerTransacciones(listadoTransacciones: ListView){

        iconoNoTransacciones.visibility = View.VISIBLE
        TextoNoTransacciones.visibility = View.VISIBLE
        listadoTransacciones.visibility = View.INVISIBLE

        totalIngresos = 0.00
        totalGastos = 0.00

        var listaTransaccion = mutableListOf<Transaccion>()

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        bd.collection("Transacciones")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .whereEqualTo("mesTransaccion", (currentMonth + 1).toString())
            .whereEqualTo("anioTransaccion", currentYear.toString())
            .get()

            .addOnSuccessListener { result ->
                for (document in result) {

                    iconoNoTransacciones.visibility = View.INVISIBLE
                    TextoNoTransacciones.visibility = View.INVISIBLE
                    listadoTransacciones.visibility = View.VISIBLE

                    val transaccion = Transaccion(document.getString("tituloTransaccion") ?: "", document.getString("montoTransaccion") ?: "",document.getString("tipoTransaccion") ?: "",document.getString("cuentaTransaccion") ?: "",document.getString("contactoTransaccion") ?: "", document.getString("categoriaTransaccion") ?: "" ?: "",document.getString("fechaTransaccion") ?: "",document.getString("terminoTransaccion") ?: "",document.getString("comentarioTransaccion") ?: "",document.getString("imagenTransaccion") ?: "",document.getString("mesTransaccion") ?: "",document.getString("anioTrasaccion") ?: "",document.getString("Usuario_ID") ?: "",document.id,document.getString("estado") ?: "")

                    if (transaccion.tipoTransac.equals("Cliente")){
                        totalIngresos += transaccion.montoTransaccion.toDouble()
                    }else
                    {
                        totalGastos += transaccion.montoTransaccion.toDouble()
                    }
                    listaTransaccion.add(transaccion)

                }

                val transaccionesConsolidadas = consolidarTransaccionesPorFecha(listaTransaccion)

                //transaccionesConsolidadas.sortedBy { it.fecha }

                val adapter = context?.let { TransaccionAdapter(it, transaccionesConsolidadas) }

                listadoTransacciones.adapter = adapter
                lblIngresos.text = totalIngresos.toString()
                lblGastos.text = totalGastos.toString()
                lblBalance.text = (totalIngresos - totalGastos).toString()


            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun consolidarTransaccionesPorFecha(listaTransacciones: List<Transaccion>): List<TransaccionesConsolidadas> {
        val transaccionesConsolidadas = mutableListOf<TransaccionesConsolidadas>()

        val agrupadasPorFecha = listaTransacciones.groupBy { it.fechaTransaccion }
        for ((fecha, transacciones) in agrupadasPorFecha) {
            var conteo = 0
            var montoIngreso = 0.0
            var montoGasto = 0.0

            for (t in transacciones) {
                conteo++
                if (t.tipoTransac.equals("Cliente")) {
                    montoIngreso += t.montoTransaccion.toDouble()
                } else {
                    montoGasto += t.montoTransaccion.toDouble()
                }
            }

            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaFinal = format.parse(fecha)

            val transaccionConsolidada = TransaccionesConsolidadas(fechaFinal, conteo, montoIngreso, montoGasto)
            transaccionesConsolidadas.add(transaccionConsolidada)
        }

        return transaccionesConsolidadas
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}