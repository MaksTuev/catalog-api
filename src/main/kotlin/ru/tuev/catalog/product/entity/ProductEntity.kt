package ru.tuev.catalog.product.entity

import ru.tuev.catalog.directory.entity.DirectoryEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "product",
        indexes = [ Index(name = "FK__product__directory_id", columnList = "directory_id") ])
data class ProductEntity(

        @Id
        val id: UUID,

        @Column(name = "name", nullable = false)
        val name: String,

        /**
         * Однонаправленная связь с DirectoryEntity чтобы избежать
         * получения слишком большой коллекции на другой стороне
         */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "directory_id", nullable = false)
        val directory: DirectoryEntity
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ProductEntity) return false
        return this.id == (other as ProductEntity?)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
