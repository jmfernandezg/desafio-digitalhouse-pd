package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerUpdateRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para CustomerServiceImpl
 *
 * Revisa la lógica de negocio para:
 * - Autenticación de usuarios
 * - Gestión de clientes
 * - Operaciones CRUD
 * - Estadísticas y reportes
 */
@DisplayName("Customer Service Implementation Tests")
class CustomerServiceImplTest {

    private lateinit var customerRepository: CustomerRepository
    private lateinit var jwtEncoder: JwtEncoder
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var customerService: CustomerServiceImpl

    @BeforeEach
    fun setup() {
        customerRepository = mockk()
        jwtEncoder = mockk()
        passwordEncoder = mockk()
        customerService = CustomerServiceImpl(customerRepository, jwtEncoder, passwordEncoder)
    }

    @Nested
    @DisplayName("Authentication Tests")
    inner class AuthenticationTests {
        private val validEmail = "test@example.com"
        private val validPassword = "password123"
        private val hashedPassword = "hashedPassword123"
        private val mockToken = "jwt.mock.token"

        private val mockCustomer = Customer(
            id = 1L,
            email = validEmail,
            password = hashedPassword,
            firstName = "Test",
            lastName = "User",
            dob = LocalDate.now().minusYears(25)
        )

        @Test
        @DisplayName("Inicio de sesión exitoso con credenciales correctas")
        fun `successful login with valid credentials`() = runBlocking {
            // Arrange
            coEvery { customerRepository.findByEmail(validEmail) } returns mockCustomer
            every { passwordEncoder.matches(validPassword, hashedPassword) } returns true
            every { jwtEncoder.encode(any()) } returns mockk {
                every { tokenValue } returns mockToken
            }

            // Act
            val loginRequest = LoginRequest(validEmail, validPassword)
            val result = customerService.login(loginRequest)

            // Assert
            assertNotNull(result)
            assertEquals(validEmail, result.email)
            assertEquals(mockToken, result.token)

            // Verify
            coVerify(exactly = 1) { customerRepository.findByEmail(validEmail) }
            verify(exactly = 1) { passwordEncoder.matches(validPassword, hashedPassword) }
        }

        @Test
        @DisplayName("Falla el inicio de sesión con contraseña incorrecta")
        fun `login fails with invalid password`(): Unit = runBlocking {
            // Arrange
            coEvery { customerRepository.findByEmail(validEmail) } returns mockCustomer
            every { passwordEncoder.matches(validPassword, hashedPassword) } returns false

            // Act & Assert
            val loginRequest = LoginRequest(validEmail, validPassword)
            assertFailsWith<IllegalArgumentException> {
                customerService.login(loginRequest)
            }
        }
    }

    @Nested
    @DisplayName("Customer Creation Tests")
    inner class CustomerCreationTests {
        private val newCustomerRequest = CustomerCreationRequest(
            email = "new@example.com",
            password = "password123",
            firstName = "New",
            lastName = "Customer",
            dob = LocalDate.now().minusYears(30),
            phoneNumber = "+1234567890"
        )

        @Test
        @DisplayName("Crea cliente exitosamente con datos válidos")
        fun `creates customer successfully with valid data`() = runBlocking {
            // Arrange
            coEvery { customerRepository.existsByEmail(any()) } returns false
            every { passwordEncoder.encode(any()) } returns "encodedPassword"
            coEvery { customerRepository.save(any()) } returnsArgument 0

            // Act
            val result = customerService.create(newCustomerRequest)

            // Assert
            assertNotNull(result)
            assertEquals(newCustomerRequest.email, result.email)
            assertEquals(newCustomerRequest.firstName, result.firstName)

            // Verify
            coVerify { customerRepository.save(any()) }
        }

        @Test
        @DisplayName("Falla al crear cliente con email duplicado")
        fun `fails to create customer with duplicate email`(): Unit = runBlocking {
            // Arrange
            coEvery { customerRepository.existsByEmail(any()) } returns true

            // Act & Assert
            assertFailsWith<IllegalArgumentException> {
                customerService.create(newCustomerRequest)
            }
        }
    }

    @Nested
    @DisplayName("Customer Update Tests")
    inner class CustomerUpdateTests {
        private val existingCustomer = Customer(
            id = 1L,
            email = "existing@example.com",
            password = "hashedPassword",
            firstName = "Existing",
            lastName = "Customer",
            dob = LocalDate.now().minusYears(25)
        )

        private val updateRequest = CustomerUpdateRequest(
            firstName = "Updated",
            phoneNumber = "+9876543210"
        )

        @Test
        @DisplayName("Actualiza cliente exitosamente")
        fun `updates customer successfully`() = runBlocking {
            // Arrange
            coEvery { customerRepository.findById(1L) } returns java.util.Optional.of(existingCustomer)
            coEvery { customerRepository.save(any()) } returnsArgument 0

            // Act
            val result = customerService.update(1L, updateRequest)

            // Assert
            assertNotNull(result)
            assertEquals(updateRequest.firstName, result.firstName)

            // Verify
            coVerify { customerRepository.save(any()) }
        }
    }

    @Nested
    @DisplayName("Statistics Tests")
    inner class StatisticsTests {
        private val mockCustomers = listOf(
            Customer(
                id = 1L,
                email = "customer1@example.com",
                password = "hashed1",
                firstName = "Customer",
                lastName = "One",
                dob = LocalDate.now().minusYears(25),
                countryOfResidence = "Spain"
            ),
            Customer(
                id = 2L,
                email = "customer2@example.com",
                password = "hashed2",
                firstName = "Customer",
                lastName = "Two",
                dob = LocalDate.now().minusYears(35),
                countryOfResidence = "France"
            )
        )

        @Test
        @DisplayName("Genera estadísticas correctamente")
        fun `generates statistics correctly`() = runBlocking {
            // Arrange
            coEvery { customerRepository.findAll() } returns mockCustomers

            // Act
            val stats = customerService.getStatistics()

            // Assert
            assertEquals(2, stats.totalCustomers)
            assertEquals(30.0, stats.averageAge)
            assertEquals(2, stats.customersByCountry.size)
            assertTrue(stats.customersByCountry.containsKey("Spain"))
            assertTrue(stats.customersByCountry.containsKey("France"))
        }
    }

    @Nested
    @DisplayName("Passport Expiry Tests")
    inner class PassportExpiryTests {
        private val expiryDate = LocalDate.now().plusMonths(3)

        @Test
        @DisplayName("Encuentra correctamente los pasaportes por vencer")
        fun `finds expiring passports correctly`() = runBlocking {
            // Arrange
            coEvery {
                customerRepository.findByPassportExpiryBefore(expiryDate)
            } returns listOf(
                Customer(
                    id = 1L,
                    email = "expiring@example.com",
                    password = "hashed",
                    firstName = "Expiring",
                    lastName = "Passport",
                    dob = LocalDate.now().minusYears(30),
                    passportExpiry = LocalDate.now().plusMonths(2)
                )
            )

            // Act
            val result = customerService.findByPassportExpiryBefore(expiryDate)

            // Assert
            assertTrue(result.customers.isNotEmpty())
            assertEquals(1, result.customers.size)
        }
    }
}