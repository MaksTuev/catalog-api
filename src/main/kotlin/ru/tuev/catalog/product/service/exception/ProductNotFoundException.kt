package ru.tuev.catalog.directory.service.exception

import java.util.*

class ProductNotFoundException(
        private val id: UUID
) : Exception() {
    override val message: String?
        get() = "Product with id: $id not found"
}