package com.example.alfonsohh.proyectofinalkotlindi

import java.io.Serializable

/**
 * Created by AlfonsoHH on 21/02/2018.
 */
data class Usuario(val idUsuario: String, val usuario: String, val password: String, val foto: String) : Serializable {
    constructor() : this ("","","",""){

    }
}