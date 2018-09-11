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
package net.netconomy.jiraassistant.restservices.controller.billing;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.BillingDataService;

@Api(value = "Billing")
@RestController
public class BillingRestController {

    @Autowired
    BillingDataService billingDataService;

    @ApiOperation(value = "Get the Billing Data for the given Projects during the given Time-frame")
    @RequestMapping(value = "/api/v1/billing/byProjects/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String billingByProjects(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @RequestParam(value = "identifiers", required = true) String identifiers,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDate,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDate,
            @ApiParam(value = "How many Hours are in a Person Day", required = true) @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
            @ApiParam(value = "Issue Types to analyse", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause,
            @ApiParam(value = "Project Keys of Links to List, separated by ,", required = false) @RequestParam(value = "linksToList", required = false) String linksToList,
            @ApiParam(value = "Account to analyse, separated by ,", required = false) @RequestParam(value = "additionalAccounts", required = false) String additionalAccounts,
            @ApiParam(value = "Fields to add to the Billing Sheet, separated by ,", required = false) @RequestParam(value = "additionalFields", required = false) String additionalFields) {

        return billingDataService.getBillingDataJSON(identifiers, startDate, endDate, hoursInAPersonDay, issueTypes,
                andClause, true, linksToList, additionalAccounts, false, additionalFields);

    }

    @ApiOperation(value = "Get the Billing Data for the given Accounts during the given Time-frame")
    @RequestMapping(value = "/api/v1/billing/byAccounts/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String billingByAccounts(
            @ApiParam(value = "Accounts to analyse, separated by ,", required = true) @RequestParam(value = "identifiers", required = true) String identifiers,
            @ApiParam(value = "Start Date for the Analysis", required = true) @RequestParam(value = "startDate", required = true) String startDate,
            @ApiParam(value = "End Date for the Analysis", required = true) @RequestParam(value = "endDate", required = true) String endDate,
            @ApiParam(value = "How many Hours are in a Person Day", required = true) @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
            @ApiParam(value = "Issue Types to analyse", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause,
            @ApiParam(value = "Project Keys of Links to List, separated by ,", required = false) @RequestParam(value = "linksToList", required = false) String linksToList,
            @ApiParam(value = "Fields to add to the Billing Sheet, separated by ,", required = false) @RequestParam(value = "additionalFields", required = false) String additionalFields) {

        return billingDataService.getBillingDataJSON(identifiers, startDate, endDate, hoursInAPersonDay, issueTypes,
                andClause, false, linksToList, null, false, additionalFields);

    }

    @ApiOperation(value = "Get the Billing Data for the given Projects during the given Month")
    @RequestMapping(value = "/api/v1/billing/byProjects/byMonth/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String billingByProjectsMonth(
            @ApiParam(value = "Projects to analyse, separated by ,", required = true) @RequestParam(value = "identifiers", required = true) String identifiers,
            @ApiParam(value = "Month for the Analysis", required = true) @RequestParam(value = "month", required = true) String month,
            @ApiParam(value = "How many Hours are in a Person Day", required = true) @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
            @ApiParam(value = "Issue Types to analyse", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause,
            @ApiParam(value = "Project Keys of Links to List, separated by ,", required = false) @RequestParam(value = "linksToList", required = false) String linksToList,
            @ApiParam(value = "Account to analyse, separated by ,", required = false) @RequestParam(value = "additionalAccounts", required = false) String additionalAccounts,
            @ApiParam(value = "Fields to add to the Billing Sheet, separated by ,", required = false) @RequestParam(value = "additionalFields", required = false) String additionalFields) {

        return billingDataService.getBillingDataJSON(identifiers, month, null, hoursInAPersonDay, issueTypes,
                andClause, true, linksToList, additionalAccounts, true, additionalFields);

    }

    @ApiOperation(value = "Get the Billing Data for the given Accounts during the given Month")
    @RequestMapping(value = "/api/v1/billing/byAccounts/byMonth/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String billingByAccountsMonth(
            @ApiParam(value = "Accounts to analyse, separated by ,", required = true) @RequestParam(value = "identifiers", required = true) String identifiers,
            @ApiParam(value = "Month for the Analysis", required = true) @RequestParam(value = "month", required = true) String month,
            @ApiParam(value = "How many Hours are in a Person Day", required = true) @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
            @ApiParam(value = "Issue Types to analyse", required = false) @RequestParam(value = "issueTypes", required = false) String issueTypes,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause,
            @ApiParam(value = "Project Keys of Links to List, separated by ,", required = false) @RequestParam(value = "linksToList", required = false) String linksToList,
            @ApiParam(value = "Fields to add to the Billing Sheet, separated by ,", required = false) @RequestParam(value = "additionalFields", required = false) String additionalFields) {

        return billingDataService.getBillingDataJSON(identifiers, month, null, hoursInAPersonDay, issueTypes,
                andClause, false, linksToList, null, true, additionalFields);

    }

}
