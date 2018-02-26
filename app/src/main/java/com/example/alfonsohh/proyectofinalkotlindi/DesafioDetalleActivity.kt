package com.example.alfonsohh.proyectofinalkotlindi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Base64
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class DesafioDetalleActivity : AppCompatActivity() {

    private var fotoRetador: ImageView? = null
    private var fotoRetado: ImageView? = null
    private var fotoDesafioTipoArriba: ImageView? = null
    private var fotoDesafioTipoAbajo: ImageView? = null
    private var nombreRetador: TextView? = null
    private var nombreRetado: TextView? = null
    private var nombreDesafio: TextView? = null
    private var objetivoDesafio: TextView? = null
    private var progresoRetador: TextView? = null
    private var progresoRetado: TextView? = null
    private var barraProgresoRetador: ProgressBar? = null
    private var barraProgresoRetado: ProgressBar? = null
    private var botonProgresoRetador: ImageButton? = null
    private var botonProgresoRetado: ImageButton? = null

    lateinit var usuarioActual: Usuario
    lateinit var desafioActual: Desafio
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desafio_detalle)

        nombreRetador = findViewById<TextView>(R.id.textViewDesafioDetalleNombreRetador)
        nombreRetado = findViewById<TextView>(R.id.textViewDesafioDetalleNombreRetado)
        objetivoDesafio = findViewById<TextView>(R.id.textViewDesafioDetalleObjetivo)
        nombreDesafio = findViewById<TextView>(R.id.textViewDesafioDetalleNombre)
        progresoRetador = findViewById<TextView>(R.id.textViewDesafioDetalleProgresoRetador)
        progresoRetado = findViewById<TextView>(R.id.textViewDesafioDetalleProgresoRetado)
        fotoRetador = findViewById<ImageView>(R.id.ivFotoRetador)
        fotoRetado = findViewById<ImageView>(R.id.ivFotoRetado)
        fotoDesafioTipoArriba = findViewById<ImageView>(R.id.ivDesafioTipoArriba)
        fotoDesafioTipoAbajo = findViewById<ImageView>(R.id.ivDesafioTipoAbajo)
        botonProgresoRetador = findViewById<ImageButton>(R.id.imageButtonDesafioDetalleRetador)
        botonProgresoRetado = findViewById<ImageButton>(R.id.imageButtonDesafioDetalleRetado)
        barraProgresoRetador = findViewById<ProgressBar>(R.id.progressBarDesafioDetalleRetador)
        barraProgresoRetado = findViewById<ProgressBar>(R.id.progressBarDesafioDetalleRetado)

        usuarioActual = intent.getSerializableExtra("usuario") as Usuario
        desafioActual = intent.getSerializableExtra("desafio") as Desafio

        nombreDesafio!!.text = desafioActual.nombre
        objetivoDesafio!!.text = desafioActual.objetivo
        nombreRetador!!.text = desafioActual.retadorNombre
        nombreRetado!!.text = desafioActual.retadoNombre

        //------------------------------------
        val decodedString = Base64.decode(desafioActual.retadorFoto, Base64.DEFAULT)

        val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        Bitmap.createScaledBitmap(imagenJug, 40, 40, false)

        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
        roundedBitmapDrawable.isCircular = true

        fotoRetador!!.setImageDrawable(roundedBitmapDrawable)
        //--------------------------------------

        val decodedString2 = Base64.decode(desafioActual.retadoFoto, Base64.DEFAULT)

        val imagenJug2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.size)

        Bitmap.createScaledBitmap(imagenJug2, 40, 40, false)

        val roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug2)
        roundedBitmapDrawable2.isCircular = true

        fotoRetado!!.setImageDrawable(roundedBitmapDrawable2)
        //--------------------------------------

        if(desafioActual.retadorNombre.equals(usuarioActual.usuario)){
            botonProgresoRetado!!.visibility = View.INVISIBLE
            botonProgresoRetador!!.visibility = View.VISIBLE
        }else{
            botonProgresoRetador!!.visibility = View.INVISIBLE
            botonProgresoRetado!!.visibility = View.VISIBLE
        }

        when(desafioActual.tipo){
            "Dieta" -> fotoDesafioTipoArriba?.setImageResource(R.drawable.icono_arriba_dieta)
            "Ejercicio" -> fotoDesafioTipoArriba?.setImageResource(R.drawable.icono_arriba_ejercicio)
            "Libros" -> fotoDesafioTipoArriba?.setImageResource(R.drawable.icono_arriba_libros)
            "Amigos" -> fotoDesafioTipoArriba?.setImageResource(R.drawable.icono_arriba_amigos)
            "Viajes" -> fotoDesafioTipoArriba?.setImageResource(R.drawable.icono_arriba_viajar)
        }

        when(desafioActual.tipo){
            "Dieta" -> fotoDesafioTipoAbajo?.setImageResource(R.drawable.icono_abajo_dieta)
            "Ejercicio" -> fotoDesafioTipoAbajo?.setImageResource(R.drawable.icono_abajo_ejercicio)
            "Libros" -> fotoDesafioTipoAbajo?.setImageResource(R.drawable.icono_abajo_libros)
            "Amigos" -> fotoDesafioTipoAbajo?.setImageResource(R.drawable.icono_abajo_amigos)
            "Viajes" -> fotoDesafioTipoAbajo?.setImageResource(R.drawable.icono_abajo_viajar)
        }

        barraProgresoRetador!!.progress = desafioActual.progresoRetador
        barraProgresoRetado!!.progress = desafioActual.progresoRetado

        var repeticionesRetador: Int = desafioActual.progresoRetador/(100/desafioActual.repeticiones)
        var repeticionesRetado: Int = desafioActual.progresoRetado/(100/desafioActual.repeticiones)

        when(desafioActual.tipo){
            "Dieta" -> progresoRetador!!.text = repeticionesRetador.toString()+" de "+desafioActual.repeticiones+" kilos"
            "Ejercicio" -> progresoRetador!!.text = repeticionesRetador.toString()+" de "+desafioActual.repeticiones+" rutinas"
            "Libros" -> progresoRetador!!.text = repeticionesRetador.toString()+" de "+desafioActual.repeticiones+" libros"
            "Amigos" -> progresoRetador!!.text = repeticionesRetador.toString()+" de "+desafioActual.repeticiones+" contactos"
            "Viajes" -> progresoRetador!!.text = repeticionesRetador.toString()+" de "+desafioActual.repeticiones+" viajes"
        }

        when(desafioActual.tipo){
            "Dieta" -> progresoRetado!!.text = repeticionesRetado.toString()+" de "+desafioActual.repeticiones+" kilos"
            "Ejercicio" -> progresoRetado!!.text = repeticionesRetado.toString()+" de "+desafioActual.repeticiones+" rutinas"
            "Libros" -> progresoRetado!!.text = repeticionesRetado.toString()+" de "+desafioActual.repeticiones+" libros"
            "Amigos" -> progresoRetado!!.text = repeticionesRetado.toString()+" de "+desafioActual.repeticiones+" contactos"
            "Viajes" -> progresoRetado!!.text = repeticionesRetado.toString()+" de "+desafioActual.repeticiones+" viajes"
        }


        ref = FirebaseDatabase.getInstance().getReference("Desafios")

    }

    fun aumentarProgresoRetador(v: View){
        if(!desafioActual.terminado) {
            desafioActual.progresoRetador = desafioActual.progresoRetador + (100 / desafioActual.repeticiones)
            barraProgresoRetador!!.progress = desafioActual.progresoRetador
            var repeticionesRetador: Int = desafioActual.progresoRetador / (100 / desafioActual.repeticiones)



            when (desafioActual.tipo) {
                "Dieta" -> progresoRetador!!.text = repeticionesRetador.toString() + " de " + desafioActual.repeticiones + " kilos"
                "Ejercicio" -> progresoRetador!!.text = repeticionesRetador.toString() + " de " + desafioActual.repeticiones + " rutinas"
                "Libros" -> progresoRetador!!.text = repeticionesRetador.toString() + " de " + desafioActual.repeticiones + " libros"
                "Amigos" -> progresoRetador!!.text = repeticionesRetador.toString() + " de " + desafioActual.repeticiones + " contactos"
                "Viajes" -> progresoRetador!!.text = repeticionesRetador.toString() + " de " + desafioActual.repeticiones + " viajes"
            }

            var desafio: Desafio? = null

            if (repeticionesRetador != desafioActual.repeticiones) {
                desafio = Desafio(desafioActual.idDesafio, desafioActual.nombre, desafioActual.objetivo, desafioActual.tipo, desafioActual.repeticiones, desafioActual.progresoRetador, desafioActual.progresoRetado, desafioActual.retadorNombre, desafioActual.retadoNombre, desafioActual.retadorFoto, desafioActual.retadoFoto, false)
            } else {
                desafio = Desafio(desafioActual.idDesafio, desafioActual.nombre, desafioActual.objetivo, desafioActual.tipo, desafioActual.repeticiones, desafioActual.progresoRetador, desafioActual.progresoRetado, desafioActual.retadorNombre, desafioActual.retadoNombre, desafioActual.retadorFoto, desafioActual.retadoFoto, true)
                desafioActual=desafio
            }

            ref.child(desafioActual.idDesafio).setValue(desafio).addOnCompleteListener {
                Toast.makeText(this, "Desafio guardado satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "El desafio ya ha terminado", Toast.LENGTH_SHORT).show()
        }

    }

    fun aumentarProgresoRetado(v: View){
        if(!desafioActual.terminado) {
            desafioActual.progresoRetado = desafioActual.progresoRetado + (100 / desafioActual.repeticiones)
            barraProgresoRetado!!.progress = desafioActual.progresoRetado
            var repeticionesRetado: Int = desafioActual.progresoRetado / (100 / desafioActual.repeticiones)



            when (desafioActual.tipo) {
                "Dieta" -> progresoRetado!!.text = repeticionesRetado.toString() + " de " + desafioActual.repeticiones + " kilos"
                "Ejercicio" -> progresoRetado!!.text = repeticionesRetado.toString() + " de " + desafioActual.repeticiones + " rutinas"
                "Libros" -> progresoRetado!!.text = repeticionesRetado.toString() + " de " + desafioActual.repeticiones + " libros"
                "Amigos" -> progresoRetado!!.text = repeticionesRetado.toString() + " de " + desafioActual.repeticiones + " contactos"
                "Viajes" -> progresoRetado!!.text = repeticionesRetado.toString() + " de " + desafioActual.repeticiones + " viajes"
            }

            var desafio: Desafio? = null

            if (repeticionesRetado != desafioActual.repeticiones) {
                desafio = Desafio(desafioActual.idDesafio, desafioActual.nombre, desafioActual.objetivo, desafioActual.tipo, desafioActual.repeticiones, desafioActual.progresoRetador, desafioActual.progresoRetado, desafioActual.retadorNombre, desafioActual.retadoNombre, desafioActual.retadorFoto, desafioActual.retadoFoto, false)
            } else {
                desafio = Desafio(desafioActual.idDesafio, desafioActual.nombre, desafioActual.objetivo, desafioActual.tipo, desafioActual.repeticiones, desafioActual.progresoRetador, desafioActual.progresoRetado, desafioActual.retadorNombre, desafioActual.retadoNombre, desafioActual.retadorFoto, desafioActual.retadoFoto, true)
                desafioActual=desafio
            }
            ref.child(desafioActual.idDesafio).setValue(desafio).addOnCompleteListener {
                Toast.makeText(this, "Desafio guardado satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "El desafio ya ha terminado", Toast.LENGTH_SHORT).show()
        }

    }

}
