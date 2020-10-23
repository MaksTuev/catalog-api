package ru.tuev.catalog.directory.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.tuev.catalog.directory.controller.dto.DirectoryRequestDTO
import ru.tuev.catalog.directory.entity.DirectoryEntity
import ru.tuev.catalog.directory.service.exception.DirectoryNotFoundException
import ru.tuev.catalog.directory.repo.DirectoryRepository
import ru.tuev.catalog.directory.service.exception.DirectoryAlreadyExistException
import ru.tuev.catalog.directory.service.exception.ParentDirectoryNotFoundException
import java.util.*

@Service
class DirectoryService(
        @Autowired
        val directoryRepository: DirectoryRepository
) {

    @Throws(ParentDirectoryNotFoundException::class, DirectoryNotFoundException::class)
    fun save(directoryDTO: DirectoryRequestDTO): DirectoryEntity {
        val parentEntity = directoryDTO.parentId?.let {
            directoryRepository.findById(it).orElseThrow { ParentDirectoryNotFoundException(it) }
        }
        val directory = this.get(directoryDTO.id)
        return directoryRepository.save(directory.copy(
                parent = parentEntity,
                name = directoryDTO.name
        ))
    }

    @Throws(ParentDirectoryNotFoundException::class, DirectoryAlreadyExistException::class)
    fun create(directoryDTO: DirectoryRequestDTO): DirectoryEntity {
        val parentEntity = directoryDTO.parentId?.let {
            directoryRepository.findById(it).orElseThrow { ParentDirectoryNotFoundException(it) }
        }

        directoryRepository.findById(directoryDTO.id)
                .ifPresent { throw DirectoryAlreadyExistException(it.id) }

        return directoryRepository.save(DirectoryEntity(
                name = directoryDTO.name,
                parent = parentEntity,
                id = directoryDTO.id))
    }

    @Throws(DirectoryNotFoundException::class)
    fun delete(id: UUID) {
        this.get(id) //throw exception if absent
        directoryRepository.deleteById(id)
    }

    @Throws(DirectoryNotFoundException::class)
    fun get(id: UUID): DirectoryEntity {
        return directoryRepository.findById(id).orElseThrow { DirectoryNotFoundException(id) }
    }

    fun getTopLevelDirectories(): List<DirectoryEntity> {
        return directoryRepository.findByParentIsNull()
    }
}