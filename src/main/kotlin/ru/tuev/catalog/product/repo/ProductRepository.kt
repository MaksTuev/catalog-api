package ru.tuev.catalog.product.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tuev.catalog.product.entity.ProductEntity
import java.util.*
import javax.security.auth.Subject


@Repository
interface ProductRepository : JpaRepository<ProductEntity, UUID> {

    fun findByDirectoryId(directoryId: UUID, p: Pageable): Page<ProductEntity>

}