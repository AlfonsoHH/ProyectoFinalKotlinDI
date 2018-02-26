package com.example.alfonsohh.proyectofinalkotlindi

/**
 * Created by AlfonsoHH on 18/02/2018.
 */
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.app.AlertDialog
import android.widget.*
import com.google.firebase.database.DatabaseReference

class AdaptadorReto(val retoList: MutableList<Reto>, val activity: Activity, var ref: DatabaseReference,var usuarioActual: Usuario): RecyclerView.Adapter<AdaptadorReto.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItems(retoList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_reto, parent, false)
        return ViewHolder(v, activity,ref,usuarioActual)
    }

    override fun getItemCount(): Int {
        return retoList.size
    }

    class ViewHolder(itemView: View, val activity: Activity, var ref: DatabaseReference,var usuarioActual: Usuario): RecyclerView.ViewHolder(itemView){


        fun bindItems(reto: Reto){

            val txtName = itemView.findViewById<TextView>(R.id.txtName)
            val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
            val foto = itemView.findViewById<ImageView>(R.id.fotoRv)
            val progreso = itemView.findViewById<ProgressBar>(R.id.progressBar)

            txtName?.text = reto.nombre
            txtTitle?.text = reto.objetivo
            progreso?.setProgress(reto.progreso)
            when(reto.tipo){
                "Dieta" -> foto?.setImageResource(R.drawable.icono_dieta)
                "Ejercicio" -> foto?.setImageResource(R.drawable.icono_ejercicio)
                "Libros" -> foto?.setImageResource(R.drawable.icono_libros)
                "Amigos" -> foto?.setImageResource(R.drawable.icono_amigos)
                "Viajes" -> foto?.setImageResource(R.drawable.icono_viajar)
            }

            itemView.setOnClickListener({
                var intent:Intent = Intent(activity,RetoDetalleActivity::class.java)
                intent.putExtra("usuario",usuarioActual)
                intent.putExtra("reto",reto)
                activity.startActivity(intent)

            })

            itemView.setOnLongClickListener({
                val alertDilog = AlertDialog.Builder(activity).create()
                alertDilog.setTitle("Alert")
                alertDilog.setMessage("¿Estas seguro de abandonar este reto?")

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "Si", {
                    dialogInterface, i ->  ref.child(reto.idReto).removeValue().addOnCompleteListener{
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