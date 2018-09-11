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
package net.netconomy.jiraassistant.restservices.controller.reopenFactor;

import com.atlassian.jira.rest.client.api.RestClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.ReopenFactorDataService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Reopen Factor")
@RestController
public class ReopenFactorRestController {

    @Autowired
    ReopenFactorDataService reopenFactorDataService;

    @ApiOperation(value = "Get reopen Information for a specific Project")
    @RequestMapping(value = "/api/v1/reopenFactor/{projectNames}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String reopenFactor(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @PathVariable("projectNames") String projectNames,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDateString,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDateString,
            @ApiParam(value = "Logical Clause that will be added to drill down the analysed Issues", required = false) @RequestParam(value = "andClause", required = false) String andClause,
            @ApiParam(value = "Threshold for reopens, if reached or passed the Issue is tracked", required = false) @RequestParam(value = "threshold", required = false) String thresholdString) {

        return reopenFactorDataService
                .getReopenFactorJSON(projectNames, startDateString, endDateString, andClause, thresholdString);

    }

}
