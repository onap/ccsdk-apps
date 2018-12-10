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

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.CloseCondition;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CbaFileManagementService.java Purpose: Provide Service processing CBA file management
 *
 * @author Steve Siani
 * @version 1.0
 */
@Service
public class CbaFileManagementService {
    private static EELFLogger log = EELFManager.getInstance().getLogger(CbaFileManagementService.class);

    @Value("${load.cbaExtension}")
    private String cbaExtension;

    private static final String CBA_FILE_NAME_PATTERN = "CBA_{0}_{1}";


    /**
     * cleanupSavedCBA: This method cleanup the Zip file and the unzip directory that was added.
     *
     * @param zipFileName zipFileName
     * @param cbaFileLocation cbaFileLocation
     * @return
     * @throws BluePrintException BluePrintException
     */
    public void cleanupSavedCBA(String zipFileName, Path cbaFileLocation) throws BluePrintException {

        String fileNameWithoutExtension = ConfigModelUtils.stripFileExtension(zipFileName);

        try {
            //Delete the Zip file from the repository
            ConfigModelUtils.deleteCBA(zipFileName, cbaFileLocation);

            //Delete the CBA directory from the repository
            ConfigModelUtils.deleteCBA(fileNameWithoutExtension, cbaFileLocation);
        }
        catch (IOException ex){
            throw new BluePrintException("Fail while cleaning up CBA saved!", ex);
        }
    }

    /**
     * This is a saveCBAFile method
     * tske a {@link FilePart}, transfer it to disk using {@link AsynchronousFileChannel}s and return a {@link Mono} representing the result
     *
     * @param (filePart, targetDirectory) - the request part containing the file to be saved and the default directory where to save
     * @return a {@link Mono} representing the result of the operation
     * @throws (BluePrintException, IOException) BluePrintException, IOException
     */
    public Mono<String> saveCBAFile(FilePart filePart, Path targetDirectory) throws BluePrintException, IOException {

        // Normalize file name
        final String fileName = StringUtils.cleanPath(filePart.filename());

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new BluePrintException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        // Check if the file's extension is "CBA"
        if(!StringUtils.getFilenameExtension(fileName).equals(cbaExtension)) {
            throw new BluePrintException("Invalid file extention.");
        }

        // Change file name to match a pattern
        String changedFileName = ConfigModelUtils.getCbaFileName(fileName, this.CBA_FILE_NAME_PATTERN);

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = targetDirectory.resolve(changedFileName);

        // if a file with the same name already exists in a repository, delete and recreate it
        File file = new File(targetLocation.toString());
        if (file.exists())
            file.delete();
        file.createNewFile();

        // create an async file channel to store the file on disk
        final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.WRITE);

        final CloseCondition closeCondition = new CloseCondition();

        // pointer to the end of file offset
        AtomicInteger fileWriteOffset = new AtomicInteger(0);
        // error signal
        AtomicBoolean errorFlag = new AtomicBoolean(false);

        log.info("Subscribing to file parts");
        // FilePart.content produces a flux of data buffers, each need to be written to the file
        return filePart.content().doOnEach(dataBufferSignal -> {
            if (dataBufferSignal.hasValue() && !errorFlag.get()) {
                // read data from the incoming data buffer into a file array
                DataBuffer dataBuffer = dataBufferSignal.get();
                int count = dataBuffer.readableByteCount();
                byte[] bytes = new byte[count];
                dataBuffer.read(bytes);

                // create a file channel compatible byte buffer
                final ByteBuffer byteBuffer = ByteBuffer.allocate(count);
                byteBuffer.put(bytes);
                byteBuffer.flip();

                // get the current write offset and increment by the buffer size
                final int filePartOffset = fileWriteOffset.getAndAdd(count);
                log.info("Processing file part at offset {}", filePartOffset);
                // write the buffer to disk
                closeCondition.onTaskSubmitted();
                fileChannel.write(byteBuffer, filePartOffset, null, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        // file part successfuly written to disk, clean up
                        log.info("Done saving file part {}", filePartOffset);
                        byteBuffer.clear();

                        if (closeCondition.onTaskCompleted())
                            try {
                                log.info("Closing after last part");
                                fileChannel.close();
                            } catch (IOException ignored) {
                                ignored.printStackTrace();
                            }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        // there was an error while writing to disk, set an error flag
                        errorFlag.set(true);
                        log.info("Error while saving file part {}", filePartOffset);
                    }
                });
            }
        }).doOnComplete(() -> {
            // all done, close the file channel
            log.info("Done processing file parts");
            if (closeCondition.canCloseOnComplete())
                try {
                    log.info("Closing after complete");
                    fileChannel.close();
                } catch (IOException ignored) {
                }

        }).doOnError(t -> {
            // ooops there was an error
            log.info("Error processing file parts");
            try {
                fileChannel.close();
            } catch (IOException ignored) {
            }
            // take last, map to a status string
        }).last().map(dataBuffer ->
        {
            //TODO Turn this into asynchrounous code
            while (!closeCondition.canCloseOnComplete()){
                log.info("Wait until uploading file {} completed...", changedFileName);
            }
            if (!errorFlag.get()) {
                return changedFileName;
            } else {
                return null;
            }
        });
    }
}
