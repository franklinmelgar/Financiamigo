package com.example.financiamigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var listaProveedores: ListView
private lateinit var firebaseauth: FirebaseAuth
private lateinit var txtBuscar: EditText

class SupplierFragment : Fragment() {
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

        var view = inflater.inflate(R.layout.fragment_suppliers, container, false)

        listaProveedores = view.findViewById(R.id.listadoProveedores)
        txtBuscar = view.findViewById(R.id.txtBuscar)

        obtenerSuplidores(listaProveedores)

        return view
    }

    private fun obtenerSuplidores(listaProveedores: ListView){
        var listaContactos = mutableListOf<Contacto>()

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        bd.collection("Contactos")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .whereEqualTo("Tipo", "Proveedor")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val contacto = Contacto(document.getString("Tipo") ?: "", document.getString("Nombre") ?: "", document.getString("Apellido") ?: "", document.getString("Empresa") ?: "", document.getString("Telefono") ?: "", document.getString("Celular") ?: "", document.getString("Correo") ?: "", document.getString("Direccion") ?: "", document.getString("Usuario_ID") ?: "")
                    listaContactos.add(contacto)
                }

                val adapter = context?.let { ContactoAdapter(it, listaContactos) }

                listaProveedores.adapter = adapter

            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }
}