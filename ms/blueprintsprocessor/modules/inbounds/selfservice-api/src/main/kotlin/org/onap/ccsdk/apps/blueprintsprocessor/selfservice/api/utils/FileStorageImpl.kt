package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.utils

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
class FileStorageImpl: FileStorage {

    @Value("\${blueprintsprocessor.blueprintDeployPath}")
    lateinit var deployPath: String

    val log = LoggerFactory.getLogger(this::class.java)
    val rootLocation = Paths.get(deployPath)

    override fun store(file: MultipartFile) {
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()))
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable()) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }
}