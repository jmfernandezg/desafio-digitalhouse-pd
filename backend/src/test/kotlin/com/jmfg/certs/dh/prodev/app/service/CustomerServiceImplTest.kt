package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Pruebas unitarias para CustomerServiceImpl
 *
 * Esta clase contiene pruebas exhaustivas para validar el comportamiento
 * del servicio de gestation de clientes, incluyendo:
 *
 * - Authentication y login de usuarios
 * - Creation de nuevos clientes con validaciones
 * - Actualization de datos de clientes
 * - Elimination de clientes
 * - Listado de clientes
 */
class CustomerServiceImplTest {
 private lateinit var customerService: CustomerServiceImpl
 private lateinit var customerRepository: CustomerRepository
 private lateinit var jwtEncoder: JwtEncoder
 private lateinit var passwordEncoder: PasswordEncoder

 private val testCustomer = Customer(
  id = "1",
  username = "testuser",
  password = "hashedPassword",
  email = "test@example.com",
  firstName = "Test",
  lastName = "User"
 )

 @BeforeEach
 fun setup() {
  customerRepository = mockk<CustomerRepository>()
  jwtEncoder = mockk<JwtEncoder>()
  passwordEncoder = mockk<PasswordEncoder>()
  customerService = CustomerServiceImpl(customerRepository, jwtEncoder, passwordEncoder)
 }

 /**
  * Prueba de login exitoso
  *
  * Verifica que:
  * - Se devuelve el cliente con token cuando las credenciales son válidas
  * - Se verifica la contrasena correctamente
  * - Se genera el token JWT
  */
 @Test
 fun `login deberia retornar cliente con token cuando las credenciales son validas`() {
  // Arrange
  val loginRequest = LoginRequest("testuser", "password123")
  val mockJwt = mockk<Jwt>()

  every { customerRepository.findByUsername("testuser") } returns testCustomer
  every { passwordEncoder.matches("password123", "hashedPassword") } returns true
  every { jwtEncoder.encode(any()) } returns mockJwt
  every { mockJwt.tokenValue } returns "jwt-token"

  // Act
  val result = customerService.login(loginRequest)

  // Assert
  assertNotNull(result)
  assertEquals("testuser", result?.username)
  verify {
   customerRepository.findByUsername("testuser")
   passwordEncoder.matches("password123", "hashedPassword")
   jwtEncoder.encode(any())
  }
 }

 /**
  * Prueba de login fallido
  *
  * Verifica que:
  * - Se retorna null cuando la contrasena es inválida
  * - No se genera token JWT para credenciales inválidas
  */
 @Test
 fun `login deberia retornar null cuando la contrasena es invalida`() {
  // Arrange
  val loginRequest = LoginRequest("testuser", "wrongpassword")

  every { customerRepository.findByUsername("testuser") } returns testCustomer
  every { passwordEncoder.matches("wrongpassword", "hashedPassword") } returns false

  // Act
  val result = customerService.login(loginRequest)

  // Assert
  assertNull(result)
  verify {
   customerRepository.findByUsername("testuser")
   passwordEncoder.matches("wrongpassword", "hashedPassword")
  }
  verify(exactly = 0) { jwtEncoder.encode(any()) }
 }

 /**
  * Prueba de creation exitosa de cliente
  *
  * Verifica que:
  * - Se guarda el cliente cuando los datos son válidos
  * - Se encripta la contrasena correctamente
  * - Se retorna el cliente creado
  */
 @Test
 fun `create deberia guardar nuevo cliente cuando los datos son validos`() {
  // Arrange
  val request = CustomerCreationRequest(
   username = "newuser",
   password = "password123",
   email = "new@example.com",
   firstName = "New",
   lastName = "User"
  )

  every { customerRepository.findByUsername("newuser") } returns null
  every { passwordEncoder.encode("password123") } returns "hashedPassword"
  every { customerRepository.save(any()) } returns testCustomer

  // Act
  val result = customerService.create(request)

  // Assert
  assertNotNull(result)
  verify {
   customerRepository.findByUsername("newuser")
   passwordEncoder.encode("password123")
   customerRepository.save(any())
  }
 }

 /**
  * Prueba de validation de usuario duplicado
  *
  * Verifica que:
  * - Se lanza excepcion cuando el username ya existe
  * - No se intenta guardar el cliente duplicado
  */
 @Test
 fun `create deberia lanzar excepcion cuando el username ya existe`() {
  // Arrange
  val request = CustomerCreationRequest(
   username = "testuser",
   password = "password123",
   email = "test@example.com",
   firstName = "Test",
   lastName = "User"
  )

  every { customerRepository.findByUsername("testuser") } returns testCustomer

  // Act & Assert
  assertThrows<IllegalArgumentException> { customerService.create(request) }
  verify { customerRepository.findByUsername("testuser") }
  verify(exactly = 0) { customerRepository.save(any()) }
 }

