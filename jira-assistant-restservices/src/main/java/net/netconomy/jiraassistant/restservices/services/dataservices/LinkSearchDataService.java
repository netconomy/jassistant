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
package net.netconomy.jiraassistant.restservices.services.dataservices;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.atlassian.jira.rest.client.api.RestClientException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData.LinkSearchDataBuilder;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchResult;
import net.netconomy.jiraassistant.linksearch.data.ProjectSearchData.ProjectSearchDataBuilder;
import net.netconomy.jiraassistant.linksearch.services.LinkSearchResultToCSVService;
import net.netconomy.jiraassistant.linksearch.services.LinkSearchService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class LinkSearchDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkSearchDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    LinkSearchService linkSearchService;

    @Autowired
    LinkSearchResultToCSVService linkSearchToCSVService;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    private LinkSearchResult getLinkSearchResult(LinkSearchData linkSearchData) throws ConfigurationException {

        LinkSearchResult linkSearchResult;
        ClientCredentials credentials;
        List<String> wantedFields;

        credentials = configuration.getClientCredentials();
        wantedFields = configuration.getIssueConfiguration().getWantedFields();

        linkSearchResult = linkSearchService.searchLinks(credentials, linkSearchData, wantedFields);

        return linkSearchResult;

    }

    public LinkSearchData parseParameterMapIntoLinkSearchData(String project1, String project2,
            Map<String, String> parameterMap) {

        LinkSearchData linkSearchData;
        ProjectSearchDataBuilder project1SearchDataBuilder;
        ProjectSearchDataBuilder project2SearchDataBuilder;

        project1SearchDataBuilder = new ProjectSearchDataBuilder(project1);
        project1SearchDataBuilder.issueTypes(basicDataService.splitString(parameterMap.get("issueTypesP1"), ","));
        project1SearchDataBuilder.negateIssueTypes(Boolean.valueOf(parameterMap.get("negateIssueTypesP1")));
        project1SearchDataBuilder.status(basicDataService.splitString(parameterMap.get("statusP1"), ","));
        project1SearchDataBuilder.negateStatus(Boolean.valueOf(parameterMap.get("negateStatusP1")));
        project1SearchDataBuilder.andClause(parameterMap.get("andClauseP1"));

        project2SearchDataBuilder = new ProjectSearchDataBuilder(project2);
        project2SearchDataBuilder.issueTypes(basicDataService.splitString(parameterMap.get("issueTypesP2"), ","));
        project2SearchDataBuilder.negateIssueTypes(Boolean.valueOf(parameterMap.get("negateIssueTypesP2")));
        project2SearchDataBuilder.status(basicDataService.splitString(parameterMap.get("statusP2"), ","));
        project2SearchDataBuilder.negateStatus(Boolean.valueOf(parameterMap.get("negateStatusP2")));

        LinkSearchDataBuilder linkSearchDataBuilder = new LinkSearchDataBuilder(project1SearchDataBuilder.build(),
                project2SearchDataBuilder.build());
        linkSearchDataBuilder.noLink(Boolean.valueOf(parameterMap.get("noLink")));
        linkSearchDataBuilder.linkType(parameterMap.get("linkType"));
        linkSearchDataBuilder.negateLinkType(Boolean.valueOf(parameterMap.get("negateLinkType")));

        linkSearchData = linkSearchDataBuilder.build();

        return linkSearchData;

    }

    public String getLinkSearchResultJSON(LinkSearchData linkSearchData) {

        LinkSearchResult linkSearchResult;

        try {
            linkSearchResult = getLinkSearchResult(linkSearchData);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(linkSearchResult);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }
    }

    @Async
    public Future<Path> createLinkSearchResultCSVFile(Path tmpFile, LinkSearchData linkSearchData) throws ConfigurationException {

        LinkSearchResult linkSearchResult;
        CSVTable csvTable;

        linkSearchResult = getLinkSearchResult(linkSearchData);

        csvTable = linkSearchToCSVService.generateCSVTableFromLinkSearchResult(linkSearchResult);

        fileOutput.writeCSVTableToFile(csvTable, tmpFile, null, null);

        return new AsyncResult<Path>(tmpFile);

    }

}
