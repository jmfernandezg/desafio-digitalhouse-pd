package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.CustomerStatistics
import com.jmfg.certs.dh.prodev.service.CustomerService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para AdminController
 *
 * Verifica el correcto funcionamiento de las operaciones administrativas:
 * - Consulta de clientes
 * - Eliminación de clientes
 * - Estadísticas y reportes
 */
@DisplayName("Admin Controller Tests")
class AdminControllerTest {

 private lateinit var customerService: CustomerService
 private lateinit var adminController: AdminController

 @BeforeEach
 fun setup() {
  customerService = mockk()
  adminController = AdminController(customerService)
 }

 @Nested
 @DisplayName("Get All Customers Tests")
 inner class GetAllCustomersTests {

  private val mockCustomers = CustomerResponse(
   customers = listOf(
    CustomerResponse.CustomerItem(
     id = "1",
     email = "test1@example.com",
     firstName = "Test",
     lastName = "User"
    ),
    CustomerResponse.CustomerItem(
     id = "2",
     email = "test2@example.com",
     firstName = "Another",
     lastName = "User"
    )
   )
  )

  @Test
  @DisplayName("Lista todos los clientes exitosamente")
  fun `getAllCustomers returns all customers successfully`() = runBlocking {
   // Arrange
   coEvery { customerService.findAll() } returns mockCustomers

   // Act
   val response = adminController.getAllCustomers()

   // Assert
   assertEquals(HttpStatus.OK, response.statusCode)
   assertEquals(2, response.body?.customers?.size)

   // Verify
   coVerify(exactly = 1) { customerService.findAll() }
  }
 }

 @Nested
 @DisplayName("Delete Customer Tests")
 inner class DeleteCustomerTests {

  @Test
  @DisplayName("Elimina cliente exitosamente")
  fun `deleteCustomer removes customer successfully`() = runBlocking {
   // Arrange
   val customerId = 1L
   coEvery { customerService.delete(customerId) } returns Unit

   // Act
   adminController.deleteCustomer(customerId)

   // Verify
   coVerify(exactly = 1) { customerService.delete(customerId) }
  }

  @Test
  @DisplayName("Falla al eliminar cliente inexistente")
  fun `deleteCustomer throws exception when customer not found`() = runBlocking {
   // Arrange
   val customerId = 999L
   coEvery { customerService.delete(customerId) } throws NoSuchElementException("Customer not found")

   // Act & Assert
   val exception = assertFailsWith<ResponseStatusException> {
    adminController.deleteCustomer(customerId)
   }
   assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
  }
 }

 @Nested
 @DisplayName("Get Customers By Country Tests")
 inner class GetCustomersByCountryTests {

  private val mockCountryCustomers = CustomerResponse(
   customers = listOf(
    CustomerResponse.CustomerItem(
     id = "1",
     email = "spain@example.com",
     firstName = "Spanish",
     lastName = "User"
    )
   )
  )

  @Test
  @DisplayName("Encuentra clientes por país exitosamente")
  fun `getCustomersByCountry returns customers for specific country`() = runBlocking {
   // Arrange
   val country = "Spain"
   coEvery { customerService.findByCountry(country) } returns mockCountryCustomers

   // Act
   val response = adminController.getCustomersByCountry(country)

   // Assert
   assertEquals(HttpStatus.OK, response.statusCode)
   assertEquals(1, response.body?.customers?.size)

   // Verify
   coVerify(exactly = 1) { customerService.findByCountry(country) }
  }

  @Test
  @DisplayName("Retorna sin contenido cuando no hay clientes en el país")
  fun `getCustomersByCountry returns no content when country has no customers`() = runBlocking {
   // Arrange
   val country = "EmptyCountry"
   coEvery { customerService.findByCountry(country) } returns CustomerResponse(emptyList())

   // Act
   val response = adminController.getCustomersByCountry(country)

   // Assert
   assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  }
 }

 @Nested
 @DisplayName("Get Customers With Expiring Passports Tests")
 inner class GetCustomersWithExpiringPassportsTests {

  private val mockExpiringPassports = CustomerResponse(
   customers = listOf(
    CustomerResponse.CustomerItem(
     id = "1",
     email = "expiring@example.com",
     firstName = "Expiring",
     lastName = "Passport"
    )
   )
  )

  @Test
  @DisplayName("Encuentra pasaportes por vencer exitosamente")
  fun `getCustomersWithExpiringPassports returns customers successfully`() = runBlocking {
   // Arrange
   val beforeDate = LocalDate.now().plusMonths(6)
   coEvery { customerService.findByPassportExpiryBefore(beforeDate) } returns mockExpiringPassports

   // Act
   val response = adminController.getCustomersWithExpiringPassports(beforeDate)

   // Assert
   assertEquals(HttpStatus.OK, response.statusCode)
   assertTrue(response.body?.customers?.isNotEmpty() == true)
  }

  @Test
  @DisplayName("Retorna sin contenido cuando no hay pasaportes por vencer")
  fun `getCustomersWithExpiringPassports returns no content when no expiring passports`() = runBlocking {
   // Arrange
   val beforeDate = LocalDate.now().plusMonths(6)
   coEvery { customerService.findByPassportExpiryBefore(beforeDate) } returns CustomerResponse(emptyList())

   // Act
   val response = adminController.getCustomersWithExpiringPassports(beforeDate)

   // Assert
   assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  }
 }

 @Nested
 @DisplayName("Get Customer Statistics Tests")
 inner class GetCustomerStatisticsTests {

  private val mockStatistics = CustomerStatistics(
   totalCustomers = 100,
   customersByCountry = mapOf("Spain" to 50, "France" to 30, "Germany" to 20),
   averageAge = 35.5,
   expiringPassportsCount = 5,
   customersWithoutPassport = 10
  )

  @Test
  @DisplayName("Obtiene estadísticas exitosamente")
  fun `getCustomerStatistics returns statistics successfully`() = runBlocking {
   // Arrange
   coEvery { customerService.getStatistics() } returns mockStatistics

   // Act
   val response = adminController.getCustomerStatistics()

   // Assert
   assertEquals(HttpStatus.OK, response.statusCode)
   assertEquals(100, response.body?.totalCustomers)
   assertEquals(35.5, response.body?.averageAge)
   assertEquals(3, response.body?.customersByCountry?.size)

   // Verify
   coVerify(exactly = 1) { customerService.getStatistics() }
  }
 }
}