/**
*  Copyright 2018 The JASSISTANT Authors.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
**/
package net.netconomy.jiraassistant.base.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;

@Service
public class ZipFileOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipFileOutput.class);

    private void addFileToZipFile(Path file, String fileName, ZipOutputStream zipOutputStream) {

        ZipEntry zipEntry;
        byte[] bytes;
        int length;

        try (InputStream inputStream = Files.newInputStream(file);) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding File {} to Zip File.", file.getFileName());
            }

            zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);

            bytes = new byte[1024];

            while ((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }

        } catch (IOException e) {
            throw new JiraAssistantException("Exception while adding a File to a Zip File.", e);
        } finally {
            try {
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                LOGGER.error("Exception while closing Zip File Entry.", e);
            }
        }

    }

    /**
     * Create a Zip File with the given Files. The String in the Map will be the File Name in the Zip File.
     * 
     * @param zipFile
     * @param filesToZip
     */
    public void createZipFile(Path zipFile, Map<String, Path> filesToZip) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Creating Zip File {} with {} Files.", zipFile.getFileName(), filesToZip.size());
        }

        try (OutputStream outputStream = Files.newOutputStream(zipFile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {

            for (Entry<String, Path> currentSet : filesToZip.entrySet()) {

                addFileToZipFile(currentSet.getValue(), currentSet.getKey(), zipOutputStream);

            }

        } catch (IOException e) {
            throw new JiraAssistantException("Exception while creating a Zip File.", e);
        }

    }

    /**
     * Create a Zip File with the given Files. The String in the Map will be the File Name in the Zip File.
     * 
     * @param zipFileName
     * @param filesToZip
     */
    public void createZipFile(String zipFileName, Map<String, Path> filesToZip) {

        Path zipFile = Paths.get(zipFileName);

        createZipFile(zipFile, filesToZip);

    }

}
