package com.example.alfonsohh.proyectofinalkotlindi

import java.io.Serializable
import java.util.*

/**
 * Created by AlfonsoHH on 18/02/2018.
 */
data class Reto(val idReto: String, val nombre: String, val objetivo: String, val fecha: String, val tipo: String, val repeticiones: Int, var progreso: Int) : Serializable {
    constructor() : this ("","","","","",0,0){

    }
}

