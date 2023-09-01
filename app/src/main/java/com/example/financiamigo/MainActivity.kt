package com.example.financiamigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menu: (BottomNavigationView) = findViewById(R.id.bottonNavigationView)
    }

}