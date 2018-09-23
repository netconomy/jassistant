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
package net.netconomy.jiraassistant.restservices.controller.sprintAnalysis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisService;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.SprintAnalysisDataService;

import java.io.IOException;

@Api(value = "Sprint Analysis")
@RestController
public class SprintAnalysisRestController {

    @Autowired
    SprintAnalysisDataService sprintAnalysisDataService;

    @Autowired
    MultipleSprintAnalysisService multipleSprintAnalysisService;

    @ApiOperation(value = "Get a Sprint Analysis for a Sprint identified by it's Name")
    @RequestMapping(value = "/api/v1/sprintAnalysis/byName/{sprintName}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintAnalysisByName(
            @ApiParam(value = "Name of the Sprint", required = true) @PathVariable("sprintName") String sprintName,
            @ApiParam(value = "Light Analysis, if true -> no Issue Detail Data, if false Issue Detail Data", required = false, allowableValues = "true, false", defaultValue = "true") @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
            @ApiParam(value = "Relevant Projects for Defect and Bug Creation Statistics, Projects occuring in the Sprint are always included", required = false) @RequestParam(value = "relevantProjects", required = false) String relevantProjects) {

        return sprintAnalysisDataService.getSprintAnalysisJSON(sprintName, false, lightAnalysis, relevantProjects);

    }

    @ApiOperation(value = "Get a Sprint Analysis for a Sprint identified by it's Id")
    @RequestMapping(value = "/api/v1/sprintAnalysis/byId/{sprintId}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintAnalysisById(
            @ApiParam(value = "Id of the Sprint", required = true) @PathVariable("sprintId") String sprintId,
            @ApiParam(value = "Light Analysis, if true -> no Issue Detail Data, if false Issue Detail Data", required = false, allowableValues = "true, false", defaultValue = "true") @RequestParam(value = "lightAnalysis", required = false) String lightAnalysis,
            @ApiParam(value = "Relevant Projects for Defect and Bug Creation Statistics, Projects occuring in the Sprint are always included", required = false) @RequestParam(value = "relevantProjects", required = false) String relevantProjects) {

        return sprintAnalysisDataService.getSprintAnalysisJSON(sprintId, true, lightAnalysis, relevantProjects);

    }

    @ApiOperation(value = "Get a Sprint Analysis Object filled with Dummy Data")
    @RequestMapping(value = "/api/v1/sprintAnalysis/dataStructure/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sprintAnalysisDataStructure() {

        return sprintAnalysisDataService.getSprintAnalysisDummy();

    }

    @ApiOperation(value = "Get a Sprint Analysis for a Sprint identified by it's Id")
    @RequestMapping(value = "/api/v1/multiSprintAnalysis/byId/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String multipleSprintAnalysisByIdCSV(@RequestParam("sprintIds") String sprintIds,
                                                @RequestParam(value = "relevantProjects", required = false) String relevantProjects,
                                                Model model) throws IOException, ConfigurationException {
        return this.sprintAnalysisDataService.getMultipleSprintAnalysisJSON(sprintIds, relevantProjects);
    }

}
