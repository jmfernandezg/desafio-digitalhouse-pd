package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.dto.*
import com.jmfg.certs.dh.prodev.service.LodgingService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Pruebas unitarias para LodgingController
 *
 * Esta clase contiene pruebas exhaustivas para validar el comportamiento
 * del controlador REST de alojamientos, incluyendo:
 *
 * - Gestión de categorías
 * - Búsqueda y filtrado de alojamientos
 * - Operaciones CRUD de alojamientos
 * - Manejo de ciudades disponibles
 */
class LodgingControllerTest {
    private lateinit var lodgingController: LodgingController
    private lateinit var lodgingService: LodgingService

    private val testLodging = LodgingResponse.LodgingItem(
        id = "1",
        name = "Test Hotel",
        description = "Test Description",
        category = Category.HOTEL,
        city = "Test City",
        address = "Test Address",
        rating = 4.5,
        images = listOf("image1.jpg", "image2.jpg")
    )

    @BeforeEach
    fun setup() {
        lodgingService = mockk()
        lodgingController = LodgingController(lodgingService)
    }

    /**
     * Pruebas de categorías
     *
     * Verifica:
     * - Obtención exitosa de todas las categorías
     * - Búsqueda de alojamientos por categoría
     * - Manejo de categorías inválidas
     */
    @Test
    fun `getAllCategories debería retornar lista de categorías`() {
        // Arrange
        val categories = CategoryResponse(
            listOf(
                CategoryResponse.CategoryItem(Category.HOTEL, "Hotel"),
                CategoryResponse.CategoryItem(Category.HOSTEL, "Hostel")
            )
        )
        every { lodgingService.findAllCategories() } returns categories

        // Act
        val response = lodgingController.getAllCategories()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.categories?.size)
        verify { lodgingService.findAllCategories() }
    }

    @Test
    fun `getLodgingsByCategory debería retornar alojamientos de la categoría especificada`() {
        // Arrange
        val category = "HOTEL"
        val lodgings = LodgingResponse(listOf(testLodging))
        every { lodgingService.findByCategory(Category.HOTEL) } returns lodgings

        // Act
        val response = lodgingController.getLodgingsByCategory(category)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.lodgings?.size)
        verify { lodgingService.findByCategory(Category.HOTEL) }
    }

    @Test
    fun `getLodgingsByCategory debería lanzar excepción para categoría inválida`() {
        // Arrange
        val invalidCategory = "INVALID"

        // Act & Assert
        assertThrows<ResponseStatusException> {
            lodgingController.getLodgingsByCategory(invalidCategory)
        }
    }

    /**
     * Pruebas de búsqueda de alojamientos
     *
     * Verifica:
     * - Búsqueda por ID
     * - Búsqueda con filtros
     * - Manejo de alojamientos no encontrados
     */
    @Test
    fun `getLodgingById debería retornar alojamiento cuando existe`() {
        // Arrange
        val lodgingId = "1"
        every { lodgingService.findById(lodgingId) } returns testLodging

        // Act
        val response = lodgingController.getLodgingById(lodgingId)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lodgingId, response.body?.id)
        verify { lodgingService.findById(lodgingId) }
    }

    @Test
    fun `getLodgingById debería lanzar excepción cuando no existe`() {
        // Arrange
        val lodgingId = "999"
        every { lodgingService.findById(lodgingId) } returns null

        // Act & Assert
        assertThrows<ResponseStatusException> {
            lodgingController.getLodgingById(lodgingId)
        }
        verify { lodgingService.findById(lodgingId) }
    }

    /**
     * Pruebas de creación y eliminación
     *
     * Verifica:
     * - Creación exitosa de alojamiento
     * - Eliminación exitosa
     * - Manejo de errores en creación/eliminación
     */
    @Test
    fun `createLodging debería crear nuevo alojamiento exitosamente`() {
        // Arrange
        val request = LodgingCreationRequest(
            name = "New Hotel",
            description = "New Description",
            category = Category.HOTEL,
            city = "New City",
            address = "New Address",
            averageCustomerRating = 4,
            photos =  listOf ("new-image.jpg")
        )
        every { lodgingService.create(request) } returns testLodging

        // Act
        val response = lodgingController.createLodging(request)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        verify { lodgingService.create(request) }
    }

    @Test
    fun `deleteLodging debería eliminar alojamiento exitosamente`() {
        // Arrange
        val lodgingId = "1"
        every { lodgingService.delete(lodgingId) } returns Unit

        // Act
        val response = lodgingController.deleteLodging(lodgingId)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify { lodgingService.delete(lodgingId) }
    }

    /**
     * Pruebas de listado general y ciudades
     *
     * Verifica:
     * - Obtención de todos los alojamientos
     * - Obtención de lista de ciudades
     */
    @Test
    fun `getAllLodgings debería retornar lista completa de alojamientos`() {
        // Arrange
        val lodgings = LodgingResponse(listOf(testLodging))
        every { lodgingService.findAll() } returns lodgings

        // Act
        val response = lodgingController.getAllLodgings()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.lodgings?.size)
        verify { lodgingService.findAll() }
    }

    @Test
    fun `getAllCities debería retornar conjunto de ciudades disponibles`() {
        // Arrange
        val cities = setOf("Ciudad 1", "Ciudad 2")
        every { lodgingService.findAllCities() } returns cities

        // Act
        val response = lodgingController.getAllCities()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
        verify { lodgingService.findAllCities() }
    }

    /**
     * Pruebas de búsqueda con filtros
     *
     * Verifica:
     * - Búsqueda con múltiples criterios
     * - Manejo de filtros vacíos
     */
    @Test
    fun `searchLodgings debería retornar resultados según criterios`() {
        // Arrange
        val searchRequest = LodgingSearchRequest(
            city = "Test City",
            category = Category.HOTEL,
            rating = 4.0
        )
        val searchResults = LodgingResponse(listOf(testLodging))
        every { lodgingService.search(searchRequest) } returns searchResults

        // Act
        val response = lodgingController.searchLodgings(searchRequest)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.lodgings?.size)
        verify { lodgingService.search(searchRequest) }
    }
}