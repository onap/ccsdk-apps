package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.utils

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

interface FileStorage {
    fun store(file: MultipartFile)
    fun loadFile(filename: String): Resource
}