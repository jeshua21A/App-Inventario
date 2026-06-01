package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

// Conexion a la Base de Datos
/*
jdbc:postgresql://aws-1-us-west-1.pooler.supabase.com:6543/postgres?user=postgres.aytckcllbulgfvkavhzt&password=[YOUR-PASSWORD]

host:aws-1-us-west-1.pooler.supabase.com
port:6543
database:postgres
user:postgres.aytckcllbulgfvkavhzt
*/
/**
 *
 */
object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://aws-1-us-west-1.pooler.supabase.com:6543/postgres?user=postgres.aytckcllbulgfvkavhzt&password=kQte_tV8-c.dvvK";
            username = "postgres.aytckcllbulgfvkavhzt";
            password = "kQte_tV8-c.dvvK";
            driverClassName = "org.postgresql.Driver";
            maximumPoolSize = 5
        }
        Database.connect(HikariDataSource(config))
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }