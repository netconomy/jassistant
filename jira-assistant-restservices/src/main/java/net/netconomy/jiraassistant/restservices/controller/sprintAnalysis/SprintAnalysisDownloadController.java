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
package net.netconomy.jiraassistant.restservices.controller.sprintAnalysis;

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.SprintAnalysisDataService;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

@Controller
public class SprintAnalysisDownloadController {

    @Autowired
    SprintAnalysisDataService sprintAnalysisDataService;

    @RequestMapping(value = "/download/v1/sprintAnalysis/byName/{sprintName}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String sprintAnalysisByNameJSON(@PathVariable("sprintName") String sprintName,
        @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
        @RequestParam(value = "relevantProjects", required = false) String relevantProjects,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir,"sprintAnalysis-", ".json");

        Future<Path> resultFuture = sprintAnalysisDataService.createSprintAnalysisJSONFile(tmpFile, sprintName, false, lightAnalysis, relevantProjects);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "sprint_" + sprintName + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/sprintAnalysis/byId/{sprintId}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String sprintAnalysisByIdJSON(@PathVariable("sprintId") String sprintId,
        @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
        @RequestParam(value = "relevantProjects", required = false) String relevantProjects,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "sprintAnalysis-", ".json");

        Future<Path> resultFuture = sprintAnalysisDataService.createSprintAnalysisJSONFile(tmpFile, sprintId, true, lightAnalysis, relevantProjects);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "sprint_" + sprintId + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/multiSprintAnalysis/byId/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String multipleSprintAnalysisByIdCSV(@RequestParam("sprintIds") String sprintIds,
        @RequestParam(value = "relevantProjects", required = false) String relevantProjects,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "sprintAnalysis-", ".csv");

        Future<Path> resultFuture = sprintAnalysisDataService.createMultipleSprintAnalysisCSVFile(tmpFile, sprintIds, relevantProjects);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "sprint_" + sprintIds.replace(',', '_') + ".csv");

        return "download";

    }

}
