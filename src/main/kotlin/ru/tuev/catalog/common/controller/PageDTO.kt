package ru.tuev.catalog.common.controller

class PageDTO<T> (
        val content: List<T>,
        val page: Int,
        val size: Int,
        val totalPages: Int,
        val last: Boolean,
        val first: Boolean
)