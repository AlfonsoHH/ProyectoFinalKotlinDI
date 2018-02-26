package com.example.alfonsohh.proyectofinalkotlindi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private var nombreDialog: EditText? = null
    private var passwordDialog: EditText? = null
    private var recordar: CheckBox? = null

    private val PREFS_KEY = "tu_contexto"
    private val ESTADO_BOTON = "estado.boton"
    private val ESTADO_NOMBRE = "estado.nombre"
    private val ESTADO_PASSWORD = "estado.password"

    var prefs: SharedPreferences? = null

    lateinit var listaUsuarios: MutableList<Usuario>
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)

        nombreDialog = findViewById<EditText>(R.id.inputNombre)
        passwordDialog = findViewById<EditText>(R.id.inputPassword)
        recordar = findViewById<CheckBox>(R.id.checkBoxLogin)

        if (obtener_estado_boton()) {
            prefs = this.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            recordar!!.isChecked = true
            nombreDialog!!.setText(prefs!!.getString(ESTADO_NOMBRE,""))
            passwordDialog!!.setText(prefs!!.getString(ESTADO_PASSWORD, ""))
        }

        ref = FirebaseDatabase.getInstance().getReference("Usuarios")

        listaUsuarios = mutableListOf()

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (h in p0.children){
                        val usuario = h.getValue(Usuario::class.java)
                        listaUsuarios.add(usuario!!)
                        Log.d("logeando",usuario.usuario)
                    }
                    //le paso al adaptador el array de listas, la actividad y la ref de database
                }
            }
        })

        //AQUI PEDIMOS LOS PERMISOS

        val needsRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        val needsWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        val camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (needsRead || needsWrite || camera) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 2909)
            }
        }
    }

    fun entrar(v: View) {

        if (!nombreDialog!!.text.trim().toString().equals("") && !passwordDialog!!.text.trim().toString().equals("")) {

            for (usuarioActual: Usuario in listaUsuarios) {
                if (usuarioActual.usuario.equals(nombreDialog!!.text.toString())) {
                    if (usuarioActual.password.equals(passwordDialog!!.text.toString())) {
                        guardar_estado_boton()
                        val i = Intent(applicationContext, RetoActivity::class.java)
                        i.putExtra("usuario",usuarioActual)
                        startActivity(i)
                    } else {
                        Toast.makeText(applicationContext, "El usuario o contraseña son incorrectos", Toast.LENGTH_LONG).show()
                    }
                }
            }

        } else {
            Toast.makeText(v.context, "Uno o más campos se encuentran vacios", Toast.LENGTH_SHORT).show()
        }
    }

    fun guardar_estado_boton() {
        val settings = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor
        editor = settings.edit()
        editor.putBoolean(ESTADO_BOTON, recordar!!.isChecked)
        editor.putString(ESTADO_NOMBRE, nombreDialog!!.text.toString())
        editor.putString(ESTADO_PASSWORD, passwordDialog!!.text.toString())
        editor.apply()
    }

    fun obtener_estado_boton(): Boolean {
        val settings = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        return settings.getBoolean(ESTADO_BOTON, false)

    }

    fun registrarse(v: View) {

        val i = Intent(applicationContext, RegistrarActivity::class.java)
        startActivity(i)

        if (!recordar!!.isChecked) {
            nombreDialog!!.setText("")
            passwordDialog!!.setText("")
        }

    }
}
