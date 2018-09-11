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
package net.netconomy.jiraassistant.restservices.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    public static final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "JAssistant");

    public static ConcurrentHashMap<String, Future<Path>> fileMap = new ConcurrentHashMap<>();

    private static final int BUFFER_SIZE = 1024;

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    /**
     * The Function pushes a given File to the HTTP Response
     * 
     * @param file
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    public void transferFile(Path file, String fileName, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String headerKey;
        String headerValue;

        try (InputStream inputStream = Files.newInputStream(file);
                OutputStream outStream = response.getOutputStream()) {

            // get absolute path of the application
            ServletContext context = request.getServletContext();

            // get MIME type of the file
            String mimeType = context.getMimeType(file.toAbsolutePath().toString());
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) Files.size(file));

            // set headers for the response
            headerKey = "Content-Disposition";
            headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpTempDirectoryAndTable() {

        Integer deleted = 0;
        List<String> remainingFiles = new ArrayList<>();

        try {

            Files.createDirectories(tempDir);

            for(Map.Entry<String, Future<Path>> current : DownloadService.fileMap.entrySet()) {

                if(current.getValue().isDone()) {

                    Files.deleteIfExists(tempDir.resolve(current.getKey()));

                    DownloadService.fileMap.remove(current.getKey());

                    deleted++;

                }

            }

        } catch (IOException e) {
            LOGGER.error("Error while deleting temporary Files during Cron Job", e);
        }

        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Temp Directory Clean up completed, {} Files were deleted", deleted);
        }

        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(DownloadService.tempDir)) {
            for(Path path : dirStream) {
                remainingFiles.add(path.toString());
            }

            if(!remainingFiles.isEmpty()) {
                LOGGER.warn("The Temp Directory is not empty after Clean Up, after deleting {} Files. There were {} Files left."
                        , deleted, remainingFiles.size());
            }
        } catch (IOException e) {
            LOGGER.error("Error while checking Temp Directory during Cron Job", e);
        }

    }

}
