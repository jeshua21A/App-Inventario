package com.example.appinventario.data.remote.mapper

import com.example.appinventario.data.local.entities.RecetaEntity
import com.example.appinventario.data.remote.dto.RecetaDto

fun RecetaDto.toEntity(): RecetaEntity = RecetaEntity(
    id = this.id ?: 0,
    idLlavero = this.idLlavero,
    idMaterial = this.idMaterial,
    cantidad = this.cantidad
)

fun RecetaEntity.toDto(): RecetaDto = RecetaDto(
    id = if (this.id == 0) null else this.id,
    idLlavero = this.idLlavero,
    idMaterial = this.idMaterial,
    cantidad = this.cantidad
)