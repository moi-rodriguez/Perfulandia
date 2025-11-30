/*package com.example.perfulandia.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.perfulandia.data.remote.ApiService
import com.example.perfulandia.data.remote.RetrofitClient
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Before

/**
 * Pruebas unitarias para AuthRepository
 *
 * Cubre:
 * - Login exitoso
 * - Login con credenciales inválidas
 * - Registro exitoso
 * - Registro con email duplicado
 * - Creación de productor
 * - Obtener todos los usuarios
 */
class AuthRepositoryTest {

    // Mocks
    private lateinit var mockContext: Context
    private lateinit var mockApiService: ApiService
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    // SUT (System Under Test)
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        // Crear mocks
        mockContext = mockk(relaxed = true)
        mockApiService = mockk()
        mockSharedPreferences = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        // Configurar comportamiento de SharedPreferences
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.apply() } just Runs
        every { mockEditor.clear() } returns mockEditor

        // Mockear RetrofitClient para que use nuestro mock
        mockkObject(RetrofitClient)
        every { RetrofitClient.authApiService} returns mockApiService

        // crear instancia del repository
        repository = UserRepository(mockContext)
    }
}*/