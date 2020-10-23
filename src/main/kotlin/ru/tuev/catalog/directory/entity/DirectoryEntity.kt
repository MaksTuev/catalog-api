package ru.tuev.catalog.directory.entity

import ru.tuev.catalog.product.entity.ProductEntity
import java.util.*
import javax.persistence.*

/**
 * Уровень каталога
 */
@Entity
@Table(name = "directory")
data class DirectoryEntity(

        @Id
        val id: UUID,

        @Column(name = "name", nullable = false)
        val name: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        val parent: DirectoryEntity?,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = [CascadeType.REMOVE])
        val children: List<DirectoryEntity> = emptyList()
) {

    override fun equals(other: Any?): Boolean {
        if (other !is DirectoryEntity) return false
        return this.id == (other as DirectoryEntity?)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        //override for avoid recursion
        return "DirectoryEntity(id=$id, name=$name, parent=${parent?.id}"
    }
}
