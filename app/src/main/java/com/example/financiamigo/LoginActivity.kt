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

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        val registrar: TextView = findViewById(R.id.lblRegistrar)
        val btnIniiarSesion: Button = findViewById(R.id.btIniciarSesion)
        val txtCorreo: EditText = findViewById(R.id.txtCorreoElectronico)
        val txtcontra: EditText = findViewById(R.id.txtContrasena)
        val olvidoContrasena: TextView = findViewById(R.id.lblOlvidoContrasena)

        firebaseauth = Firebase.auth

        registrar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnIniiarSesion.setOnClickListener{
            if (txtCorreo.text.isNotEmpty() && txtcontra.text.isNotEmpty()){
                firebaseauth.signInWithEmailAndPassword(txtCorreo.text.toString(), txtCorreo.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        val mainActivity = Intent(this, MainActivity::class.java)
                        startActivity(mainActivity)
                    }else{
                        Toast.makeText(this, "Usuario y contraseña incorrectos", Toast.LENGTH_LONG).show()
                    }
                }
            }else{

                if (txtCorreo.text.isEmpty()){
                    txtCorreo.setError("Correo Electronico Requerido")
                }

                if (txtcontra.text.isEmpty()) {
                    txtcontra.setError("Contraseña Requerida")
                }
            }

        }

        olvidoContrasena.setOnClickListener{
            val olvidoContrasena = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(olvidoContrasena)
        }

    }
}