package com.example.financiamigo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtCorreo: EditText = findViewById(R.id.txtCorreoElectronico);
        val txtcontra: EditText = findViewById(R.id.txtContrasena);
        val btnRegistrar: (Button) = findViewById(R.id.btnRegistrar);

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
            }
        }

    }

}