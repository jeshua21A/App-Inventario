package com.example.appinventario.data.remote.mapper

import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.remote.dto.LlaveroDto

fun LlaveroDto.toEntity(): LlaveroEntity = LlaveroEntity(
    id = this.id ?: 0,
    nombre = this.nombre,
    descripcion = this.descripcion,
    precioVenta = this.precioVenta
)

fun LlaveroEntity.toDto(): LlaveroDto = LlaveroDto(
    id = if (this.id == 0) null else this.id,
    nombre = this.nombre,
    descripcion = this.descripcion,
    precioVenta = this.precioVenta
)