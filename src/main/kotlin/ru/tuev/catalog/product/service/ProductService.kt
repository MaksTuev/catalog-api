package ru.tuev.catalog.product.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.tuev.catalog.directory.controller.dto.DirectoryRequestDTO
import ru.tuev.catalog.directory.controller.dto.ProductDTO
import ru.tuev.catalog.directory.entity.DirectoryEntity
import ru.tuev.catalog.directory.repo.DirectoryRepository
import ru.tuev.catalog.directory.service.DirectoryService
import ru.tuev.catalog.directory.service.exception.*
import ru.tuev.catalog.product.entity.ProductEntity
import ru.tuev.catalog.product.repo.ProductRepository
import java.util.*

@Service
class ProductService(
        @Autowired
        val productRepository: ProductRepository,
        @Autowired
        val directoryService: DirectoryService
) {

    @Throws(ProductNotFoundException::class, DirectoryNotFoundException::class)
    fun save(productDTO: ProductDTO): ProductEntity {
        val directory = directoryService.get(productDTO.directoryId)
        val product = this.get(productDTO.id)
        return productRepository.save(product.copy(
                directory = directory,
                name = productDTO.name
        ))
    }

    @Throws(ProductAlreadyExistException::class, DirectoryNotFoundException::class)
    fun create(productDTO: ProductDTO): ProductEntity {
        val directory = directoryService.get(productDTO.directoryId)
        productRepository.findById(productDTO.id)
                .ifPresent { throw ProductAlreadyExistException(it.id) }

        return productRepository.save(ProductEntity(
                name = productDTO.name,
                directory = directory,
                id = productDTO.id))
    }

    @Throws(ProductNotFoundException::class)
    fun delete(id: UUID) {
        this.get(id) //throw exception if absent
        productRepository.deleteById(id)
    }

    @Throws(ProductNotFoundException::class)
    fun get(id: UUID): ProductEntity {
        return productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
    }

    @Throws(DirectoryNotFoundException::class)
    fun getForDirectory(directoryId: UUID, page: Int, size: Int): Page<ProductEntity> {
        directoryService.get(directoryId) //throw exception if absent
        return productRepository.findByDirectoryId(directoryId, PageRequest.of(page, size))
    }
}