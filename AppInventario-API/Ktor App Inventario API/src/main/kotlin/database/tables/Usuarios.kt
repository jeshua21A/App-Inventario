package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Usuarios : Table("usuario") {
    val id = integer("id")
    val nombre = varchar("nombre", 100)
    val psswrd = varchar("psswrd", 50)
    val esAdmin = bool("esadmin")

    override val primaryKey = PrimaryKey(id)
}