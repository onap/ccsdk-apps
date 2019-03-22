/*
 * Copyright (C) 2019 Bell Canada. All rights reserved.
 *
 * NOTICE:  All the intellectual and technical concepts contained herein are
 * proprietary to Bell Canada and are protected by trade secret or copyright law.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package org.onap.ccsdk.apps.cdssdclistener.service;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("listenerservice")
public class ListenerServiceImpl implements ListenerService {

    @Value("${listenerservice.config.csarArchive}")
    private String csarArchivePath;

    @Value("${listenerservice.config.cbaArchive}")
    private String cbaArchivePath;

    // Size of the buffer to read/write data.
    private static final int BUFFER_SIZE = 4096;

    private static final String CBA_ZIP_PATH = "Artifacts/Resources/[a-zA-Z0-9-_]+/Deployment/CONTROLLER_BLUEPRINT_ARCHIVE/[a-zA-Z0-9-_]+[.]zip";
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerServiceImpl.class);

    @Override
    public void extractBluePrint(String csarArchivePath, String cbaArchivePath) {
        Path cbaStorageDir = getStorageDirectory(cbaArchivePath);
        try (FileInputStream fileToRead = new FileInputStream(
            csarArchivePath); ZipInputStream csarFile = new ZipInputStream(fileToRead)) {

            ZipEntry entry = csarFile.getNextEntry();

            while (entry != null) {
                String fileName = entry.getName();
                if (Pattern.matches(CBA_ZIP_PATH, fileName)) {
                    final String cbaArchiveName = Paths.get(fileName).getFileName().toString();
                    storeBluePrint(csarFile, cbaArchiveName, cbaStorageDir.toString());
                }

                entry = csarFile.getNextEntry();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to extract blueprint", e);
        }
    }

    private void storeBluePrint(ZipInputStream inputStream, String fileName, String cbaArchivePath) {
        final String filePath = Paths.get(cbaArchivePath, UUID.randomUUID().toString() + fileName).normalize()
            .toString();

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read;
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to open file", e);
        }
    }

    private Path getStorageDirectory(String path) {
        Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        if (!Files.exists(fileStorageLocation)) {
            try {
                return Files.createDirectories(fileStorageLocation);
            } catch (IOException e) {
                LOGGER.error("Fail to create directory", e);
            }
        }
        return fileStorageLocation;
    }

    @Override
    public void saveBluePrintToCdsDatabase(ZipFile file) {
        //TODO
    }
}
