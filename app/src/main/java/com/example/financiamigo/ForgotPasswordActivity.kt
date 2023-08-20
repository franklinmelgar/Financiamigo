package com.example.financiamigo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        firebaseauth = Firebase.auth

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
            }
        }
    }
}