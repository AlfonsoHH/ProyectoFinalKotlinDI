package com.example.alfonsohh.proyectofinalkotlindi

/**
 * Created by AlfonsoHH on 18/02/2018.
 */
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.database.DatabaseReference

class AdaptadorDesafio(val desafioList: MutableList<Desafio>, val activity: Activity, var ref: DatabaseReference, var usuarioActual: Usuario): RecyclerView.Adapter<AdaptadorDesafio.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.bindItems(desafioList[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_desafio, parent, false)
        return AdaptadorDesafio.ViewHolder(v, activity, ref, usuarioActual);
    }

    override fun getItemCount(): Int {
        return desafioList.size
    }

    class ViewHolder(itemView: View, val activity: Activity, var ref: DatabaseReference,var usuarioActual: Usuario): RecyclerView.ViewHolder(itemView){


        fun bindItems(desafio: Desafio){

            val txtNameReto = itemView.findViewById<TextView>(R.id.textNameDesafioReto)
            val txtNameRetador = itemView.findViewById<TextView>(R.id.txtNameRetador)
            val txtNameRetado = itemView.findViewById<TextView>(R.id.txtNameRetado)
            val fotoRetador = itemView.findViewById<ImageView>(R.id.fotoDesafioRetador)
            val fotoRetado = itemView.findViewById<ImageView>(R.id.fotoDesafioRetado)
            val progresoRetador = itemView.findViewById<ProgressBar>(R.id.progressBarRetador)
            val progresoRetado = itemView.findViewById<ProgressBar>(R.id.progressBarRetado)
            val imagenTipo = itemView.findViewById<ImageView>(R.id.fotoDesafioTipo)

            txtNameReto?.text = desafio.nombre
            txtNameRetador?.text = desafio.retadorNombre
            txtNameRetado?.text = desafio.retadoNombre

            //------------------------------------
            if (!desafio.retadorFoto.equals("url")) {

                val decodedString = Base64.decode(desafio.retadorFoto, Base64.DEFAULT)

                val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                Bitmap.createScaledBitmap(imagenJug, 30, 30, false)


                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), imagenJug)
                roundedBitmapDrawable.isCircular = true

                fotoRetador?.setImageDrawable(roundedBitmapDrawable)

            } else {
                fotoRetador?.setImageResource(R.drawable.icono_interrogacion)
            }
            //--------------------------------------

            //------------------------------------
            if (!desafio.retadoFoto.equals("url")) {

                val decodedString = Base64.decode(desafio.retadoFoto, Base64.DEFAULT)

                val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                Bitmap.createScaledBitmap(imagenJug, 30, 30, false)

                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), imagenJug)
                roundedBitmapDrawable.isCircular = true

                fotoRetado?.setImageDrawable(roundedBitmapDrawable)

            } else {
                fotoRetado?.setImageResource(R.drawable.icono_interrogacion)
            }
            //--------------------------------------

            progresoRetador?.setProgress(desafio.progresoRetador)
            progresoRetado?.setProgress(desafio.progresoRetado)
            when(desafio.tipo){
                "Dieta" -> imagenTipo?.setImageResource(R.drawable.icono_mitad_dieta)
                "Ejercicio" -> imagenTipo?.setImageResource(R.drawable.icono_mitad_ejercicio)
                "Libros" -> imagenTipo?.setImageResource(R.drawable.icono_mitad_libros)
                "Amigos" -> imagenTipo?.setImageResource(R.drawable.icono_mitad_amigos)
                "Viajes" -> imagenTipo?.setImageResource(R.drawable.icono_mitad_viajes)
            }

            itemView.setOnClickListener({
                var intent: Intent = Intent(activity,DesafioDetalleActivity::class.java)
                intent.putExtra("usuario",usuarioActual)
                intent.putExtra("desafio",desafio)
                activity.startActivity(intent)

            })

            itemView.setOnLongClickListener({
                val alertDilog = AlertDialog.Builder(activity).create()
                alertDilog.setTitle("Alert")
                alertDilog.setMessage("¿Estas seguro de abandonar este desafio?")

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "Si", {
                    dialogInterface, i ->  ref.child(desafio.idDesafio).removeValue().addOnCompleteListener{
                }

                })

                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", {
                    dialogInterface, i ->
                })
                alertDilog.show()
                return@setOnLongClickListener true
            })

        }
    }

}