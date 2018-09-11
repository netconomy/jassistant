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
package net.netconomy.jiraassistant.restservices.controller.accountProgress;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.netconomy.jiraassistant.restservices.services.dataservices.AccountProgressDataService;

@Api(value = "Account Progress")
@RestController
public class AccountProgressRestController {

    @Autowired
    AccountProgressDataService accountProgressDataService;

    @ApiOperation(value = "Get the Progress Information for given Accounts identified by their Keys")
    @RequestMapping(value = "/api/v1/accountProgress/byKey/{accountKeys}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String accountProgressByKey(
            @ApiParam(value = "Key(s) of the Account(s), separated by ,", required = true) @PathVariable("accountKeys") String accountKeys,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return accountProgressDataService.getAccountProgressJSON(accountKeys, false, andClause);

    }

    @ApiOperation(value = "Get the Progress Information for given Accounts identified by their IDs")
    @RequestMapping(value = "/api/v1/accountProgress/byId/{accountIDs}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String accountProgressById(
            @ApiParam(value = "Id(s) of the Account(s), separated by ,", required = true) @PathVariable("accountIDs") String accountIDs,
            @ApiParam(value = "Logical Clause that will be added to the Filter for the Analysis", required = false) @RequestParam(value = "andClause", required = false) String andClause) {

        return accountProgressDataService.getAccountProgressJSON(accountIDs, true, andClause);

    }

    @ApiOperation(value = "Get an Account Progress Object filled with Dummy Data")
    @RequestMapping(value = "/api/v1/accountProgress/dataStructure/", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String accountProgressDataStructure() {

        return accountProgressDataService.getAccountProgressDummy();

    }

}
