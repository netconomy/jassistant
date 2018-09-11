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

import com.atlassian.jira.rest.client.api.RestClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.LinkSearchDataService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Link Search")
@RestController
public class LinkSearchRestController {

    @Autowired
    LinkSearchDataService linkSearchDataService;

    @ApiOperation(value = "Find Issues with specific links over Projects")
    @RequestMapping(value = "/api/v1/linkSearch/{project1}/{project2}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String linkSearch(
            @ApiParam(value = "Source Project", required = true) @PathVariable(value = "project1") String project1,
            @ApiParam(value = "Destination Project", required = true) @PathVariable("project2") String project2,
            @ApiParam(value = "List of additional Parameters", required = false) @RequestParam(required = false) Map<String, String> parameters) {

        LinkSearchData linkSearchData = linkSearchDataService.parseParameterMapIntoLinkSearchData(project1, project2,
                parameters);

        return linkSearchDataService.getLinkSearchResultJSON(linkSearchData);

    }

    @ApiOperation(value = "Find Issues without specific links over Projects")
    @RequestMapping(value = "/api/v1/noLinkSearch/{project1}/{project2}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String noLinkSearch(
            @ApiParam(value = "Source Project", required = true) @PathVariable(value = "project1") String project1,
            @ApiParam(value = "Destination Project", required = true) @PathVariable(value = "project2") String project2,
            @ApiParam(value = "List of additional Parameters", required = false) @RequestParam(required = false) Map<String, String> parameters) {

        Map<String, String> editedParameters = new HashMap<>();

        editedParameters.putAll(parameters);

        editedParameters.put("noLink", "true");

        LinkSearchData linkSearchData = linkSearchDataService.parseParameterMapIntoLinkSearchData(project1, project2,
                editedParameters);

        return linkSearchDataService.getLinkSearchResultJSON(linkSearchData);

    }

}
