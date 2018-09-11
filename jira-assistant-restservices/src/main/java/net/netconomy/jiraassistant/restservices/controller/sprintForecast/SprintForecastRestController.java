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
package net.netconomy.jiraassistant.restservices.controller.sprintForecast;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.SprintForecastDataService;

@Api(value = "Sprint Forecast")
@RestController
public class SprintForecastRestController {

    @Autowired
    SprintForecastDataService sprintForecastDataService;

    @ApiOperation(value = "Get a Forecast for future Sprints based on a Filter defining the Backlog")
    @RequestMapping(value = "/api/v1/sprintForecast/byFilter/{backlogFilter}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintForecastByFilter(
            @ApiParam(value = "Valid Jira QL Filter to define the Backlog", required = true) @PathVariable("backlogFilter") String backlogFilter,
            @ApiParam(value = "Velocity for the Forecast", required = true) @RequestParam(value = "forecastedVelocity", required = true) String forecastedVelocity,
            @ApiParam(value = "Number of Sprints to Forecast", required = true) @RequestParam(value = "numberOfSprints", required = true) String numberOfSprints) {

        return sprintForecastDataService.getSprintForecastJSON(backlogFilter, null, forecastedVelocity,
                numberOfSprints, true);

    }

    @ApiOperation(value = "Get a Forecast for future Sprints based on Projects defining the Backlog")
    @RequestMapping(value = "/api/v1/sprintForecast/byProjects/{projects}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintForecastByProjects(
            @ApiParam(value = "Projects to define the Backlog, separated by ,", required = true) @PathVariable("projects") String projects,
            @ApiParam(value = "Velocity for the Forecast", required = true) @RequestParam(value = "forecastedVelocity", required = true) String forecastedVelocity,
            @ApiParam(value = "Number of Sprints to Forecast", required = true) @RequestParam(value = "numberOfSprints", required = true) String numberOfSprints,
            @ApiParam(value = "Logical Clause that will be added for the Backlog Definition", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return sprintForecastDataService.getSprintForecastJSON(projects, andClause, forecastedVelocity,
                numberOfSprints, false);

    }

}
