package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtCorreo: EditText = findViewById(R.id.txtCorreoElectronico);
        val txtcontra: EditText = findViewById(R.id.txtContrasena);
        val btnRegistrar: (Button) = findViewById(R.id.btnRegistrar);
        val lblIniciarSesion: (TextView) = findViewById(R.id.lblIniciarSesion)

        lblIniciarSesion.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener{
            if (txtCorreo.text.isNotEmpty() && txtcontra.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtCorreo.text.toString(), txtcontra.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()
                    }else{
                        Toast.makeText(this, "No se pudo autenticar el correo", Toast.LENGTH_LONG).show()
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

    }

}