package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.dto.*
import com.jmfg.certs.dh.prodev.service.CustomerService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@DisplayName("CustomerController Tests")
class CustomerControllerTest {

    private lateinit var customerService: CustomerService
    private lateinit var customerController: CustomerController

    @BeforeEach
    fun setup() {
        customerService = mockk()
        customerController = CustomerController(customerService)
    }

    @Nested
    @DisplayName("Login Tests")
    inner class LoginTests {
        private val validLoginRequest = LoginRequest(
            email = "test@example.com",
            password = "password123"
        )

        private val validCustomerResponse = CustomerResponse.CustomerItem(
            id = "1",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User",
            token = "valid.jwt.token"
        )

        @Test
        @DisplayName("Successful login returns 200 OK with token")
        fun `login with valid credentials returns success response`() = runBlocking {
            // Arrange
            coEvery { customerService.login(validLoginRequest) } returns validCustomerResponse

            // Act
            val response = customerController.login(validLoginRequest)

            // Assert
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
            assertEquals("valid.jwt.token", response.body?.authToken)
            assertTrue(response.body?.isSuccessful == true)

            // Verify
            coVerify(exactly = 1) { customerService.login(validLoginRequest) }
        }

        @Test
        @DisplayName("Login with invalid credentials returns 401 Unauthorized")
        fun `login with invalid credentials returns unauthorized`() = runBlocking {
            // Arrange
            coEvery { customerService.login(validLoginRequest) } returns null

            // Act
            val response = customerController.login(validLoginRequest)

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
            assertNotNull(response.body)
            assertNull(response.body?.authToken)
            assertEquals("Credenciales inválidas", response.body?.error)

            // Verify
            coVerify(exactly = 1) { customerService.login(validLoginRequest) }
        }

        @Test
        @DisplayName("Login throwing exception returns 401 Unauthorized with error message")
        fun `login throwing exception returns error response`() = runBlocking {
            // Arrange
            coEvery { customerService.login(validLoginRequest) } throws IllegalArgumentException("Error de prueba")

            // Act
            val response = customerController.login(validLoginRequest)

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
            assertNotNull(response.body)
            assertNull(response.body?.authToken)
            assertEquals("Error de prueba", response.body?.error)
        }
    }

    @Nested
    @DisplayName("Customer Registration Tests")
    inner class RegistrationTests {
        private val validCustomerRequest = CustomerCreationRequest(
            email = "test@example.com",
            password = "password123",
            firstName = "Test",
            lastName = "User",
            dob = LocalDate.now().minusYears(25),
            phoneNumber = "+1234567890"
        )

        private val validCustomerResponse = CustomerResponse.CustomerItem(
            id = "1",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )

        @Test
        @DisplayName("Successful registration returns 201 Created")
        fun `register with valid data returns created response`() = runBlocking {
            // Arrange
            coEvery { customerService.create(validCustomerRequest) } returns validCustomerResponse

            // Act
            val response = customerController.createCustomer(validCustomerRequest)

            // Assert
            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertNotNull(response.body)
            assertEquals(validCustomerResponse.email, response.body?.email)

            // Verify
            coVerify(exactly = 1) { customerService.create(validCustomerRequest) }
        }

        @Test
        @DisplayName("Registration with invalid data throws BadRequest")
        fun `register with invalid data throws exception`() = runBlocking {
            // Arrange
            coEvery { customerService.create(validCustomerRequest) } throws
                    IllegalArgumentException("Email ya registrado")

            try {
                // Act
                customerController.createCustomer(validCustomerRequest)
                fail("Expected ResponseStatusException")
            } catch (e: ResponseStatusException) {
                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
                assertEquals("Email ya registrado", e.reason)
            }
        }
    }

    @Nested
    @DisplayName("Customer Update Tests")
    inner class UpdateTests {
        private val customerId = 1L
        private val validUpdateRequest = CustomerUpdateRequest(
            firstName = "Updated",
            lastName = "User",
            phoneNumber = "+1234567890"
        )

        private val updatedCustomerResponse = CustomerResponse.CustomerItem(
            id = "1",
            email = "test@example.com",
            firstName = "Updated",
            lastName = "User"
        )

        @Test
        @DisplayName("Successful update returns 200 OK")
        fun `update with valid data returns success response`() = runBlocking {
            // Arrange
            coEvery {
                customerService.update(customerId, validUpdateRequest)
            } returns updatedCustomerResponse

            // Act
            val response = customerController.updateCustomer(customerId, validUpdateRequest)

            // Assert
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
            assertEquals("Updated", response.body?.firstName)

            // Verify
            coVerify(exactly = 1) { customerService.update(customerId, validUpdateRequest) }
        }

        @Test
        @DisplayName("Update non-existent customer throws NotFound")
        fun `update non-existent customer throws exception`() = runBlocking {
            // Arrange
            coEvery {
                customerService.update(customerId, validUpdateRequest)
            } throws NoSuchElementException("Cliente no encontrado")

            try {
                // Act
                customerController.updateCustomer(customerId, validUpdateRequest)
                fail("Expected ResponseStatusException")
            } catch (e: ResponseStatusException) {
                // Assert
                assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
                assertEquals("Cliente no encontrado", e.reason)
            }
        }

        @Test
        @DisplayName("Update with invalid data throws BadRequest")
        fun `update with invalid data throws exception`() = runBlocking {
            // Arrange
            coEvery {
                customerService.update(customerId, validUpdateRequest)
            } throws IllegalArgumentException("Datos inválidos")

            try {
                // Act
                customerController.updateCustomer(customerId, validUpdateRequest)
                fail("Expected ResponseStatusException")
            } catch (e: ResponseStatusException) {
                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
                assertEquals("Datos inválidos", e.reason)
            }
        }
    }
}