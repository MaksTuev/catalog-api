package ru.tuev.catalog.product.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.tuev.catalog.common.controller.PageDTO
import ru.tuev.catalog.common.controller.defaultFallback
import ru.tuev.catalog.common.controller.fallback
import ru.tuev.catalog.common.controller.withFallbacks
import ru.tuev.catalog.directory.controller.dto.ProductDTO
import ru.tuev.catalog.directory.service.exception.*
import ru.tuev.catalog.product.service.ProductService
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@RequestMapping("/api/product")
@Validated
class ProductController(
        @Autowired val productService: ProductService
) {

    @GetMapping("")
    fun getForDirectory(
            @RequestParam(name = "directory_id") directoryId: UUID,
            @RequestParam(name = "page") @PositiveOrZero page: Int,
            @RequestParam(name = "size") @Max(100) @Positive size: Int
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.BAD_REQUEST, logger),
                defaultFallback(logger)
        )) {
            val productsPage = productService.getForDirectory(directoryId, page, size)
            return@withFallbacks ResponseEntity.ok(PageDTO(
                    productsPage.content.map { ProductDTO(it) },
                    productsPage.number,
                    productsPage.size,
                    productsPage.totalPages,
                    productsPage.isLast,
                    productsPage.isFirst
            ))
        }
    }

    @GetMapping("/{id}")
    fun get(
            @PathVariable(name = "id") id: UUID
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(ProductNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                defaultFallback(logger)
        )) {
            val product = productService.get(id)
            return@withFallbacks ResponseEntity.ok(ProductDTO(product))
        }
    }

    @PostMapping("")
    fun update(
            @Valid @RequestBody productDTO: ProductDTO
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.BAD_REQUEST, logger),
                fallback(ProductNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                defaultFallback(logger)
        )) {
            val product = productService.save(productDTO)
            return@withFallbacks ResponseEntity.ok(ProductDTO(product))
        }
    }

    @PutMapping("")
    fun create(
            @Valid @RequestBody productDTO: ProductDTO
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.BAD_REQUEST, logger),
                fallback(ProductAlreadyExistException::class, HttpStatus.BAD_REQUEST, logger),
                defaultFallback(logger)
        )) {
            val product = productService.create(productDTO)
            return@withFallbacks ResponseEntity.ok(ProductDTO(product))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable(name = "id") id: UUID
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(ProductNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                defaultFallback(logger)
        )) {
            productService.delete(id)
            return@withFallbacks ResponseEntity.accepted().build()
        }
    }


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)
    }
}