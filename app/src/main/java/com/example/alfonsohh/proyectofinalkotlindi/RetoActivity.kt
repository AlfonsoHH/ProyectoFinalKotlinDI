package com.example.alfonsohh.proyectofinalkotlindi

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import java.util.ArrayList

class RetoActivity : AppCompatActivity() {

    private var nombreUsuario: TextView? = null
    private var fotoUsuario: ImageView? = null

    lateinit var listaRetos: MutableList<Reto>
    lateinit var ref: DatabaseReference
    lateinit var usuarioActual: Usuario
    lateinit var itemDesafio: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reto)

        val myToolbar = findViewById<Toolbar>(R.id.toolbarReto)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setTitle("Retos")

        val activity: Activity = this

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario

        nombreUsuario = findViewById<TextView>(R.id.textViewUsuario)
        fotoUsuario = findViewById<ImageView>(R.id.fotoUsuario)

        nombreUsuario!!.text = usuarioActual.usuario

        //------------------------------------
        val decodedString = Base64.decode(usuarioActual.foto, Base64.DEFAULT)

        val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        Bitmap.createScaledBitmap(imagenJug, 40, 40, false)

        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
        roundedBitmapDrawable.isCircular = true

        fotoUsuario!!.setImageDrawable(roundedBitmapDrawable)
        //--------------------------------------

        val rv = findViewById<RecyclerView>(R.id.rvRetos)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        //Firebase
        ref = FirebaseDatabase.getInstance().getReference("Retos").child(usuarioActual.idUsuario)

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    listaRetos = mutableListOf()
                    for (h in p0.children){
                        val reto = h.getValue(Reto::class.java)
                        listaRetos.add(reto!!)
                    }
                    //le paso al adaptador el array de listas, la actividad y la ref de database
                    var adapter = AdaptadorReto(listaRetos,activity,ref,usuarioActual)
                    rv.adapter = adapter
                }else{
                    listaRetos = mutableListOf()
                    var adapter = AdaptadorReto(listaRetos,activity,ref,usuarioActual)
                    rv.adapter = adapter
                }
            }
        })

    }

    fun agregarReto(v: View){
        val intent = Intent(this, AgregarRetoActivity::class.java)
        intent.putExtra("usuario",usuarioActual)
        startActivity(intent)
    }

    //TOOLBAR MENU

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar_reto, menu)

        itemDesafio = menu.findItem(R.id.desafioMenuItem)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.desafioMenuItem -> {
                val i = Intent(applicationContext, DesafioActivity::class.java)
                i.putExtra("usuario", usuarioActual)
                startActivity(i)
            }

        }
        return true
    }
}
