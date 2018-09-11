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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.KanbanAnalysisDataService;

@Api(value = "Kanban Analysis")
@RestController
public class KanbanAnalysisRestController {

    @Autowired
    KanbanAnalysisDataService kanbanAnalysisDataService;

    @ApiOperation(value = "Get an Analysis for a specified Timeframe identified by a Filter")
    @RequestMapping(value = "/api/v1/kanbanAnalysis/byFilter/{backlogFilter}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String kanbanAnalysisByName(
            @ApiParam(value = "Valid Jira QL Filter to define the Backlog", required = true) @PathVariable("backlogFilter") String backlogFilter,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDateString,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDateString,
            @ApiParam(value = "Analysis with alternative Estimations like T-Shirt Sizes, if false Analysis with Story Points", required = false, allowableValues = "true, false", defaultValue = "false") @RequestParam(value = "withAltEstimations", required = false) String withAltEstimations,
            @ApiParam(value = "Light Analysis, if true -> no Issue Detail Data, if false Issue Detail Data", required = false, allowableValues = "true, false", defaultValue = "true") @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
            @ApiParam(value = "Relevant Projects for Defect and Bug Creation Statistics, Projects occuring in the Filter are always included", required = false) @RequestParam(value = "relevantProjects", required = false) String relevantProjects) {

        return kanbanAnalysisDataService.getKanbanAnalysisJSON(backlogFilter, startDateString, endDateString,
                lightAnalysis, relevantProjects, null, null, null, true, withAltEstimations);

    }

    @ApiOperation(value = "Get an Analysis for a specified Timeframe identified by Projects")
    @RequestMapping(value = "/api/v1/kanbanAnalysis/byProjects/{projects}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String kanbanAnalysisById(
            @ApiParam(value = "Projects to define the Backlog, separated by ,", required = true) @PathVariable("projects") String projects,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDateString,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDateString,
            @ApiParam(value = "Analysis with alternative Estimations like T-Shirt Sizes, if false Analysis with Story Points", required = false, allowableValues = "true, false", defaultValue = "false") @RequestParam(value = "withAltEstimations", required = false) String withAltEstimations,
            @ApiParam(value = "Light Analysis, if true -> no Issue Detail Data, if false Issue Detail Data", required = false, allowableValues = "true, false", defaultValue = "true") @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
            @ApiParam(value = "Status to exclude from the Analysis, separated by ,", required = false) @RequestParam(value = "excludedStatus", required = false) String excludedStatusString,
            @ApiParam(value = "Issue Types to exclude from the Analysis, separated by ,", required = false) @RequestParam(value = "excludedTypes", required = false) String excludedTypesString,
            @ApiParam(value = "Logical Clause that will be added for the Backlog Definition", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return kanbanAnalysisDataService.getKanbanAnalysisJSON(projects, startDateString, endDateString, lightAnalysis,
                null, excludedStatusString, excludedTypesString, andClause, false, withAltEstimations);

    }

}
