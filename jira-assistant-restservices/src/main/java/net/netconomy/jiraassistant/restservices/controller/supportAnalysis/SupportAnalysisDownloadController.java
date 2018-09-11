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
package net.netconomy.jiraassistant.restservices.controller.supportAnalysis;

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.SupportAnalysisDataService;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

@Controller
public class SupportAnalysisDownloadController {

    @Autowired
    SupportAnalysisDataService supportAnalysisDataService;

    @RequestMapping(value = "/download/v1/supportAnalysis/byDates/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByDatesJSON(@RequestParam(value = "projects", required = true) String projects,
        @RequestParam(value = "startDate", required = true) String startDate,
        @RequestParam(value = "endDate", required = true) String endDate,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "supportAnalysisData-", ".json");

        Future<Path> resultFuture = supportAnalysisDataService.createSupportAnalysisJSONFile(tmpFile, projects, issueTypes, startDate, endDate,
                andClause, false);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "supportAnalysis_" + projects.replace(',', '_') + "_" + startDate
            + "_" + endDate + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/supportAnalysis/zip/byDates/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByDatesZIP(@RequestParam(value = "projects", required = true) String projects,
        @RequestParam(value = "startDate", required = true) String startDate,
        @RequestParam(value = "endDate", required = true) String endDate,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "supportAnalysisData-", ".zip");

        Future<Path> resultFuture = supportAnalysisDataService.createSupportAnalysisZipFile(tmpFile, projects, issueTypes, startDate, endDate,
                andClause, false);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "supportAnalysis_" + projects.replace(',', '_') + "_" + startDate
            + "_" + endDate + ".zip");

        return "download";

    }

    @RequestMapping(value = "/download/v1/supportAnalysis/byMonth/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByMonthJSON(@RequestParam(value = "projects", required = true) String projects,
        @RequestParam(value = "month", required = true) String month,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "supportAnalysisData-", ".json");

        Future<Path> resultFuture = supportAnalysisDataService.createSupportAnalysisJSONFile(tmpFile, projects, issueTypes, month, null,
                andClause, true);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "supportAnalysis_" + projects.replace(',', '_') + "_" + month
            + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/supportAnalysis/zip/byMonth/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByMonthZIP(@RequestParam(value = "projects", required = true) String projects,
        @RequestParam(value = "month", required = true) String month,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "supportAnalysisData-", ".zip");

        Future<Path> resultFuture = supportAnalysisDataService.createSupportAnalysisZipFile(tmpFile, projects, issueTypes, month, null,
                andClause, true);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "supportAnalysis_" + projects.replace(',', '_') + "_" + month
            + ".zip");

        return "download";

    }

}
