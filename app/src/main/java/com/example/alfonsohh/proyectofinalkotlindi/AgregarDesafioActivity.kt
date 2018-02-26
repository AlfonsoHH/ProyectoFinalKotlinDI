package com.example.alfonsohh.proyectofinalkotlindi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_agegar_reto.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_agregar_desafio.*

class AgregarDesafioActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var refUsu: DatabaseReference
    lateinit var listaUsuarios: MutableList<Usuario>
    lateinit var usuarioActual: Usuario

    private var nombre: EditText? = null
    private var objetivo: EditText? = null
    private var textoRepeticiones: TextView? = null
    private var repeticiones: EditText? = null
    private var retado: EditText? = null
    lateinit var spinner: Spinner
    private var imagenTipo: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_desafio)

        Log.d("logero","antes de findview")

        nombre = findViewById<EditText>(R.id.editTextDesafioNombre)
        objetivo = findViewById<EditText>(R.id.editTextDesafioObjetivo)
        textoRepeticiones = findViewById<TextView>(R.id.textViewAgregarDesafioTipo)
        repeticiones = findViewById<EditText>(R.id.editTextDesafioRepeticiones)
        retado = findViewById<EditText>(R.id.editTextAgregarDesafioNombreRetado)
        spinner = findViewById<Spinner>(R.id.spinnerDesafio)
        imagenTipo = findViewById<ImageView>(R.id.ivDesafioTipo)

        Log.d("logero","despues de findview")

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario

        Log.d("logero","despues de usuarioActual")

        ref = FirebaseDatabase.getInstance().getReference("Desafios")

        Log.d("logero","despues de base de datos")

        val listaTipos: MutableList<String> = mutableListOf("Libros", "Ejercicio", "Amigos","Dieta","Viajes")
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaTipos)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(spinnerArrayAdapter)

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                when(spinner.selectedItem.toString()){
                    "Dieta" -> imagenTipo?.setImageResource(R.drawable.icono_grande_dieta)
                    "Ejercicio" -> imagenTipo?.setImageResource(R.drawable.icono_grande_ejercicio)
                    "Libros" -> imagenTipo?.setImageResource(R.drawable.icono_grande_libros)
                    "Amigos" -> imagenTipo?.setImageResource(R.drawable.icono_grande_amigos)
                    "Viajes" -> imagenTipo?.setImageResource(R.drawable.icono_grande_viajar)
                }
                when(spinner.selectedItem.toString()){
                    "Dieta" -> textoRepeticiones!!.text = "Peso a perder"
                    "Ejercicio" -> textoRepeticiones!!.text = "Número de rutinas a realizar"
                    "Libros" -> textoRepeticiones!!.text = "Número de libros a leer"
                    "Amigos" -> textoRepeticiones!!.text = "Número de veces que vas a contactar"
                    "Viajes" -> textoRepeticiones!!.text = "Número de viajes que vas a realizar"
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }

        refUsu = FirebaseDatabase.getInstance().getReference("Usuarios")

        listaUsuarios = mutableListOf()

        refUsu.addValueEventListener(object: ValueEventListener{
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

    }

    fun agregar(v: View){
        var existeUsuario: Boolean = false
        Log.d("logero","dentro agregar")
        if (!nombre!!.text.trim().equals("") and !objetivo!!.text.trim().equals("") and !repeticiones!!.text.trim().equals("") and !retado!!.text.trim().equals("")) {
            Log.d("logero","despues no hay campos vacios")
            for (usuarioComprobar: Usuario in listaUsuarios) {

                Log.d("logero",usuarioComprobar.usuario)

                if (usuarioComprobar.usuario.equals(retado!!.text.toString())) {

                    Log.d("logero","dentro existe un usuario con ese nombre")

                    val desafioId = ref.push().key
                    Log.d("logero","despues de push")
                    val repeticionesNumber: Int = repeticiones!!.text.toString().toInt()
                    Log.d("logero",desafioId)
                    Log.d("logero",nombre!!.text.toString())
                    Log.d("logero",objetivo!!.text.toString())
                    Log.d("logero",spinner.selectedItem.toString())
                    Log.d("logero",repeticionesNumber.toString())
                    Log.d("logero",usuarioActual.usuario)
                    Log.d("logero",retado!!.text.toString())
                    Log.d("logero",usuarioActual.foto)
                    Log.d("logero",usuarioComprobar.foto)

                    nombre!!.text.toString()
                    val desafio = Desafio(desafioId, nombre!!.text.toString(), objetivo!!.text.toString(), spinner.selectedItem.toString(), repeticionesNumber, 0, 0, usuarioActual.usuario, retado!!.text.toString(), usuarioActual.foto,usuarioComprobar.foto,false)

                    ref.child(desafioId).setValue(desafio).addOnCompleteListener {
                        Toast.makeText(applicationContext, "Desafio guardado satisfactoriamente", Toast.LENGTH_SHORT).show()
                    }
                    existeUsuario=true
                }
            }
            if(!existeUsuario){
                Toast.makeText(this@AgregarDesafioActivity, "El usuario al que intenta retar no existe", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@AgregarDesafioActivity, "Alguno de los campos vacío", Toast.LENGTH_LONG).show()
        }

    }

}
