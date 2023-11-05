package com.example.financiamigo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ABActivity : AppCompatActivity() {

    val tiposContacto = arrayOf("Proveedor", "Cliente")
    private lateinit var btRegresar: ImageView

    private lateinit var cmbTipo: Spinner
    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtEmpresa: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCelular: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var btGuardar: Button
    private lateinit var cmbTermino: Spinner

    private lateinit var firebaseauth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abactivity)

        btRegresar = findViewById(R.id.btRegregar)
        btRegresar.setOnClickListener{
            this.finish();
        }

        cmbTipo = findViewById(R.id.cmbTipo)
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtEmpresa = findViewById(R.id.txtEmpresa)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtCelular = findViewById(R.id.txtCelular)
        txtCorreo = findViewById(R.id.txtCorreo)
        cmbTermino = findViewById(R.id.cmbTermino)
        txtDireccion = findViewById(R.id.txtDireccion)
        btGuardar = findViewById(R.id.btGuardar)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposContacto)
        cmbTipo.adapter = arrayAdapter

        btGuardar.setOnClickListener{
            if (validacion().equals(true)){
                guardar(cmbTipo.selectedItem.toString(), txtNombre.text.toString(), txtApellido.text.toString(), txtEmpresa.text.toString(), txtTelefono.text.toString(), txtCelular.text.toString(), txtCorreo.text.toString(), cmbTermino.selectedItem.toString(),  txtDireccion.text.toString() )
            }
        }

        val firebaseauth = Firebase.auth
        val listaTerminos = mutableListOf<String>()
        val bd = Firebase.firestore

        bd.collection("Terminos_Credito")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val termino = document.getString("Nombre_Termino") ?: ""
                    listaTerminos.add(termino)
                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaTerminos)
                cmbTermino.adapter = adapter
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }

    }

    private fun guardar(tipo: String, nombre: String, apellido: String, empresa: String, telefono: String, celular: String,  correo: String, termino: String, direccion: String){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        val cuenta = hashMapOf(
            "Tipo" to tipo,
            "Nombre" to nombre,
            "Apellido" to apellido,
            "Empresa" to empresa,
            "Telefono" to telefono,
            "Celular" to celular,
            "Correo" to correo,
            "Termino" to termino,
            "Direccion" to direccion,
            "Usuario_ID" to firebaseauth.currentUser?.uid.toString()
        )

        // Add a new document with a generated ID
        bd.collection("Contactos")
            .add(cuenta)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Contacto Creado", Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun validacion(): Boolean {

        var continuar = true

        if (txtNombre.text.isEmpty()) {
            txtNombre.error = "Nombre de cuenta obligatorio"
            continuar = false
        }

        return continuar
    }

    private fun limpiarCampos(){
        txtNombre.text.clear()
        txtApellido.text.clear()
        txtEmpresa.text.clear()
        txtTelefono.text.clear()
        txtCelular.text.clear()
        txtCorreo.text.clear()
        txtDireccion.text.clear()
    }
}