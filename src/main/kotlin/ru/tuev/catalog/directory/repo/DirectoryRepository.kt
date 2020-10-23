package ru.tuev.catalog.directory.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tuev.catalog.directory.entity.DirectoryEntity
import java.util.*

@Repository
interface DirectoryRepository : JpaRepository<DirectoryEntity, UUID> {

    fun findByParentIsNull(): List<DirectoryEntity>
}