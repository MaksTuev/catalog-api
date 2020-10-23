package ru.tuev.catalog.directory.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude
import ru.tuev.catalog.directory.entity.DirectoryEntity
import java.util.*

class DirectoryResponseDTO(
        val id: UUID,
        val name: String,
        val parentId: UUID?,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val children: List<DirectoryResponseDTO>?
) {

    constructor (
            directory: DirectoryEntity,
            includeChildren: Boolean
    ) : this(directory.id,
            directory.name,
            directory.parent?.id,
            if (includeChildren) directory.children.map { DirectoryResponseDTO(it, false) } else null
    )
}