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
package net.netconomy.jiraassistant.restservices.controller.projectStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.ProjectStatusDataService;

@Api(value = "Project Status")
@RestController
public class ProjectStatusRestController {

    @Autowired
    ProjectStatusDataService projectStatusDataService;

    @ApiOperation(value = "Get the Status of Issues in the specified Projects")
    @RequestMapping(value = "/api/v1/projectStatus/{projects}/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String projectStatus(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @PathVariable("projects") String projects,
            @ApiParam(value = "Field Name the Issues should be grouped by, e.g. 'Epic Link'", required = false) @RequestParam(value = "groupBy", required = false) String groupBy,
            @ApiParam(value = "Issue Types to exclude from the Analysis, separated by ,", required = false) @RequestParam(value = "excludedTypes", required = false) String excludedTypesString,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return projectStatusDataService.getProjectStatusDataJSON(projects, groupBy, excludedTypesString, andClause);

    }




}
