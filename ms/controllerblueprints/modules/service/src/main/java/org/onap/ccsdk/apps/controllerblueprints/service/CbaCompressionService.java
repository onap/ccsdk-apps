/*
 * Copyright © 2018 IBM Intellectual Property.
 * Modifications Copyright © 2018 IBM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ccsdk.apps.controllerblueprints.service;

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * CbaCompressionService.java Purpose: Unzip/decompress the zip file and return the path of decompressed folder
 *
 * @author Vinal Patel
 * @version 1.0
 */

@Service
public class CbaCompressionService {


    /**
     * @param zipInputStream
     * @param unzipFilePath
     * @throws IOException
     */
    public static void unzipFiles(final ZipInputStream zipInputStream, final Path unzipFilePath) throws IOException {

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(unzipFilePath.toAbsolutePath().toString()))) {
            byte[] bytesIn = new byte[1024];
            int read;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
        catch (IOException ex) {
            throw new IOException("Fail to unzip the file.", ex);
        }
    }

    /**
     * Decompress the file into the cbaFileLocation parameter
     * @param zipfilename name of the zipped file
     * @param cbaFileLocation path in which the zipped file will get decompressed
     * @return Strng the path in which the file is decompressed
     * @throws BluePrintException Exception in the process
     */
    public static String decompressCBAFile(final String zipfilename, Path cbaFileLocation) throws BluePrintException {

        Path filepath = cbaFileLocation.resolve(zipfilename);

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filepath.toString()))) {
            Path directoryPath = Files.createDirectories(cbaFileLocation.resolve(zipfilename.substring(0, zipfilename.lastIndexOf('.'))));
            ZipEntry entry = zipInputStream.getNextEntry();


            while (entry != null) {
                Path filePath = Paths.get(directoryPath.toString(), entry.getName());
                if (!entry.isDirectory()) {
                    unzipFiles(zipInputStream, filePath);
                } else {
                    Files.createDirectories(filePath);
                }

                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }

            return directoryPath.toString();

        } catch (IOException ex) {
            throw new BluePrintException("Fail to decompress " + zipfilename + ". Please try again!", ex);
        }
    }

}
