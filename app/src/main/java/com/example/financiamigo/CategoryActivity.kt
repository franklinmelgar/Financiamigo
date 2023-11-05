package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {

    private lateinit var btRegresar: ImageView
    private lateinit var txtBuscar: EditText
    private lateinit var listadoCategorias: ListView
    private lateinit var btAgregar: FloatingActionButton
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        btRegresar = findViewById(R.id.btRegregar)
        txtBuscar = findViewById(R.id.txtBuscar)
        listadoCategorias = findViewById(R.id.listadoCategorias)
        btAgregar = findViewById(R.id.fabAdd)

        obtenerCategorias(listadoCategorias)

        btRegresar.setOnClickListener{
            this.finish()
        }

        btAgregar.setOnClickListener{
            val agregar = Intent(this, AddCategoryActivity::class.java)
            startActivity(agregar)
        }

    }

    private fun obtenerCategorias(listadoCategorias: ListView){
        var listaCategorias = mutableListOf<Categoria>()

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

                val adapter = CategoriaAdapter(this, listaCategorias)

                listadoCategorias.adapter = adapter

            }
            .addOnFailureListener { e ->
                //Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }
}