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
package net.netconomy.jiraassistant.accountprogress.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.accountprogress.data.AccountProgressResultData;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.AccountBaseData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.AccountBaseDataService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;

@Service
public class AccountProgressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountProgressService.class);

    @Autowired
    AccountBaseDataService accountBaseDataService;

    @Autowired
    AccountProgressFilterService accountProgressFilterService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    JiraSearch jiraSearch;

    List<Issue> getIssuesForAccounts(ClientCredentials credentials, List<Integer> accountIDs, String andClause) {

        List<Issue> issueList = new ArrayList<>();

        String jqlQuery = accountProgressFilterService.generateAccountFilter(accountIDs, andClause);

        issueList.addAll(jiraSearch.searchJiraGetAllIssues(credentials, jqlQuery));

        return issueList;

    }

    List<Integer> addBasicAccountData(ClientCredentials credentials,
            AccountProgressResultData accountProgressResultData, List<String> accountIdentifiers,
            Boolean identifiedByIDs) {
        
        Integer accountID;
        List<Integer> accountIDs = new ArrayList<>();
        AccountBaseData accountBaseData;

        for (String accountIdentifier : accountIdentifiers) {

            if (identifiedByIDs) {

                accountID = Integer.valueOf(accountIdentifier);

                accountIDs.add(accountID);

                accountBaseData = accountBaseDataService.getAccountBaseData(credentials, accountID);

            } else {

                accountBaseData = accountBaseDataService.getAccountBaseData(credentials, accountIdentifier);

                accountIDs.add(accountBaseData.getId());

            }

            accountProgressResultData.addAccountBaseData(accountBaseData);

        }

        return accountIDs;

    }
    
    AccountProgressResultData calculateAccountsProgress(ClientCredentials credentials, ProjectConfiguration projConf,
            List<String> accountIdentifiers, Boolean identifiedByIDs, String andClause) throws ConfigurationException {


        AccountProgressResultData accountProgressResultData;
        List<Integer> accountIDs;
        String currentIssueStatus;
        String estimationFieldName;
        Double currentEstimation = 0.0;
        Integer currentSpentTime;
        Integer estimatedIssuesCount = 0;
        Double allStoryPoints = 0.0;
        Double finishedStoryPoints = 0.0;
        Double closedStoryPoints = 0.0;
        List<Issue> issueList;
        List<String> finishedStatus;
        List<String> closedStatus;
        Integer finishedCount = 0;
        Integer closedCount = 0;
        Double spentTimeOpenIssues = 0.0;
        Double spentTimeFinishedIssues = 0.0;
        Double spentTimeClosedIssues = 0.0;
        
        accountProgressResultData = new AccountProgressResultData();

        accountIDs = addBasicAccountData(credentials, accountProgressResultData, accountIdentifiers, identifiedByIDs);

        estimationFieldName = configurationService.getProjectConfiguration().getEstimationFieldName();
        
        finishedStatus = projConf.getFinishedStatus();
        closedStatus = projConf.getClosedStatus();

        issueList = getIssuesForAccounts(credentials, accountIDs, andClause);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("processing {} Issues for Account Progress Analysis of the Account(s) with the Identifier(s): {}",
                    issueList.size(), accountIdentifiers);
        }

        accountProgressResultData.setAccountProgressDate(DateTime.now().toString());
        accountProgressResultData.setAndClause(andClause);

        accountProgressResultData.setFinishedStatus(projConf.getFinishedStatus());
        accountProgressResultData.setClosedStatus(projConf.getClosedStatus());

        accountProgressResultData.setNumberOfIssuesInAccounts(issueList.size());

        for (Issue currentIssue : issueList) {

            currentEstimation = advancedIssueService.getEstimation(currentIssue, estimationFieldName);
            
            currentIssueStatus = currentIssue.getStatus().getName();

            currentSpentTime = advancedIssueService.getMinutesSpent(currentIssue);

            if (!Double.valueOf(0.0).equals(currentEstimation)) {
                
                estimatedIssuesCount++;
                
                allStoryPoints += currentEstimation;

                if (finishedStatus.contains(currentIssueStatus)) {

                    finishedStoryPoints += currentEstimation;

                } else if (closedStatus.contains(currentIssueStatus)) {

                    closedStoryPoints += currentEstimation;

                }
            
            }
            
            if (finishedStatus.contains(currentIssueStatus)) {
                
                finishedCount++;
                spentTimeFinishedIssues += Double.valueOf(currentSpentTime);
                
            } else if (closedStatus.contains(currentIssueStatus)) {
                
                closedCount++;
                spentTimeClosedIssues += Double.valueOf(currentSpentTime);
                
            } else {
                
                spentTimeOpenIssues += Double.valueOf(currentSpentTime);
                accountProgressResultData.addOpenStatus(currentIssueStatus);
                
            }
            
        }

        accountProgressResultData.setNumberOfEstimatedIssuesInAccounts(estimatedIssuesCount);
        accountProgressResultData.setPercentEstimated(Double.valueOf(estimatedIssuesCount)
                / Double.valueOf(issueList.size()));

        accountProgressResultData.setNumberOfAllStoryPoints(allStoryPoints);
        accountProgressResultData.setNumberOfFinishedStoryPoints(finishedStoryPoints);
        accountProgressResultData.setNumberOfClosedStoryPoints(closedStoryPoints);

        accountProgressResultData.setPercentOfStoryPointsFinished(finishedStoryPoints / allStoryPoints);
        accountProgressResultData.setPercentOfStoryPointsClosed(closedStoryPoints / allStoryPoints);

        accountProgressResultData.setSpentTimeOnOpenIssues(spentTimeOpenIssues);
        accountProgressResultData.setSpentTimeOnFinishedIssues(spentTimeFinishedIssues);
        accountProgressResultData.setSpentTimeOnClosedIssues(spentTimeClosedIssues);

        return accountProgressResultData;

    }

    public AccountProgressResultData calculateAccountProgress(ClientCredentials credentials, List<String> accountKeys,
            Boolean identifiedByIDs, String andClause) {

        try {
            ProjectConfiguration projConf = configurationService.getProjectConfiguration();

            return calculateAccountsProgress(credentials, projConf, accountKeys, identifiedByIDs, andClause);

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        }

    }

}
