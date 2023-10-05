package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var firebaseauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnEnviarCorreo: Button = findViewById(R.id.btEnviarCorreo)
        val txtCorreo: EditText = findViewById(R.id.txtCorreoElectronico)
        val lblLogin: TextView = findViewById(R.id.lblIniciarSesion)

        firebaseauth = Firebase.auth

        lblLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnEnviarCorreo.setOnClickListener{
            if (txtCorreo.text.isNotEmpty()){
                firebaseauth.sendPasswordResetEmail(txtCorreo.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "Correo electronico enviado", Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(this, "Error al enviar electronico enviado", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                if (txtCorreo.text.isEmpty()){
                    txtCorreo.setError("Correo Electronico Requerido")
                }
            }
        }
    }
}