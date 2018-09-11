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
package net.netconomy.jiraassistant.linksearch.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.AbstractOptionProcessorService;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.AndClauseProject1OptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.CsvProjectOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.HelpOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.IssueTypeProject1OptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.IssueTypeProject2OptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.JsonProjectOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.LinkSearchOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.LinkTypeOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.NoLinkOptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.StatusProject1OptionProcessor;
import net.netconomy.jiraassistant.linksearch.cli.optionprocessors.StatusProject2OptionProcessor;
import net.netconomy.jiraassistant.linksearch.data.CliOutputConfiguration;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchResult;
import net.netconomy.jiraassistant.linksearch.services.LinkSearchResultToCSVService;
import net.netconomy.jiraassistant.linksearch.services.LinkSearchService;

@Service
public class LinkSearchOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkSearchOptionProcessorService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    LinkSearchService linkSearch;

    @Autowired
    LinkSearchResultToCSVService linkSearchToCSVService;

    @Autowired
    FileOutput fileOutput;

    private static final Map<String, LinkSearchOptionProcessor> OPTION_PROCESSOR_MAP = new HashMap<>();
    static {
        OPTION_PROCESSOR_MAP.put("projectsJSON", new JsonProjectOptionProcessor());
        OPTION_PROCESSOR_MAP.put("projectsCSV", new CsvProjectOptionProcessor());
        OPTION_PROCESSOR_MAP.put("noLink", new NoLinkOptionProcessor());
        OPTION_PROCESSOR_MAP.put("issueTypesProject1", new IssueTypeProject1OptionProcessor());
        OPTION_PROCESSOR_MAP.put("issueTypesProject2", new IssueTypeProject2OptionProcessor());
        OPTION_PROCESSOR_MAP.put("linkType", new LinkTypeOptionProcessor());
        OPTION_PROCESSOR_MAP.put("statusProject1", new StatusProject1OptionProcessor());
        OPTION_PROCESSOR_MAP.put("statusProject2", new StatusProject2OptionProcessor());
        OPTION_PROCESSOR_MAP.put("andClauseProject1", new AndClauseProject1OptionProcessor());
        OPTION_PROCESSOR_MAP.put("help", new HelpOptionProcessor());
    }

    @Override
    public void processOptions(Option[] options) {

        CliOutputConfiguration cliConfig = new CliOutputConfiguration();
        LinkSearchData linkSearchData = new LinkSearchData();

        try {

            for (Option thisOption : options) {
                OPTION_PROCESSOR_MAP.get(thisOption.getOpt()).processOption(thisOption, linkSearchData, cliConfig, allOptions);
            }

            executeLinkSearchJSON(linkSearchData, cliConfig);

        } catch (ConfigurationException e) {

            LOGGER.error(e.getMessage(), e);

        }

    }

    private void executeLinkSearchJSON(LinkSearchData linkSearchData, CliOutputConfiguration cliConfig)
            throws ConfigurationException {

        LinkSearchResult linkSearchResult;
        String fileName;
        String fileNameEnding;
        String completeFileName;
        CSVTable table;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        linkSearchResult = linkSearch.searchLinks(credentials, linkSearchData, wantedFields);

        fileName = "LinkSearch_" + linkSearchData.getProject1Data().getProject() + "_"
                + linkSearchData.getProject2Data().getProject();

        if (cliConfig.getAsJson()) {
            fileNameEnding = ".json";

            completeFileName = fileName + fileNameEnding;

            fileOutput.writeObjectAsJsonToFile(linkSearchResult, completeFileName, cliConfig.getEncoding());
        } else {
            fileNameEnding = ".csv";

            completeFileName = fileName + fileNameEnding;

            table = linkSearchToCSVService.generateCSVTableFromLinkSearchResult(linkSearchResult);

            fileOutput.writeCSVTableToFile(table, completeFileName, ';', cliConfig.getEncoding());
        }

    }

}
