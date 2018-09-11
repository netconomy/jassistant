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
package net.netconomy.jiraassistant.restservices.controller;

import net.netconomy.jiraassistant.restservices.JiraAssistantDownloadException;
import net.netconomy.jiraassistant.restservices.services.DownloadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TempFileDownloadController extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempFileDownloadController.class);

    @Autowired
    DownloadService downloadService;

    @RequestMapping(path = "/downloads/")
    public void downloadTempFile(@RequestParam(value = "fileName", required = true) String fileName,
        @RequestParam(value = "downloadName", required = false) String downloadName,
        HttpServletRequest request, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {

        if(downloadName == null || downloadName.isEmpty()) {
            downloadName = fileName;
        }

        if(DownloadService.fileMap.get(fileName) != null && DownloadService.fileMap.get(fileName).isDone()
            && DownloadService.fileMap.get(fileName).get() != null && DownloadService.fileMap.get(fileName).get().toFile().exists()) {
            downloadService.transferFile(DownloadService.fileMap.get(fileName).get(), downloadName, request, response);
        } else if(DownloadService.fileMap.get(fileName) == null) {
            LOGGER.warn("File {} was not found in temp files", fileName);
            throw new JiraAssistantDownloadException("File not found in temp files. Maybe the name is wrong or it was already cleaned up. Please execute the Analysis again.");
        } else if(!DownloadService.fileMap.get(fileName).isDone()) {
            LOGGER.warn("File {} not created yet", fileName);
            throw new JiraAssistantDownloadException("file not created yet, please go back and try again in a few minutes.");
        } else if(DownloadService.fileMap.get(fileName).get() == null) {
            LOGGER.warn("Error during analysis or file creation", fileName);
            throw new JiraAssistantDownloadException("Error during analysis or file creation, please try again.");
        } else if(!Files.exists(DownloadService.fileMap.get(fileName).get())) {
            LOGGER.warn("File {} not found", fileName);
            throw new JiraAssistantDownloadException("File not found.");
        }

    }

}
