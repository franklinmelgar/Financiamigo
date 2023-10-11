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


class AccountActivity : AppCompatActivity() {
    val tiposCuenta = arrayOf("Cuenta de ahorros", "Cuenta de cheques", "Tarjeta de credito", "Tarjeta de debito")
    private lateinit var btnRegresar: ImageView

    private lateinit var txtNombreCuenta: EditText
    private lateinit var txtDescripcionCuenta: EditText
    private lateinit var txtNumeroCuenta: EditText
    private lateinit var txtBanco: EditText
    private lateinit var cmbTipo: Spinner
    private lateinit var txtFecha: EditText
    private lateinit var txtSaldo: EditText
    private lateinit var btGuardar: Button
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        btnRegresar = findViewById(R.id.btRegregar)

        txtNombreCuenta = findViewById(R.id.txtNombreCuenta)
        txtDescripcionCuenta = findViewById(R.id.txtDescripcionCuenta)
        txtNumeroCuenta = findViewById(R.id.txtNumeroCuenta)
        txtBanco = findViewById(R.id.txtBanco)
        cmbTipo = findViewById(R.id.cmbTipo)
        txtFecha = findViewById(R.id.txtFecha)
        txtSaldo = findViewById(R.id.txtSaldo)
        btGuardar = findViewById(R.id.btGuardar)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposCuenta)

        cmbTipo.adapter = arrayAdapter

        btnRegresar.setOnClickListener{
            this.finish();
        }

        btGuardar.setOnClickListener{
            if (validacion().equals(true)){
                guardar(txtNombreCuenta.text.toString(), txtDescripcionCuenta.text.toString(), txtNumeroCuenta.text.toString(), txtBanco.text.toString(), cmbTipo.selectedItem.toString(), txtFecha.text.toString(), txtSaldo.text.toString() )
            }
        }
    }

    private fun guardar(nombre: String, descripcion: String, cuenta: String, banco: String, tipo: String,  fecha: String, saldo: String){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        val cuenta = hashMapOf(
            "Nombre" to nombre,
            "Descripcion" to descripcion,
            "Numero_Cuenta" to cuenta,
            "Nombre_Banco" to banco,
            "Tipo_Cuenta" to tipo,
            "Fecha" to fecha,
            "Saldo" to saldo,
            "Usuario_ID" to firebaseauth.currentUser?.uid.toString()
        )

        // Add a new document with a generated ID
        bd.collection("Cuentas")
            .add(cuenta)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Cuenta Creada", Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }

    }

    private fun validacion(): Boolean {

        var continuar = true

        if (txtNombreCuenta.text.isEmpty()) {
            txtNombreCuenta.error = "Nombre de cuenta obligatorio"
            continuar = false
        }

        if (txtDescripcionCuenta.text.isEmpty()) {
            txtDescripcionCuenta.error = "Descripcion de cuenta obligatorio"
            continuar = false
        }

        if (txtNumeroCuenta.text.isEmpty()) {
            txtNumeroCuenta.error = "Numero de cuenta obligatorio"
            continuar = false
        }

        if (txtBanco.text.isEmpty()) {
            txtBanco.error = "Nombre del banco obligatorio"
            continuar = false
        }

        if (txtFecha.text.isEmpty()) {
            txtFecha.error = "Fecha de apertura de cuenta obligatorio"
            continuar = false
        }

        if (txtSaldo.text.isEmpty()) {
            txtSaldo.error = "Saldo inicial de la cuenta obligatorio"
            continuar = false
        }

        return continuar
    }

    private fun limpiarCampos(){
        txtNombreCuenta.text.clear()
        txtDescripcionCuenta.text.clear()
        txtNumeroCuenta.text.clear()
        txtBanco.text.clear()
        txtFecha.text.clear()
        txtSaldo.text.clear()
    }


}