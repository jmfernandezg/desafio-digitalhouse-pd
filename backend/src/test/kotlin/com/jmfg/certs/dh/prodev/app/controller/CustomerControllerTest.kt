package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import com.jmfg.certs.dh.prodev.service.CustomerService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Pruebas unitarias para CustomerController
 *
 * Esta clase contiene pruebas exhaustivas para validar el comportamiento
 * del controlador REST de clientes, incluyendo:
 *
 * - Autenticación y generación de tokens
 * - Gestión de errores HTTP
 * - Operaciones CRUD de clientes
 * - Validación de respuestas HTTP
 */
class CustomerControllerTest {
    private lateinit var customerController: CustomerController
    private lateinit var customerService: CustomerService

    private val testCustomer = CustomerResponse.CustomerItem(
        id = "1",
        username = "testuser",
        email = "test@example.com",
        firstName = "Test",
        lastName = "User",
        token = "test-jwt-token"
    )

    @BeforeEach
    fun setup() {
        customerService = mockk()
        customerController = CustomerController(customerService)
    }

    /**
     * Pruebas de login
     *
     * Verifica:
     * - Login exitoso con token
     * - Login fallido por credenciales inválidas
     * - Login fallido por usuario no encontrado
     */
    @Test
    fun `login debería retornar token cuando las credenciales son válidas`() {
        // Arrange
        val loginRequest = LoginRequest("testuser", "password123")
        every { customerService.login(loginRequest) } returns testCustomer

        // Act
        val response = customerController.login(loginRequest)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("test-jwt-token", response.body?.authToken)
        assertNull(response.body?.error)
        verify { customerService.login(loginRequest) }
    }

    @Test
    fun `login debería retornar 401 cuando las credenciales son inválidas`() {
        // Arrange
        val loginRequest = LoginRequest("testuser", "wrongpass")
        every { customerService.login(loginRequest) } returns testCustomer.copy(token = null)

        // Act
        val response = customerController.login(loginRequest)

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertNotNull(response.body?.error)
        verify { customerService.login(loginRequest) }
    }

    @Test
    fun `login debería retornar 404 cuando el usuario no existe`() {
        // Arrange
        val loginRequest = LoginRequest("nonexistent", "password123")
        every { customerService.login(loginRequest) } returns null

        // Act
        val response = customerController.login(loginRequest)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNotNull(response.body?.error)
        verify { customerService.login(loginRequest) }
    }

    /**
     * Pruebas de creación de cliente
     *
     * Verifica:
     * - Creación exitosa
     * - Manejo de cliente duplicado
     */
    @Test
    fun `create debería retornar 201 cuando el cliente es creado exitosamente`() {
        // Arrange
        val request = CustomerCreationRequest(
            username = "newuser",
            password = "password123",
            email = "new@example.com",
            firstName = "New",
            lastName = "User"
        )
        every { customerService.create(request) } returns testCustomer

        // Act
        val response = customerController.create(request)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        verify { customerService.create(request) }
    }

    @Test
    fun `create debería lanzar excepción cuando el cliente ya existe`() {
        // Arrange
        val request = CustomerCreationRequest(
            username = "existinguser",
            password = "password123",
            email = "existing@example.com",
            firstName = "Existing",
            lastName = "User"
        )
        every { customerService.create(request) } returns null

        // Act & Assert
        assertThrows(ResponseStatusException::class.java) {
            customerController.create(request)
        }
        verify { customerService.create(request) }
    }

    /**
     * Pruebas de eliminación de cliente
     *
     * Verifica:
     * - Eliminación exitosa
     * - Manejo de cliente no encontrado
     */
    @Test
    fun `delete debería retornar 204 cuando el cliente es eliminado exitosamente`() {
        // Arrange
        val customerId = "1"
        every { customerService.delete(customerId) } returns Unit

        // Act
        val response = customerController.delete(customerId)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify { customerService.delete(customerId) }
    }

    /**
     * Pruebas de actualización de cliente
     *
     * Verifica:
     * - Actualización exitosa
     * - Validación de ID coincidente
     * - Manejo de cliente no encontrado
     */
    @Test
    fun `update debería retornar 200 cuando el cliente es actualizado exitosamente`() {
        // Arrange
        val customer = Customer(
            id = "1",
            username = "testuser",
            password = "password123",
            email = "test@example.com",
            firstName = "Updated",
            lastName = "User"
        )
        every { customerService.update(customer) } returns testCustomer.copy(firstName = "Updated")

        // Act
        val response = customerController.update("1", customer)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Updated", response.body?.firstName)
        verify { customerService.update(customer) }
    }

    @Test
    fun `update debería retornar 400 cuando el ID no coincide`() {
        // Arrange
        val customer = Customer(
            id = "1",
            username = "testuser",
            password = "password123",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )

        // Act
        val response = customerController.update("2", customer)

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(exactly = 0) { customerService.update(any()) }
    }

    @Test
    fun `update debería retornar 404 cuando el cliente no existe`() {
        // Arrange
        val customer = Customer(
            id = "999",
            username = "testuser",
            password = "password123",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )
        every { customerService.update(customer) } returns null

        // Act
        val response = customerController.update("999", customer)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify { customerService.update(customer) }
    }

    /**
     * Prueba de listado de clientes
     *
     * Verifica:
     * - Obtención exitosa de lista de clientes
     * - Formato correcto de respuesta
     */
    @Test
    fun `findAll debería retornar lista de clientes`() {
        // Arrange
        val customerResponse = CustomerResponse(listOf(testCustomer))
        every { customerService.findAll() } returns customerResponse

        // Act
        val response = customerController.findAll()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.customers?.size)
        verify { customerService.findAll() }
    }
}