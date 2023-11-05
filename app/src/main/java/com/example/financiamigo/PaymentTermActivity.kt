package com.example.financiamigo

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaymentTermActivity : AppCompatActivity() {

    private lateinit var btRegresar: ImageView
    private lateinit var txtNombreTermino: EditText
    private lateinit var txtCantdadDias: EditText
    private lateinit var btGuardar: Button
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var listadoTerminosCredito: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_term)

        btRegresar = findViewById(R.id.btRegregar)
        txtNombreTermino = findViewById(R.id.txtNombreTermino)
        txtCantdadDias = findViewById(R.id.txtCantdadDias)
        btGuardar = findViewById(R.id.btGuardar)
        listadoTerminosCredito = findViewById(R.id.listadoTerminosCredito)

        btRegresar.setOnClickListener{
            this.finish()
        }

        btGuardar.setOnClickListener{
            if (validacion().equals(true)){
                guardar(txtNombreTermino.text.toString(), txtCantdadDias.text.toString())
                setListAdapter(this, listadoTerminosCredito)
            }
        }

        setListAdapter(this, listadoTerminosCredito)
    }

    private fun setListAdapter(context: Context, listView: ListView) {
        val firebaseauth = Firebase.auth
        val listaTerminos = mutableListOf<String>()
        val bd = Firebase.firestore

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listaTerminos)

        bd.collection("Terminos_Credito")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val termino = document.getString("Nombre_Termino") ?: ""
                    listaTerminos.add(termino)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        listView.adapter = adapter
    }

    private fun guardar(nombre: String, cantidad: String){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        val termino = hashMapOf(
            "Nombre_Termino" to nombre,
            "Cantidad_Dias" to cantidad,
            "Usuario_ID" to firebaseauth.currentUser?.uid.toString()
        )

        // Add a new document with a generated ID
        bd.collection("Terminos_Credito")
            .add(termino)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Termino de Credito Creado", Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }

    }

    private fun validacion(): Boolean {

        var continuar = true

        if (txtNombreTermino.text.isEmpty()) {
            txtNombreTermino.error = "Nombre de termino de credito obligatorio"
            continuar = false
        }

        if (txtCantdadDias.text.isEmpty()) {
            txtCantdadDias.error = "Cantidad de dias de duracion el termino de credito"
            continuar = false
        }

        return continuar
    }

    private fun limpiarCampos(){
        txtNombreTermino.text.clear()
        txtCantdadDias.text.clear()
    }
}