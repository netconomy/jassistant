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
package net.netconomy.jiraassistant.sprintanalysis.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.sprintanalysis.data.MultipleSprintAnalysisData;
import net.netconomy.jiraassistant.sprintanalysis.data.SprintResultData;

@Service
public class MultipleSprintAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleSprintAnalysisService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    SprintResultService sprintResultService;

    /**
     * Reads the given File into a SprintResultData Object if possible. If something bad happens, there will be a log
     * Entry and an empty SprintResultData Object.
     * 
     * @param jsonFilePath
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     * @return
     */
    private SprintResultData getSprintResultDataFromJsonFile(Path jsonFilePath, String encoding) {

        SprintResultData sprintResultData = new SprintResultData();
        Gson gson = new Gson();
        Charset charSet;

        if (encoding != null && !encoding.trim().isEmpty() && Charset.isSupported(encoding)) {
            charSet = Charset.forName(encoding);
        } else {
            charSet = StandardCharsets.UTF_8;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Trying to read File {} into a SprintResultData Object.", jsonFilePath.toString());
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(jsonFilePath, charSet)) {

            sprintResultData = gson.fromJson(bufferedReader, SprintResultData.class);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return sprintResultData;

    }

    /**
     * generate MultipleSprintAnalysisData from a given Folder, Files not ending in .json will be ignored.
     * 
     * @param directoryName
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     * @return
     */
    public MultipleSprintAnalysisData generateMultipleSprintAnalysisDataFromFolder(String directoryName, String encoding) {

        MultipleSprintAnalysisData multipleSprintAnalysisData = new MultipleSprintAnalysisData();

        Path directoryPath = FileSystems.getDefault().getPath(directoryName);
        String currentFileName;

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {

            for (Path currentPath : directoryStream) {

                currentFileName = currentPath.getFileName().toString();

                if (!currentPath.toFile().isFile() || !currentFileName.contains(".")) {
                    continue;
                }

                if (".json".equalsIgnoreCase(currentFileName.substring(currentFileName.lastIndexOf(".")))) {
                    multipleSprintAnalysisData.addSprintResult(getSprintResultDataFromJsonFile(currentPath, encoding));
                }

            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return multipleSprintAnalysisData;

    }

    /**
     * generate MultipleSprintAnalysisData from a given List of Sprint Results.
     * 
     * @param sprintIDList
     *            A List of Sprint IDs to analyze
     * @param credentials
     * @param wantedFields
     * @param relevantProjects
     * @return
     */
    public MultipleSprintAnalysisData generateMultipleSprintAnalysisDataFromIDList(List<String> sprintIDList,
            ClientCredentials credentials, List<String> wantedFields, List<String> relevantProjects) {

        MultipleSprintAnalysisData multipleSprintAnalysisData = new MultipleSprintAnalysisData();

        try {
        
            Integer maxSprints = configurationService.getPerformanceConfiguration().getMaxSprintsMultiSprintAnalysis();
        
            if (sprintIDList.size() > maxSprints) {
                throw new JiraAssistantException("The List of Sprints to analyse '" + sprintIDList.toString() + "' "
                        + "was too big, " + sprintIDList.size() + " Sprints and exceeded the defined Maximum of "
                        + maxSprints + ". Please choose a smaller Number of Sprints.");
            }

            LOGGER.info("Analysing {} Sprints for Multi Sprint Analysis.", sprintIDList.size());

            for (String currentSprintID : sprintIDList) {
                LOGGER.info("Analysing Sprint {}", currentSprintID);
                multipleSprintAnalysisData.addSprintResult(sprintResultService.calculateSprintResult(credentials,
                        currentSprintID, true, wantedFields, true, relevantProjects));
            }

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        }

        return multipleSprintAnalysisData;

    }

}
