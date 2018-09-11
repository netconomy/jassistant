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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.RapidViewInformationDataService;

@Api(value = "Rapid View Information")
@RestController
public class RapidViewInformationController {

    @Autowired
    RapidViewInformationDataService rapidViewInformationDataService;

    @ApiOperation(value = "Get all existing RapidViews from Jira")
    @RequestMapping(value = "/api/v1/rapidViewInformation/all/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String rapidViewsAll(
            @ApiParam(value = "Sprint Support Enabled, if true -> only Scrum Boards, if false only Kanban Boards", required = false, allowableValues = "true, false", defaultValue = "all Boards") @RequestParam(value = "withSprintSupportEnabled", required = false) String withSprintSupportEnabled) {

        return rapidViewInformationDataService.getAllRapidViewsJSON(withSprintSupportEnabled);

    }

    @ApiOperation(value = "Get all existing Sprints for a specific RapidViews from Jira")
    @RequestMapping(value = "/api/v1/rapidViewInformation/allSprints/{rapidViewId}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintsForRapidView(
            @ApiParam(value = "ID of the Rapid View", required = true) @PathVariable("rapidViewId") String rapidViewId) {

        return rapidViewInformationDataService.getAllSprintsForRapidViewJSON(rapidViewId);

    }

    @ApiOperation(value = "Get all Sprints completed after a specified Date for a specific RapidViews from Jira")
    @RequestMapping(value = "/api/v1/rapidViewInformation/allSprintsCompletedAfter/{rapidViewId}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintsForRapidViewCompletedAfter(
            @ApiParam(value = "ID of the Rapid View", required = true) @PathVariable("rapidViewId") String rapidViewId,
            @ApiParam(value = "All Sprints completed after this Date", required = true) @RequestParam(value = "startDate", required = true) String startDateString) {

        return rapidViewInformationDataService.getAllSprintsForRapidViewAfterDateJSON(rapidViewId, startDateString);

    }

}
