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

import net.netconomy.jiraassistant.restservices.services.DownloadService;
import net.netconomy.jiraassistant.restservices.services.dataservices.AccountProgressDataService;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

@Controller
public class AccountProgressDownloadController {

    @Autowired
    AccountProgressDataService accountProgressDataService;

    @RequestMapping(value = "/download/v1/accountProgress/byKey/{accountKeys}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String accountProgressByKeyJSON(@PathVariable("accountKeys") String accountKeys,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir,"accountProgress-", ".json");

        Future<Path> resultFuture = accountProgressDataService.createAccountProgressJSONFile(tmpFile, accountKeys, false, andClause);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "accountProgress_" + accountKeys + ".json");

        return "download";

    }

    @RequestMapping(value = "/download/v1/accountProgress/byId/{accountIDs}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    public String accountProgressByIdJSON(@PathVariable("accountIDs") String accountIDs,
        @RequestParam(value = "andClause", required = false) String andClause, Model model) throws IOException, ConfigurationException {

        Files.createDirectories(DownloadService.tempDir);

        Path tmpFile = Files.createTempFile(DownloadService.tempDir,"accountProgress-", ".json");

        Future<Path> resultFuture = accountProgressDataService.createAccountProgressJSONFile(tmpFile, accountIDs, true, andClause);

        DownloadService.fileMap.put(tmpFile.getFileName().toString(), resultFuture);

        model.addAttribute("fileName", tmpFile.getFileName());
        model.addAttribute("downloadName", "accountProgress_" + accountIDs + ".json");

        return "download";

    }

}
