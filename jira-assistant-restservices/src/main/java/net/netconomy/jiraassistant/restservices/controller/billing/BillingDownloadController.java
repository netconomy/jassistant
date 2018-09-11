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

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.BillingDataService;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

@Controller
public class BillingDownloadController {

    @Autowired
    BillingDataService billingDataService;

    @RequestMapping(value = "/download/v1/billing/byProjects/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String billingByProjectsJSON(@RequestParam(value = "identifiers", required = true) String identifiers,
        @RequestParam(value = "startDate", required = true) String startDate,
        @RequestParam(value = "endDate", required = true) String endDate,
        @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause,
        @RequestParam(value = "linksToList", required = false) String linksToList,
        @RequestParam(value = "additionalAccounts", required = false) String additionalAccounts,
        @RequestParam(value = "additionalFields", required = false) String additionalFields,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "billing-", ".csv");

        Future<Path> resultFuture = billingDataService.createBillingDataCSVFile(tmpFile, identifiers, startDate, endDate, hoursInAPersonDay,
                issueTypes, andClause, true, linksToList, additionalAccounts, false, additionalFields);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "BillingData_" + identifiers.replace(',', '_') + "_" + startDate + "_" + endDate + ".csv");

        return "download";

    }

    @RequestMapping(value = "/download/v1/billing/byAccounts/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String billingByAccountsJSON(@RequestParam(value = "identifiers", required = true) String identifiers,
        @RequestParam(value = "startDate", required = true) String startDate,
        @RequestParam(value = "endDate", required = true) String endDate,
        @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause,
        @RequestParam(value = "linksToList", required = false) String linksToList,
        @RequestParam(value = "additionalFields", required = false) String additionalFields,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "billing-", ".csv");

        Future<Path> resultFuture = billingDataService.createBillingDataCSVFile(tmpFile, identifiers, startDate, endDate, hoursInAPersonDay,
                issueTypes, andClause, false, linksToList, null, false, additionalFields);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "BillingData_" + identifiers.replace(',', '_') + "_" + startDate + "_" + endDate + ".csv");

        return "download";

    }

    @RequestMapping(value = "/download/v1/billing/byProjects/byMonth/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String billingByProjectsMonthJSON(@RequestParam(value = "identifiers", required = true) String identifiers,
        @RequestParam(value = "month", required = true) String month,
        @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause,
        @RequestParam(value = "linksToList", required = false) String linksToList,
        @RequestParam(value = "additionalAccounts", required = false) String additionalAccounts,
        @RequestParam(value = "additionalFields", required = false) String additionalFields,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "billing-", ".csv");

        Future<Path> resultFuture = billingDataService.createBillingDataCSVFile(tmpFile, identifiers, month, null, hoursInAPersonDay,
                issueTypes, andClause, true, linksToList, additionalAccounts, true, additionalFields);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "BillingData_" + identifiers.replace(',', '_') + "_" + month + ".csv");

        return "download";

    }

    @RequestMapping(value = "/download/v1/billing/byAccounts/byMonth/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String billingByAccountsMonthJSON(@RequestParam(value = "identifiers", required = true) String identifiers,
        @RequestParam(value = "month", required = true) String month,
        @RequestParam(value = "hoursInAPersonDay", required = true) String hoursInAPersonDay,
        @RequestParam(value = "issueTypes", required = false) String issueTypes,
        @RequestParam(value = "andClause", required = false) String andClause,
        @RequestParam(value = "linksToList", required = false) String linksToList,
        @RequestParam(value = "additionalFields", required = false) String additionalFields,
        Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir, "billing-", ".csv");

        Future<Path> resultFuture = billingDataService.createBillingDataCSVFile(tmpFile, identifiers, month, null, hoursInAPersonDay,
                issueTypes, andClause, false, linksToList, null, true, additionalFields);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "BillingData_" + identifiers.replace(',', '_') + "_" + month + ".csv");

        return "download";

    }

}
