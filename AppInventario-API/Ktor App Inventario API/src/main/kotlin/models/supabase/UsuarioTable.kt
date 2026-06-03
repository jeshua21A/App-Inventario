package com.example.models.supabase

import org.jetbrains.exposed.sql.Table

object UsuarioTable : Table("usuario"){
    val id = integer("id").autoIncrement()
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val esAdmin = bool("es_admin")

    override val primaryKey = PrimaryKey(id)
}