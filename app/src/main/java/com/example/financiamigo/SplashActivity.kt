package com.example.financiamigo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var authlistener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseauth = Firebase.auth
        authlistener = FirebaseAuth.AuthStateListener {
            val currentUser = firebaseauth.currentUser
            val id = firebaseauth.currentUser?.uid

            if (currentUser != null){
                val mainActivity = Intent(this, MainActivity::class.java)
                mainActivity.putExtra("id", id);
                startActivity(mainActivity)
            }else{
                val mainActivity = Intent(this, LoginActivity::class.java)
                startActivity(mainActivity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseauth.addAuthStateListener(authlistener)
    }

    override fun onStop() {
        super.onStop()
        if (authlistener != null){
            firebaseauth.removeAuthStateListener(authlistener)
        }
    }
}