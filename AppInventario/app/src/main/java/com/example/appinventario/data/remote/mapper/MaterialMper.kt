package com.example.appinventario.data.remote.mapper

import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.remote.dto.MaterialDto

fun MaterialDto.toEntity(): MaterialEntity = MaterialEntity(
    id = this.id ?: 0,
    nombre = this.nombre,
    stockActual = this.stockActual,
    unidadMedida = this.unidadMedida,
    stockMinimo = this.stockMinimo,
    precioPorUnidad = this.precioPorUnidad
)

fun MaterialEntity.toDto(): MaterialDto = MaterialDto(
    id = if (this.id == 0) null else this.id,
    nombre = this.nombre,
    stockActual = this.stockActual,
    unidadMedida = this.unidadMedida,
    stockMinimo = this.stockMinimo,
    precioPorUnidad = this.precioPorUnidad
)