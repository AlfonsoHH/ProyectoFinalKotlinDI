package com.example.alfonsohh.proyectofinalkotlindi

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_desafio.*
import kotlinx.android.synthetic.main.activity_reto.*
import java.util.ArrayList

class DesafioActivity : AppCompatActivity() {

    lateinit var listaDesafios: MutableList<Desafio>
    lateinit var ref: DatabaseReference
    lateinit var usuarioActual: Usuario
    lateinit var itemReto: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desafio)

        val myToolbar = findViewById<Toolbar>(R.id.toolbarDesafio)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setTitle("Desafios")

        val activity: Activity = this

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario

        val rv = findViewById<RecyclerView>(R.id.rvDesafio)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        //Firebase
        ref = FirebaseDatabase.getInstance().getReference("Desafios")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    listaDesafios = mutableListOf()
                    for (h in p0.children){
                        val desafio = h.getValue(Desafio::class.java)
                        if(desafio.retadorNombre.equals(usuarioActual.usuario) or desafio.retadoNombre.equals(usuarioActual.usuario)) {
                            listaDesafios.add(desafio!!)
                        }
                    }
                    //le paso al adaptador el array de listas, la actividad y la ref de database
                    var adapter = AdaptadorDesafio(listaDesafios,activity,ref,usuarioActual)
                    rv.adapter = adapter
                }else{
                    listaDesafios = mutableListOf()
                    var adapter = AdaptadorDesafio(listaDesafios,activity,ref,usuarioActual)
                    rv.adapter = adapter
                }
            }
        })
    }

    fun agregarDesafio(v: View){
        val intent = Intent(this, AgregarDesafioActivity::class.java)
        intent.putExtra("usuario",usuarioActual)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar_desafio, menu)

        itemReto = menu.findItem(R.id.retoMenuItem)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.retoMenuItem -> {
                val i = Intent(applicationContext, RetoActivity::class.java)
                i.putExtra("usuario", usuarioActual)
                startActivity(i)
            }

        }
        return true
    }
}
