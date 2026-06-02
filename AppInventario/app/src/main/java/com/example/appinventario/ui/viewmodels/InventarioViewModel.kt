package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.local.entities.RecetaEntity
import com.example.appinventario.data.local.entities.UsuarioEntity
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.remote.mapper.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Estado de autenticación
sealed class AuthState {
    object Idle : AuthState()
    data class Autenticado(val usuario: UsuarioEntity) : AuthState()
    object CredencialesInvalidas : AuthState()
}

class InventarioViewModel(
    private val inventarioDao: InventarioDao,
    private val apiService: InventarioApiService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // 1. Observer todos los materiales para confirmar cuando habra algun cambio
    val listaMateriales: StateFlow<List<MaterialEntity>> = inventarioDao.getAllMateriales()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Funcion para insertar un nuevo material
    fun agregarMaterial(nombre: String, stock: Double, unidad: String, minimo: Double, precio: Double){
        viewModelScope.launch {
            val nuevo = MaterialEntity(
                nombre = nombre,
                stockActual = stock,
                unidadMedida = unidad,
                stockMinimo = minimo,
                precioPorUnidad = precio
            )
            inventarioDao.insertMaterial(nuevo)
        }
    }

    // 3. Obtener materiales con stock bajo
    val materialesEnEscasez: StateFlow<List<MaterialEntity>> = inventarioDao.getMaterialesEnEscasez()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 4. Actualizar un material existente
    fun actualizarMaterial(material: MaterialEntity) {
        // TODO: Implementar actualización en Room y Supabase
    }

    // 5. Eliminar un material
    fun eliminarMaterial(material: MaterialEntity) {
        // TODO: Implementar eliminación en Room y Supabase
    }

    // 6. Obtener todos los llaveros (catalogo)
    val listaLlaveros: StateFlow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 7. Agregar un nuevo llavero
    fun agregarLlavero(nombre: String, descripcion: String, precioVenta: Double) {
        // TODO: Implementar creación en Room y Supabase
    }

    // 8. Actualizar un llavero existente
    fun actualizarLlavero(llavero: LlaveroEntity) {
        // TODO: Implementar actualización en Room y Supabase
    }

    // 9. Eliminar un llavero
    fun eliminarLlavero(llavero: LlaveroEntity) {
        // TODO: Implementar eliminación en Room y Supabase
    }

    // 10. Autenticacion de usuario (login)
    fun login(user: String, password: String) {
        // TODO: Implementar autenticación con Supabase
    }

    // 11. Cerrar sesion
    fun cerrarSesion() {
        // TODO: Implementar cierre de sesión
    }

    // 12. Obtener los materiales que necesita un llavero (receta)
    fun getMaterialesDeUnLlavero(llaveroId: Int) = flow {
        emit(emptyList<MaterialEntity>())
        // TODO: Implementar consulta de recetas
    }

    // 13. Agregar un ingrediente a la receta de un llavero
    fun agregarIngredienteReceta(llaveroId: Int, materialId: Int, cantidad: Double) {
        // TODO: Implementar creación de recetas
    }

    // 14. Eliminar un ingrediente de la receta de un llavero
    fun eliminarIngredienteReceta(receta: RecetaEntity) {
        // TODO: Implementar eliminación de recetas
    }

    // 15. Verificar si hay suficiente stock para producir un llavero
    fun verificarStockParaLlavero(llaveroId: Int): Boolean {
        return TODO("Provide the return value")
        // TODO: Implementar verificación de stock
    }

    // 16. Producir un llavero (descontar stock de materiales)
    fun producirLlavero(llaveroId: Int) {
        // TODO: Implementar producción y descuento de stock
    }
}