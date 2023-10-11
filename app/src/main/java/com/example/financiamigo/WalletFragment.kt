package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var btnAgregar: FloatingActionButton
private lateinit var listadoCuentas: ListView
private lateinit var grandTotal: TextView
private lateinit var firebaseauth: FirebaseAuth

class WalletFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        var view = inflater.inflate(R.layout.fragment_wallet, container, false)

        btnAgregar = view.findViewById(R.id.fabAdd)
        btnAgregar.setOnClickListener{
            val accountActivity = Intent(activity, AccountActivity::class.java)
            startActivity(accountActivity)
        }

        listadoCuentas = view.findViewById(R.id.listadoCuenta)

        grandTotal = view.findViewById(R.id.grandTotal)

        var listaCuentas = mutableListOf<Cuenta>()
        var conteo: Int = 0

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore
        bd.collection("Cuentas")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    conteo += document.getString("Saldo")?.toInt() ?: 0
                    val cuenta = Cuenta(document.getString("Nombre") ?: "", document.getString("Descripcion") ?: "", document.getString("Numero_Cuenta") ?: "", document.getString("Nombre_Banco") ?: "", document.getString("Tipo_Cuenta") ?: "", document.getString("Fecha") ?: "", document.getString("Saldo") ?: "", document.getString("Usuario_ID") ?: "")
                    listaCuentas.add(cuenta)
                }

                grandTotal.text = "Lps. $conteo"

                val adapter = context?.let { CuentaAdapter(it, listaCuentas) }

                listadoCuentas.adapter = adapter

            }
            .addOnFailureListener { e ->
                //Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}