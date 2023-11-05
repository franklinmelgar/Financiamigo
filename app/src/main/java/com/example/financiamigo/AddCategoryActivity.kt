package com.example.financiamigo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var btRegresar: ImageView
    private lateinit var listaColores: LinearLayout
    private lateinit var listaIconos: GridLayout
    private var colorSeleccionado: String = ""
    private var iconoSeleccionado: String = ""
    private lateinit var btGuardar: Button
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        txtNombre = findViewById(R.id.txtNombre)
        btRegresar = findViewById(R.id.btRegregar)
        listaColores = findViewById(R.id.listaColores)
        listaIconos = findViewById(R.id.listaIconos)
        btGuardar = findViewById(R.id.btGuardar)

        btRegresar.setOnClickListener{
            this.finish()
        }

        //Llenar el listivew
        val colors = listOf(
            R.drawable.item_circle_blue,
            R.drawable.item_circle_yellow,
            R.drawable.item_circle_green,
            R.drawable.item_circle_pink,
            R.drawable.item_circle_purple,
            R.drawable.item_circle_brown,
            R.drawable.item_circle_blue_grey,
            R.drawable.item_circle_red,
            R.drawable.item_circle_indigo,
            R.drawable.item_circle_lime
        )

        for (color in colors) {

            val imageView = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.image_size),
                resources.getDimensionPixelSize(R.dimen.image_size)
            )
            layoutParams.setMargins(10, 10, 10, 10)
            imageView.layoutParams = layoutParams
            imageView.setImageResource(color)
            imageView.setOnClickListener {
                // Deseleccionar todos los ImageViews en el LinearLayout
                for (i in 0 until listaColores.childCount) {
                    val child = listaColores.getChildAt(i)
                    if (child is ImageView) {
                        child.isSelected = false
                        child.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                // Seleccionar el ImageView actual
                it.isSelected = true
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.empty_bg_color))

                // Obtener el color del ImageView
                colorSeleccionado = when (color) {
                    R.drawable.item_circle_blue -> "Blue"
                    R.drawable.item_circle_yellow -> "yellow"
                    R.drawable.item_circle_green -> "green"
                    R.drawable.item_circle_pink -> "pink"
                    R.drawable.item_circle_purple -> "purple"
                    R.drawable.item_circle_brown -> "brown"
                    R.drawable.item_circle_blue_grey -> "blue_gray"
                    R.drawable.item_circle_red -> "red"
                    R.drawable.item_circle_indigo -> "indigo"
                    R.drawable.item_circle_lime -> "lime"
                    else -> "Desconocido"
                }
            }
            listaColores.addView(imageView)
        }

        //Lista de iconos
        val drawables = listOf(
            R.drawable.ic_cat_alquiler,
            R.drawable.ic_cat_education,
            R.drawable.ic_cat_food,
            R.drawable.ic_cat_alquiler,
            R.drawable.ic_cat_gif,
            R.drawable.ic_cat_health,
            R.drawable.ic_cat_invoice,
            R.drawable.ic_cat_supermarket,
            R.drawable.ic_cat_transportation
        )

        var iconoSeleccionadoImage: ImageView? = null

        for (icono in drawables) {
            val frameLayout = FrameLayout(this)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            frameLayout.layoutParams = params

            val imageView = ImageView(this)
            val sizeInDp = (40 * resources.displayMetrics.density).toInt()
            val layoutParams = FrameLayout.LayoutParams(sizeInDp, sizeInDp)
            layoutParams.gravity = Gravity.CENTER
            layoutParams.setMargins(5, 5, 5, 5)
            imageView.layoutParams = layoutParams
            imageView.setImageResource(icono)

            imageView.setOnClickListener {
                iconoSeleccionado = resources.getResourceEntryName(icono)
                //Toast.makeText(this, "Icono seleccionado: $nombreIcono", Toast.LENGTH_LONG).show()

                if (iconoSeleccionadoImage != null) {
                    iconoSeleccionadoImage!!.setBackgroundColor(Color.TRANSPARENT)
                }

                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.empty_bg_color))
                iconoSeleccionadoImage = imageView
            }

            frameLayout.addView(imageView)
            listaIconos.addView(frameLayout)
        }


        btGuardar.setOnClickListener {
            if (validacion().equals(true)){
                guardar(txtNombre.text.toString(), iconoSeleccionado, colorSeleccionado )
            }
        }

    }

    private fun guardar(nombre: String, icono: String, color: String){

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        val categoria = hashMapOf(
            "Nombre" to nombre,
            "Icono" to icono,
            "Color" to color,
            "Usuario_ID" to firebaseauth.currentUser?.uid.toString()
        )

        // Add a new document with a generated ID
        bd.collection("Categoria")
            .add(categoria)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Categoria Creada", Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun validacion(): Boolean {

        var continuar = true

        if (txtNombre.text.isEmpty()) {
            txtNombre.error = "Nombre de cuenta obligatorio"
            continuar = false
        }

        if (iconoSeleccionado.equals("")){
            Toast.makeText(this, "Debe seleccionar un icono", Toast.LENGTH_LONG).show()
            continuar = false
        }

        if (colorSeleccionado.equals("")){
            Toast.makeText(this, "Debe seleccionar un color", Toast.LENGTH_LONG).show()
            continuar = false
        }

        return continuar
    }

    private fun limpiarCampos(){
        txtNombre.text.clear()
        iconoSeleccionado = ""
        colorSeleccionado = ""
    }



}