package com.example.financiamigo

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailTransactionActivity : AppCompatActivity() {

    private lateinit var btRegresar: ImageView
    private lateinit var firebaseauth: FirebaseAuth
    var listaCategorias = mutableListOf<Categoria>()
    var listaContactos = mutableListOf<Contacto>()
    var fecha: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction)

        fecha = intent.getStringExtra("fecha").toString()

        btRegresar = findViewById(R.id.btRegregar)

        btRegresar.setOnClickListener{
            this.finish()
        }

        obtenerCategorias()
        obtenerContactos()

    }

    private fun obtenerCategorias(){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        bd.collection("Categoria")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val categoria = Categoria(document.getString("Nombre") ?: "", document.getString("Icono") ?: "", document.getString("Color") ?: "", document.getString("Usuario_ID") ?: "")
                    listaCategorias.add(categoria)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun obtenerContactos(){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        bd.collection("Contactos")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val contacto = Contacto(document.getString("Tipo") ?: "", document.getString("Nombre") ?: "", document.getString("Apellido") ?: "", document.getString("Empresa") ?: "", document.getString("Telefono") ?: "", document.getString("Celular") ?: "", document.getString("Correo") ?: "", document.getString("Direccion") ?: "", document.getString("Usuario_ID") ?: "")
                    listaContactos.add(contacto)
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun obtenerTransacciones(listadoTransacciones: ListView){

        var listaTransaccion = mutableListOf<Transaccion>()

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        bd.collection("Transacciones")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .whereEqualTo("fechaTransaccion", fecha)
            .get()

            .addOnSuccessListener { result ->
                for (document in result) {
                    val transaccion = Transaccion(document.getString("tituloTransaccion") ?: "", document.getString("montoTransaccion") ?: "",document.getString("tipoTransaccion") ?: "",document.getString("cuentaTransaccion") ?: "",document.getString("contactoTransaccion") ?: "", document.getString("categoriaTransaccion") ?: "" ?: "",document.getString("fechaTransaccion") ?: "",document.getString("terminoTransaccion") ?: "",document.getString("comentarioTransaccion") ?: "",document.getString("imagenTransaccion") ?: "",document.getString("mesTransaccion") ?: "",document.getString("anioTrasaccion") ?: "",document.getString("Usuario_ID") ?: "",document.id,document.getString("estado") ?: "")
                    listaTransaccion.add(transaccion)
                }

                /*val transaccionesConsolidadas = consolidarTransaccionesPorFecha(listaTransaccion)

                val adapter = context?.let { TransaccionAdapter(it, transaccionesConsolidadas) }

                listadoTransacciones.adapter = adapter*/


            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

}