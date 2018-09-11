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
package net.netconomy.jiraassistant.restservices.controller.linkSearch;

import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.LinkSearchDataService;

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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Controller
public class LinkSearchDownloadController {

    @Autowired
    LinkSearchDataService linkSearchDataService;

    private void processLinkSearchCSV(LinkSearchData linkSearchData, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "linkSearch-", ".csv");

        Future<Path> resultFuture = linkSearchDataService.createLinkSearchResultCSVFile(tmpFile, linkSearchData);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "linkSearch_" + linkSearchData.getProject1Data().getProject() + "_" + linkSearchData.getProject2Data().getProject() + ".csv");

    }

    @RequestMapping(value = "/download/v1/linkSearch/{project1}/{project2}/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String linkSearchCSV(@PathVariable("project1") String project1, @PathVariable("project2") String project2,
        @RequestParam Map<String, String> parameters, Model model) throws IOException, ConfigurationException {

        LinkSearchData linkSearchData = linkSearchDataService.parseParameterMapIntoLinkSearchData(project1, project2, parameters);

        processLinkSearchCSV(linkSearchData, model);

        return "download";

    }

    @RequestMapping(value = "/download/v1/noLinkSearch/{project1}/{project2}/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String noLinkSearchCSV(@PathVariable("project1") String project1, @PathVariable("project2") String project2,
        @RequestParam Map<String, String> parameters, Model model) throws IOException, ConfigurationException {

        Map<String, String> editedParameters = new HashMap<>();

        editedParameters.putAll(parameters);

        editedParameters.put("noLink", "true");

        LinkSearchData linkSearchData = linkSearchDataService.parseParameterMapIntoLinkSearchData(project1, project2, editedParameters);

        processLinkSearchCSV(linkSearchData, model);

        return "download";

    }

}
