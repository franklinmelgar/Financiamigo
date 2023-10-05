package com.example.financiamigo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var menu : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getStringExtra("id")

        loadFragment(HomeFragment(), userId.toString());

        menu = findViewById(R.id.bottonNavigationView)
        menu.setOnItemSelectedListener {
            when (it.itemId){

                R.id.home ->{
                    loadFragment(HomeFragment(), userId.toString())
                    true
                }

                R.id.wallet ->{
                    loadFragment(WalletFragment(), userId.toString())
                    true
                }

                R.id.add ->{
                    loadFragment(AddFragment(), userId.toString())
                    true
                }

                R.id.addressbook -> {
                    loadFragment(ABFragment(), userId.toString())
                    true
                }

                R.id.settings ->{
                    loadFragment(SettingFragment(), userId.toString())
                    true
                }

                else -> {
                    loadFragment(HomeFragment(), userId.toString())
                    true
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment, idUsuario: String){
        val dataBundle: Bundle? = null
        dataBundle?.putString("idUser", idUsuario)
        fragment.arguments = dataBundle

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

}