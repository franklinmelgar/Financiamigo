package com.example.financiamigo

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {

    val tipoTransaccion = arrayOf("Ingreso", "Gasto")
    private lateinit var txtTitulo: EditText
    private lateinit var btRegresar: ImageView
    private lateinit var cmbTipoTransaccion: Spinner
    private lateinit var cmbCuenta: Spinner
    private lateinit var cmbContacto: Spinner
    private lateinit var txtMonto: EditText
    private lateinit var cmbCategoria: Spinner
    private lateinit var lblFecha: TextView
    private lateinit var cmbTerminoCredito: Spinner
    private lateinit var txtComentario: EditText
    private lateinit var btGuardar: Button
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var myCalendar: Calendar
    private lateinit var btSubirFotografia: Button
    private lateinit var lblnombreImagen: TextView
    private lateinit var btCamara: ImageView
    private lateinit var imagenUri: Uri
    private val IMAGE_REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 1

    private var tipoTransac: String = ""
    private var cuentaTransaccion = ""
    private var contactoTransaccion = ""
    private var categoriaTransaccion = ""
    private var fechaTransaccion = ""
    private var terminoTransaccion = ""
    private var imagenTransaccion = ""
    private var mesTransaccion = ""
    private var anioTrasaccion = ""
    private var diasTermino = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        txtTitulo = findViewById(R.id.txtTitulo)
        btRegresar = findViewById(R.id.btRegregar)
        cmbTipoTransaccion = findViewById(R.id.cmbTipoTransaccion)
        cmbCuenta = findViewById(R.id.cmbCuenta)
        cmbContacto = findViewById(R.id.cmbContacto)
        txtMonto = findViewById(R.id.txtMonto)
        cmbCategoria = findViewById(R.id.cmbCategoriaTransaccion)
        lblFecha = findViewById(R.id.txtFecha)
        cmbTerminoCredito = findViewById(R.id.cmbTermino)
        txtComentario = findViewById(R.id.txtComentario)
        btGuardar = findViewById(R.id.btGuardar)
        btSubirFotografia = findViewById(R.id.btSubirFotografia)
        lblnombreImagen = findViewById(R.id.nombreImagen)
        btCamara = findViewById(R.id.btCamara)

        firebaseauth = Firebase.auth
        val bd = Firebase.firestore

        obtenerTipoTransaccion(cmbTipoTransaccion)
        obtenerCuenta(cmbCuenta)
        establecerFechaInicial(lblFecha)
        obtenerTerminoCredito(cmbTerminoCredito)
        obtenerCategoria(cmbCategoria)

        btSubirFotografia.setOnClickListener{
            escogerFotografia()
        }

        btCamara.setOnClickListener{
            tomarFoto()
        }

        cmbTipoTransaccion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val tipoSeleccionado = parent.getItemAtPosition(position) as String

                if (tipoSeleccionado.equals("Ingreso"))
                    tipoTransac = "Cliente"
                else
                    tipoTransac = "Proveedor"
                obtenerContactos(cmbContacto, tipoTransac)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Haz algo si no se ha seleccionado nada
            }
        }


        cmbCuenta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val cuentaSeleccionado = parent.getItemAtPosition(position) as Cuenta
                cuentaTransaccion = cuentaSeleccionado.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                cuentaTransaccion = ""
            }
        }


        //lblFecha
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" //En el cual deseas mostrar
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            lblFecha.text = sdf.format(myCalendar.time)
            fechaTransaccion = sdf.format(myCalendar.time)
            mesTransaccion = (myCalendar.get(Calendar.MONTH) + 1).toString()
            anioTrasaccion = myCalendar.get(Calendar.YEAR).toString()
        }

        lblFecha.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        cmbTerminoCredito.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val terminoSeleccionado = parent.getItemAtPosition(position) as Termino_Credito
                terminoTransaccion = terminoSeleccionado.id
                diasTermino = terminoSeleccionado.Cantidad_Dias.toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                terminoTransaccion = ""
            }
        }


        // Contacto
        cmbContacto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val contactoSeleccionado = parent.getItemAtPosition(position) as Contacto
                contactoTransaccion = contactoSeleccionado.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                contactoTransaccion = ""
            }
        }

        // Categoria
        cmbCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val categoriaSeleccionado = parent.getItemAtPosition(position) as Categoria
                categoriaTransaccion = categoriaSeleccionado.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                categoriaTransaccion = ""
            }
        }

        btRegresar.setOnClickListener{
            this.finish()
        }

        btGuardar.setOnClickListener {
            if (validacion().equals(true)){

                var fechaVencimiento = ""
                var estado = "Pendiente"

                if (diasTermino.toString().equals("0")){
                    estado = "Pagado"
                    fechaVencimiento = fechaTransaccion
                }else{
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fechaFinal = format.parse(fechaTransaccion)

                    val calendar = Calendar.getInstance()
                    calendar.time = fechaFinal
                    calendar.add(Calendar.DATE, diasTermino.toInt())

                    fechaVencimiento = format.format(calendar.time)
                }

                if (imagenUri != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imagenUri)
                    saveImageToFirebaseStorage(bitmap, txtTitulo.text.toString(), txtMonto.text.toString(), tipoTransac, cuentaTransaccion, contactoTransaccion, categoriaTransaccion, fechaTransaccion, terminoTransaccion, txtComentario.text.toString(), imagenTransaccion, mesTransaccion, anioTrasaccion, fechaVencimiento, estado)
                }else{
                    guardar(txtTitulo.text.toString(), txtMonto.text.toString(), tipoTransac, cuentaTransaccion, contactoTransaccion, categoriaTransaccion, fechaTransaccion, terminoTransaccion, txtComentario.text.toString(), imagenTransaccion, mesTransaccion, anioTrasaccion, fechaVencimiento, estado )
                    limpiar()
                }
            }
        }
    }

    private fun saveImageToFirebaseStorage(bitmap: Bitmap, titulo: String, montoTransaccion: String, tipoTransac: String, cuentaTransaccion: String, contactoTransaccion: String, categoriaTransaccion: String, fechaTransaccion: String, terminoTransaccion: String, comentarioTransaccion: String, imagenTransaccion: String, mesTransaccion: String, anioTransaccion: String, fechaVencimiento: String, estado: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()

                guardar(
                    titulo,
                    montoTransaccion,
                    tipoTransac,
                    cuentaTransaccion,
                    contactoTransaccion,
                    categoriaTransaccion,
                    fechaTransaccion,
                    terminoTransaccion,
                    comentarioTransaccion,
                    downloadUrl.toString(),
                    mesTransaccion,
                    anioTransaccion,
                    fechaVencimiento,
                    estado
                )
                limpiar()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al subir la imagen a Firebase Storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tomarFoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
        val resolver = contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val outputStream = imageUri?.let { resolver.openOutputStream(it) }
        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiar(){
        txtMonto.text.clear()
        txtComentario.text.clear()
        txtMonto.requestFocus()
    }

    private fun guardar(titulo: String, montoTransaccion: String, tipoTransac: String, cuentaTransaccion: String, contactoTransaccion: String, categoriaTransaccion: String, fechaTransaccion: String, terminoTransaccion: String, comentarioTransaccion: String, imagenTransaccion: String, mesTransaccion: String, anioTransaccion: String, fechaVencimiento: String, estado: String) {

        firebaseauth = Firebase.auth

        val bd = Firebase.firestore

        val transaccion = hashMapOf(
            "tituloTransaccion" to titulo,
            "montoTransaccion" to montoTransaccion,
            "tipoTransaccion" to tipoTransac,
            "cuentaTransaccion" to cuentaTransaccion,
            "contactoTransaccion" to contactoTransaccion,
            "categoriaTransaccion" to categoriaTransaccion,
            "fechaTransaccion" to fechaTransaccion,
            "terminoTransaccion" to terminoTransaccion,
            "comentarioTransaccion" to comentarioTransaccion,
            "imagenTransaccion" to imagenTransaccion,
            "mesTransaccion" to mesTransaccion,
            "anioTransaccion" to anioTransaccion,
            "Usuario_ID" to firebaseauth.currentUser?.uid.toString(),
            "fechaVencimiento" to fechaVencimiento,
            "estado" to estado
        )

        // Add a new document with a generated ID
        bd.collection("Transacciones")
            .add(transaccion)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Transaccion Creada", Toast.LENGTH_LONG).show()
                //limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun validacion(): Boolean {
        var continuar = true

        if (txtMonto.text.isEmpty()) {
            txtMonto.error = "Monto obligatorio"
            continuar = false
        }

        return continuar
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode.equals(IMAGE_REQUEST_CODE) and resultCode.equals(RESULT_OK)){
            imagenUri = data?.data!!
            val nombreImagen = obtenerNombreArchivo(imagenUri)
            lblnombreImagen.text = nombreImagen
            imagenTransaccion = nombreImagen
        }else if (requestCode.equals(REQUEST_IMAGE_CAPTURE) and resultCode.equals(RESULT_OK)){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            saveImageToGallery(imageBitmap)
        }
    }
    private fun escogerFotografia(){
        val IMAGE_REQUEST_CODE = 100
        val galeria = Intent(Intent.ACTION_PICK)
        galeria.type = "image/*"
        startActivityForResult(galeria, IMAGE_REQUEST_CODE)
    }

    private fun obtenerNombreArchivo(uri: Uri?): String {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return if (uri != null) {
            val returnCursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            val nombre = returnCursor?.getString(nameIndex ?: 0)
            returnCursor?.close()
            nombre ?: ""
        } else {
            "Desconocido"
        }
    }


    private fun obtenerTipoTransaccion (cmbTipoTransaccion: Spinner){
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipoTransaccion)
        cmbTipoTransaccion.adapter = arrayAdapter
    }

    private fun obtenerContactos(cmbContacto: Spinner, tipo: String) {
        val listaContactos = mutableListOf<Contacto>()
        val bd = Firebase.firestore

        bd.collection("Contactos")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .whereEqualTo("Tipo", tipo)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val contacto = Contacto(document.getString("Tipo") ?: "", document.getString("Nombre") ?: "", document.getString("Apellido") ?: "", document.getString("Empresa") ?: "", document.getString("Telefono") ?: "", document.getString("Celular") ?: "", document.getString("Correo") ?: "", document.getString("Direccion") ?: "", document.getString("Usuario_ID") ?: "", document.id ?: "")
                    listaContactos.add(contacto)
                }
                val adapter = ContactoAdapterSpinner(applicationContext, android.R.layout.simple_list_item_1, android.R.id.text1, listaContactos)
                cmbContacto.adapter = adapter

            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun obtenerCuenta(cmbCuenta: Spinner) {

        val bd = Firebase.firestore
        val listaCuentas = mutableListOf<Cuenta>()
        bd.collection("Cuentas")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val cuenta = Cuenta(document.getString("Nombre") ?: "", document.getString("Descripcion") ?: "", document.getString("Numero_Cuenta") ?: "", document.getString("Nombre_Banco") ?: "", document.getString("Tipo_Cuenta") ?: "", document.getString("Fecha") ?: "", document.getString("Saldo") ?: "", document.getString("Usuario_ID") ?: "", document.id ?: "")
                    listaCuentas.add(cuenta)
                }

                val adapter = CuentaAdapterSpinner(applicationContext, android.R.layout.simple_list_item_1, android.R.id.text1, listaCuentas)
                cmbCuenta.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun establecerFechaInicial(lblFecha: TextView){
        myCalendar = Calendar.getInstance()

        val fechaActial = Calendar.getInstance().time
        val dateFormato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fecha = dateFormato.format(fechaActial)

        lblFecha.text = fecha
    }

    private fun obtenerTerminoCredito(cmbTerminoCredito: Spinner){
        val bd = Firebase.firestore
        val listaTerminos = mutableListOf<Termino_Credito>()
        bd.collection("Terminos_Credito")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val termino = Termino_Credito(document.getString("Nombre_Termino") ?: "", document.getString("Cantidad_Dias") ?: "", document.getString("Usuario_ID") ?: "", document.id ?: "")
                    listaTerminos.add(termino)
                }

                val adapter = TerminoAdapterSpinner(applicationContext, android.R.layout.simple_list_item_1, android.R.id.text1, listaTerminos)
                cmbTerminoCredito.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun obtenerCategoria(cmbCategoria: Spinner){
        val bd = Firebase.firestore
        val listaCategorias = mutableListOf<Categoria>()
        bd.collection("Categoria")
            .whereEqualTo("Usuario_ID", firebaseauth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val categoria = Categoria(document.getString("Nombre") ?: "", document.getString("Icono") ?: "", document.getString("Color") ?: "", document.getString("Usuario_ID") ?: "", document.id ?: "")
                    listaCategorias.add(categoria)
                }

                val adapter = CategoriaAdapterSpinner(applicationContext, android.R.layout.simple_list_item_1, android.R.id.text1, listaCategorias)
                cmbCategoria.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }
}