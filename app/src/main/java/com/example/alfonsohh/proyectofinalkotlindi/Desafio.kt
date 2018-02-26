package com.example.alfonsohh.proyectofinalkotlindi

import java.io.Serializable
import java.util.*

/**
 * Created by AlfonsoHH on 18/02/2018.
 */
data class Desafio(val idDesafio: String, val nombre: String, val objetivo: String, val tipo: String, val repeticiones: Int, var progresoRetador: Int, var progresoRetado: Int, val retadorNombre: String, val retadoNombre: String, val retadorFoto: String, val retadoFoto: String, val terminado: Boolean) : Serializable{
    constructor() : this ("","","","",0,0,0,"","","","",false){

    }
}