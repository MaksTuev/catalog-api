package ru.tuev.catalog.directory.service.exception

import java.util.*

class ProductAlreadyExistException(
        private val id: UUID
) : Exception() {
    override val message: String?
        get() = "Product with id: $id already exist"
}