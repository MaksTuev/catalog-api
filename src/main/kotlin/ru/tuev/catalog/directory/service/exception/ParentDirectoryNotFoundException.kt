package ru.tuev.catalog.directory.service.exception

import java.util.*

class ParentDirectoryNotFoundException(
        private val id: UUID
) : Exception() {
    override val message: String?
        get() = "Parent directory with id: $id not found"
}