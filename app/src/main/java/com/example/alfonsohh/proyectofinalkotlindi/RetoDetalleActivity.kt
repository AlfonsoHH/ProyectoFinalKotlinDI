package com.example.alfonsohh.proyectofinalkotlindi

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class RetoDetalleActivity : AppCompatActivity() {

    private var fotoTipo: ImageView? = null
    private var nombreReto: TextView? = null
    private var objetivoReto: TextView? = null
    private var fechaReto: TextView? = null
    private var progresoReto: TextView? = null
    private var barraProgreso: ProgressBar? = null
    private var botonProgreso: ImageButton? = null

    lateinit var usuarioActual: Usuario
    lateinit var retoActual: Reto

    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reto_detalle)

        nombreReto = findViewById<TextView>(R.id.textViewRetoDetalleNombre)
        objetivoReto = findViewById<TextView>(R.id.textViewRetoDetalleObjetivo)
        fechaReto = findViewById<TextView>(R.id.textViewRetoDetalleFecha)
        progresoReto = findViewById<TextView>(R.id.textViewRetoDetalleProgreso)
        fotoTipo = findViewById<ImageView>(R.id.imageViewRetoDetalle)
        botonProgreso = findViewById<ImageButton>(R.id.imageButtonRetoDetalle)
        barraProgreso = findViewById<ProgressBar>(R.id.progressBarRetoDetalle)

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario
        retoActual = intent.getSerializableExtra("reto") as Reto

        nombreReto!!.text = retoActual.nombre
        objetivoReto!!.text = retoActual.objetivo
        fechaReto!!.text = retoActual.fecha

        when(retoActual.tipo){
            "Dieta" -> fotoTipo?.setImageResource(R.drawable.icono_grande_dieta)
            "Ejercicio" -> fotoTipo?.setImageResource(R.drawable.icono_grande_ejercicio)
            "Libros" -> fotoTipo?.setImageResource(R.drawable.icono_grande_libros)
            "Amigos" -> fotoTipo?.setImageResource(R.drawable.icono_grande_amigos)
            "Viajes" -> fotoTipo?.setImageResource(R.drawable.icono_grande_viajar)
        }

        barraProgreso!!.progress = retoActual.progreso

        var repeticiones: Int = retoActual.progreso/(100/retoActual.repeticiones)

        when(retoActual.tipo){
            "Dieta" -> progresoReto!!.text = "Has perdido "+repeticiones.toString()+" kilos de "+retoActual.repeticiones
            "Ejercicio" -> progresoReto!!.text = "Has realizado "+repeticiones.toString()+" rutinas de "+retoActual.repeticiones
            "Libros" -> progresoReto!!.text = "Has leido "+repeticiones.toString()+" libros de "+retoActual.repeticiones
            "Amigos" -> progresoReto!!.text = "Has contactado con "+repeticiones.toString()+" personas queridas de "+retoActual.repeticiones
            "Viajes" -> progresoReto!!.text = "Has viajado a "+repeticiones.toString()+" destinos de "+retoActual.repeticiones
        }


        ref = FirebaseDatabase.getInstance().getReference("Retos").child(usuarioActual.idUsuario)

    }

    fun aumentarProgreso(v: View){

        val fecha: Date = Date()

        val date = SimpleDateFormat("dd/MM/yyyy").parse(retoActual.fecha)

        if(date.after(fecha)) {

            if (retoActual.progreso <= 99) {

                retoActual.progreso = retoActual.progreso + (100 / retoActual.repeticiones)
                barraProgreso!!.progress = retoActual.progreso
                var repeticiones: Int = retoActual.progreso / (100 / retoActual.repeticiones)

                when (retoActual.tipo) {
                    "Dieta" -> progresoReto!!.text = "Has perdido " + repeticiones.toString() + " kilos de " + retoActual.repeticiones
                    "Ejercicio" -> progresoReto!!.text = "Has realizado " + repeticiones.toString() + " rutinas de " + retoActual.repeticiones
                    "Libros" -> progresoReto!!.text = "Has leido " + repeticiones.toString() + " libros de " + retoActual.repeticiones
                    "Amigos" -> progresoReto!!.text = "Has contactado con " + repeticiones.toString() + " personas queridas de " + retoActual.repeticiones
                    "Viajes" -> progresoReto!!.text = "Has viajado a " + repeticiones.toString() + " destinos de " + retoActual.repeticiones
                }

                val reto = Reto(retoActual.idReto, retoActual.nombre, retoActual.objetivo, retoActual.fecha, retoActual.tipo, retoActual.repeticiones, retoActual.progreso)
                ref.child(retoActual.idReto).setValue(reto).addOnCompleteListener {
                    Toast.makeText(this, "Reto guardado satisfactoriamente", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "El reto ya ha terminado", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "El reto ya ha terminado", Toast.LENGTH_SHORT).show()
        }

    }

}
