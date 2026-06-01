package com.example.appinventario.data.remote.mapper

import com.example.appinventario.data.local.entities.UsuarioEntity
import com.example.appinventario.data.remote.dto.UsuarioDto

fun UsuarioDto.toEntity(): UsuarioEntity = UsuarioEntity(
    id = this.id ?: 0,
    username = this.username,
    password = this.password,
    esAdmin = this.esAdmin
)

fun UsuarioEntity.toDto(): UsuarioDto = UsuarioDto(
    id = if (this.id == 0) null else this.id,
    username = this.username,
    password = this.password,
    esAdmin = this.esAdmin
)