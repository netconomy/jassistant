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
package net.netconomy.jiraassistant.restservices.controller.estimationStatistics;

import com.google.common.net.HttpHeaders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.EstimationStatisticsDataService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Estimation Statistics")
@RestController
public class EstimationStatisticsRestController {

    @Autowired
    EstimationStatisticsDataService estimationStatisticsDataService;

    @ApiOperation(value = "Get statistic Data for Estimations in given Projects")
    @RequestMapping(value = "/api/v1/estimationStatistics/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String estimationStatistics(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @RequestParam(value = "projects", required = true) String projects,
            @ApiParam(value = "Issue Types to analyse", required = true) @RequestParam(value = "issueTypes", required = true) String issueTypes,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDate,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDate,
            @ApiParam(value = "Use alternative Estimations, besides Story Points", required = false) @RequestParam(value = "altEstimations", required = false) String altEstimations,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return estimationStatisticsDataService.getEstimationStatisticsJSON(projects, issueTypes, startDate, endDate,
                altEstimations, andClause);

    }

}
