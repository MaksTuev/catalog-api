package ru.tuev.catalog.directory.service.exception

import java.util.*

class DirectoryNotFoundException(
        private val id: UUID
) : Exception() {
    override val message: String?
        get() = "Directory with id: $id not found"
}