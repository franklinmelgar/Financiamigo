package com.example.financiamigo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var lblTerminoCredito: RelativeLayout
    private lateinit var lblCategoria: RelativeLayout
    private lateinit var lblNotificaciones: RelativeLayout

    private lateinit var lblContrasena: RelativeLayout
    private lateinit var lblEliminarCuenta: RelativeLayout
    private lateinit var lblCerrarSesion: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_setting, container, false)

        lblCategoria = view.findViewById(R.id.lblcategorias)
        lblTerminoCredito = view.findViewById(R.id.lblterminocredito)
        lblCerrarSesion = view.findViewById(R.id.lblCerrarSesion)

        lblCategoria.setOnClickListener{
            val categorya = Intent(activity, CategoryActivity::class.java)
            startActivity(categorya)
        }

        lblTerminoCredito.setOnClickListener{
            val terminoCredito = Intent(activity, PaymentTermActivity::class.java)
            startActivity(terminoCredito)
        }

        lblCerrarSesion.setOnClickListener{
            mostrarDialogoCerrarSesion()
        }

        return view
    }

    private fun mostrarDialogoCerrarSesion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")

        builder.setPositiveButton("Sí") { dialog, _ ->
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}