package ru.tuev.catalog.directory.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude
import ru.tuev.catalog.directory.entity.DirectoryEntity
import ru.tuev.catalog.product.entity.ProductEntity
import java.util.*
import javax.validation.constraints.NotNull

class ProductDTO(
        @NotNull
        val id: UUID,
        @NotNull
        val name: String,
        @NotNull
        val directoryId: UUID
) {

    constructor (
            directory: ProductEntity
    ) : this(directory.id,
            directory.name,
            directory.directory.id
    )
}