package com.example.alfonsohh.proyectofinalkotlindi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class RegistrarActivity : AppCompatActivity() {

    private var nombre: EditText? = null
    private var password: EditText? = null
    private var url: String = "url"
    private var imagen: ImageView? = null

    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        nombre = findViewById<EditText>(R.id.inputRegistrarNombre)
        password = findViewById<EditText>(R.id.inputRegistrarPassword)
        imagen = findViewById<ImageView>(R.id.fotoRegistrar)

        ref = FirebaseDatabase.getInstance().getReference("Usuarios")

    }

    fun registrarPerfil(v: View) {

        if ((!nombre!!.text.trim().equals("")) and (!password!!.text.trim().equals(""))) {
            val usuarioId = ref.push().key
            val usuario = Usuario(usuarioId, nombre!!.text.toString(),password!!.text.toString(),url)
            ref.child(usuarioId).setValue(usuario).addOnCompleteListener{
                Toast.makeText(applicationContext, "Lista guardada satisfactoriamente", Toast.LENGTH_SHORT).show()
                finish()
            }

        } else {
            Toast.makeText(this@RegistrarActivity, "Usuario o contraseña vacios", Toast.LENGTH_LONG).show()
        }
    }

    fun registrarFoto(v: View) {

        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data
            val roundedDrawable = RoundedBitmapDrawableFactory.create(applicationContext.resources, BitmapFactory.decodeFile(getRealPathFromURI(applicationContext, pickedImage)))
            roundedDrawable.isCircular = true
            imagen!!.setImageDrawable(roundedDrawable)
            imagen!!.adjustViewBounds = true

            var filePath = arrayOf(MediaStore.Images.Media.DATA)
            var cursor = contentResolver.query(pickedImage!!, filePath, null, null, null)
            cursor!!.moveToFirst()
            var imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))

            var options = BitmapFactory.Options()

            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            var bitmapRedux = BitmapFactory.decodeFile(imagePath, options)

            var stream = ByteArrayOutputStream()

            bitmapRedux.compress(Bitmap.CompressFormat.PNG, 100, stream)

            url = Base64.encodeToString(stream.toByteArray(), 0)

            cursor.close()
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

}
