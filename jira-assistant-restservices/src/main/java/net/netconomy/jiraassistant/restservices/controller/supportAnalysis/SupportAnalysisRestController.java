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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.SupportAnalysisDataService;

@Api(value = "Support Analysis")
@RestController
public class SupportAnalysisRestController {

    @Autowired
    SupportAnalysisDataService supportAnalysisDataService;

    @ApiOperation(value = "Get an Analysis of the Support work during the given Time")
    @RequestMapping(value = "/api/v1/supportAnalysis/byDates/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByDates(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @RequestParam(value = "projects", required = true) String projects,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDate,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDate,
            @ApiParam(value = "Issue Types to analyse, separated by ',', if non are given, all will be analysed", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return supportAnalysisDataService.getSupportAnalysisJSON(projects, issueTypes, startDate, endDate, andClause,
                false);

    }

    @ApiOperation(value = "Get an Analysis of the Support work during the given Month")
    @RequestMapping(value = "/api/v1/supportAnalysis/byMonth/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String supportAnalysisByMonth(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @RequestParam(value = "projects", required = true) String projects,
            @ApiParam(value = "Month for the Analysis", required = true) @RequestParam(value = "month", required = true) String month,
            @ApiParam(value = "Issue Types to analyse, separated by ',', if non are given, all will be analysed", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return supportAnalysisDataService.getSupportAnalysisJSON(projects, issueTypes, month, null, andClause, true);

    }

}
