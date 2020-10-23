package ru.tuev.catalog.directory.controller.dto

import java.util.*
import javax.validation.constraints.NotNull


class DirectoryRequestDTO(
        @NotNull
        val id: UUID,
        @NotNull
        val name: String,
        val parentId: UUID?
)