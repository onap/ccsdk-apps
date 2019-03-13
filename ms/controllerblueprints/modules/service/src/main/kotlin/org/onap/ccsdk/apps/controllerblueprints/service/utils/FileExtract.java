/*
 * Copyright © 2018-2019 IBM Intellectual Property.
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
package org.onap.ccsdk.apps.controllerblueprints.service.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class FileExtract {
    public void extract(String source,String destination)  throws Exception {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
        }
    }
    public String zip(String path) {
        try {
            ZipFile zipFile = new ZipFile(path+System.currentTimeMillis()+".zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFolder(path, parameters);

        }catch(ZipException e){

        }
        return path+System.currentTimeMillis()+".zip";
    }
    public static String isToString(InputStream is) throws Exception {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
    public static void main(String args[]) throws Exception{
        FileExtract fe=new FileExtract();
        fe.extract("C:\\Users\\IBM_ADMIN\\Desktop\\angular 7\\text.zip","C:\\Users\\IBM_ADMIN\\Desktop\\angular 7\\");
        System.out.println("Done");
    }


}
