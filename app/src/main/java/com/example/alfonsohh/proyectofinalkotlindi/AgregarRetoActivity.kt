package com.example.alfonsohh.proyectofinalkotlindi

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_agegar_reto.*
import java.util.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener



class AgregarRetoActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var usuarioActual: Usuario

    private var nombre: EditText? = null
    private var objetivo: EditText? = null
    private var textoRepeticiones: TextView? = null
    private var repeticiones: EditText? = null
    private var fecha: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agegar_reto)

        nombre = findViewById<EditText>(R.id.editTextRetoNombre)
        objetivo = findViewById<EditText>(R.id.editTextRetoObjetivo)
        textoRepeticiones = findViewById<TextView>(R.id.textViewAgregarRetoTipo)
        repeticiones = findViewById<EditText>(R.id.editTextRetoRepeticiones)
        fecha = findViewById<TextView>(R.id.tvfecha)
        var bt_cita_fecha = findViewById<Button>(R.id.botonFecha)
        var spinner = findViewById<Spinner>(R.id.spinnerTipo)
        var tv_fecha = findViewById<TextView>(R.id.tvfecha)
        var imagenTipo = findViewById<ImageView>(R.id.ivTipo)

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario

        ref = FirebaseDatabase.getInstance().getReference("Retos").child(usuarioActual.idUsuario)

        getDate(bt_cita_fecha,tv_fecha, this)

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

    }

    fun getDate(button: Button,textView: TextView, context: Context){

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            textView.text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)

        }

        button.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun agregar(v: View){
        if (!nombre!!.text.trim().equals("") and !objetivo!!.text.trim().equals("") and !fecha!!.text.trim().equals("")) {
            Log.d("asdf","antesPush")
            val retoId = ref.push().key
            Log.d("asdf","despuesPush")
            val repeticionesNumber: Int = repeticiones!!.text.toString().toInt()
            Log.d("asdf","antesReto")
            val reto = Reto(retoId, nombre!!.text.toString(),objetivo!!.text.toString(),fecha!!.text.toString(),spinnerTipo.selectedItem.toString(),repeticionesNumber,0)
            Log.d("asdf","despuesReto")
            ref.child(retoId).setValue(reto).addOnCompleteListener{
                Toast.makeText(applicationContext, "Reto guardado satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@AgregarRetoActivity, "Alguno de los campos vacios", Toast.LENGTH_LONG).show()
        }

    }

}
