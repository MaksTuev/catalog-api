package ru.tuev.catalog.directory.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.tuev.catalog.common.controller.defaultFallback
import ru.tuev.catalog.common.controller.fallback
import ru.tuev.catalog.common.controller.withDefaultFallback
import ru.tuev.catalog.common.controller.withFallbacks
import ru.tuev.catalog.directory.controller.dto.DirectoryRequestDTO
import ru.tuev.catalog.directory.controller.dto.DirectoryResponseDTO
import ru.tuev.catalog.directory.service.exception.DirectoryNotFoundException
import ru.tuev.catalog.directory.service.DirectoryService
import ru.tuev.catalog.directory.service.exception.DirectoryAlreadyExistException
import ru.tuev.catalog.directory.service.exception.ParentDirectoryNotFoundException
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/directory")
@Validated
class DirectoryController(
        @Autowired val directoryService: DirectoryService
) {

    @GetMapping("", params = ["top_level=true"])
    fun getTopLevelDirectories(): ResponseEntity<Any> {
        return withDefaultFallback(logger) {
            val directories = directoryService.getTopLevelDirectories()
                    .map { DirectoryResponseDTO(it, false) }
            return@withDefaultFallback ResponseEntity.ok(directories)
        }
    }

    @GetMapping("/{id}")
    fun get(
            @PathVariable(name = "id") id: UUID
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                defaultFallback(logger)
        )) {
            val directory = directoryService.get(id)
            return@withFallbacks ResponseEntity.ok(DirectoryResponseDTO(directory, true))
        }
    }

    @PostMapping("")
    fun update(
            @Valid @RequestBody directoryDTO: DirectoryRequestDTO
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                fallback(ParentDirectoryNotFoundException::class, HttpStatus.BAD_REQUEST, logger),
                defaultFallback(logger)
        )) {
            val directory = directoryService.save(directoryDTO)
            return@withFallbacks ResponseEntity.ok(DirectoryResponseDTO(directory, true))
        }
    }

    @PutMapping("")
    fun create(
            @Valid @RequestBody directoryDTO: DirectoryRequestDTO
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(ParentDirectoryNotFoundException::class, HttpStatus.BAD_REQUEST, logger),
                fallback(DirectoryAlreadyExistException::class, HttpStatus.BAD_REQUEST, logger),
                defaultFallback(logger)
        )) {
            val directory = directoryService.create(directoryDTO)
            return@withFallbacks ResponseEntity.ok(DirectoryResponseDTO(directory, true))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(
            @PathVariable(name = "id") id: UUID
    ): ResponseEntity<Any> {
        return withFallbacks(listOf(
                fallback(DirectoryNotFoundException::class, HttpStatus.NOT_FOUND, logger),
                defaultFallback(logger)
        )) {
            directoryService.delete(id)
            return@withFallbacks ResponseEntity.accepted().build()
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DirectoryController::class.java)
    }
}