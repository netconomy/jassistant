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
package net.netconomy.jiraassistant.restservices.controller.kanbanAnalysis;

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.KanbanAnalysisDataService;

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
public class KanbanAnalysisDownloadController {

    @Autowired
    KanbanAnalysisDataService kanbanAnalysisDataService;

    @RequestMapping(value = "/download/v1/kanbanAnalysis/byFilter/{backlogFilter}/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String kanbanAnalysisByFilterJSON(@PathVariable("backlogFilter") String backlogFilter,
        @RequestParam(value = "startDate", required = true) String startDateString,
        @RequestParam(value = "endDate", required = true) String endDateString,
        @RequestParam(value = "withAltEstimations", required = false) String withAltEstimations,
        @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
        @RequestParam(value = "relevantProjects", required = false) String relevantProjects,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "kanbanAnalysis-", ".json");

        Future<Path> resultFuture = kanbanAnalysisDataService.getKanbanAnalysisJSONFile(tmpFile, backlogFilter, startDateString, endDateString,
                lightAnalysis, relevantProjects, null, null, null, true, withAltEstimations);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "kanbanAnalysis_" + startDateString + "_" + endDateString + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/kanbanAnalysis/byProjects/{projects}/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String kanbanAnalysisByProjectsJSON(@PathVariable("projects") String projects,
        @RequestParam(value = "startDate", required = true) String startDateString,
        @RequestParam(value = "endDate", required = true) String endDateString,
        @RequestParam(value = "withAltEstimations", required = false) String withAltEstimations,
        @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
        @RequestParam(value = "excludedStatus", required = false) String excludedStatusString,
        @RequestParam(value = "excludedTypes", required = false) String excludedTypesString,
        @RequestParam(value = "andClause", required = false) String andClause,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "kanbanAnalysis-", ".json");

        Future<Path> resultFuture = kanbanAnalysisDataService.getKanbanAnalysisJSONFile(tmpFile, projects, startDateString, endDateString,
                lightAnalysis, null, excludedStatusString, excludedTypesString, andClause, false, withAltEstimations);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "kanbanAnalysis_" + startDateString + "_" + endDateString + ".json");

        return "download";

    }

}