 /**
  * Prueba de validation de contrasena débil
  *
  * Verifica que:
  * - Se lanza excepcion cuando la contrasena es muy corta
  * - No se intenta guardar el cliente con contrasena inválida
  */
 @Test
 fun `create deberia lanzar excepcion cuando la contrasena es muy corta`() {
  // Arrange
  val request = CustomerCreationRequest(
   username = "newuser",
   password = "short",
   email = "test@example.com",
   firstName = "Test",
   lastName = "User"
  )

  every { customerRepository.findByUsername("newuser") } returns null

  // Act & Assert
  assertThrows<IllegalArgumentException> { customerService.create(request) }
  verify { customerRepository.findByUsername("newuser") }
  verify(exactly = 0) { customerRepository.save(any()) }
 }

 /**
  * Prueba de validation de formato de email
  *
  * Verifica que:
  * - Se lanza excepcion cuando el formato del email es inválido
  * - No se intenta guardar el cliente con email inválido
  */
 @Test
 fun `create deberia lanzar excepcion cuando el formato del email es invalido`() {
  // Arrange
  val request = CustomerCreationRequest(
   username = "newuser",
   password = "password123",
   email = "invalid-email",
   firstName = "Test",
   lastName = "User"
  )

  every { customerRepository.findByUsername("newuser") } returns null

  // Act & Assert
  assertThrows<IllegalArgumentException> { customerService.create(request) }
  verify { customerRepository.findByUsername("newuser") }
  verify(exactly = 0) { customerRepository.save(any()) }
 }

 /**
  * Prueba de elimination exitosa
  *
  * Verifica que:
  * - Se elimina el cliente cuando existe
  * - Se verifica la existencia antes de eliminar
  */
 @Test
 fun `delete deberia eliminar cliente existente`() {
  // Arrange
  val customerId = "1"
  every { customerRepository.existsById(customerId) } returns true
  every { customerRepository.deleteById(customerId) } just runs

  // Act
  customerService.delete(customerId)

  // Assert
  verify {
   customerRepository.existsById(customerId)
   customerRepository.deleteById(customerId)
  }
 }

 /**
  * Prueba de elimination de cliente inexistente
  *
  * Verifica que:
  * - Se lanza excepcion cuando el cliente no existe
  * - No se intenta eliminar un cliente inexistente
  */
 @Test
 fun `delete deberia lanzar excepcion cuando el cliente no existe`() {
  // Arrange
  val customerId = "999"
  every { customerRepository.existsById(customerId) } returns false

  // Act & Assert
  assertThrows<NoSuchElementException> { customerService.delete(customerId) }
  verify { customerRepository.existsById(customerId) }
  verify(exactly = 0) { customerRepository.deleteById(any()) }
 }

 /**
  * Prueba de actualization exitosa
  *
  * Verifica que:
  * - Se actualiza el cliente cuando existe
  * - Se retorna el cliente actualizado
  */
 @Test
 fun `update deberia modificar cliente existente`() {
  // Arrange
  val updatedCustomer = testCustomer.copy(firstName = "Updated")
  every { customerRepository.existsById(updatedCustomer.id) } returns true
  every { customerRepository.save(updatedCustomer) } returns updatedCustomer

  // Act
  val result = customerService.update(updatedCustomer)

  // Assert
  assertNotNull(result)
  assertEquals("Updated", result.firstName)
  verify {
   customerRepository.existsById(updatedCustomer.id)
   customerRepository.save(updatedCustomer)
  }
 }

 /**
  * Prueba de actualization de cliente inexistente
  *
  * Verifica que:
  * - Se lanza excepcion cuando el cliente no existe
  * - No se intenta actualizar un cliente inexistente
  */
 @Test
 fun `update deberia lanzar excepcion cuando el cliente no existe`() {
  // Arrange
  val nonExistentCustomer = testCustomer.copy(id = "999")
  every { customerRepository.existsById(nonExistentCustomer.id) } returns false

  // Act & Assert
  assertThrows<NoSuchElementException> { customerService.update(nonExistentCustomer) }
  verify { customerRepository.existsById(nonExistentCustomer.id) }
  verify(exactly = 0) { customerRepository.save(any()) }
 }

 /**
  * Prueba de listado de clientes
  *
  * Verifica que:
  * - Se retorna la lista completa de clientes
  * - Los datos de los clientes son correctos
  */
 @Test
 fun `findAll deberia retornar lista de todos los clientes`() {
  // Arrange
  every { customerRepository.findAll() } returns listOf(testCustomer)

  // Act
  val result = customerService.findAll()

  // Assert
  assertNotNull(result)
  assertEquals(1, result.customers.size)
  assertEquals("testuser", result.customers[0].username)
  verify { customerRepository.findAll() }
 }
}