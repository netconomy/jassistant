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
package net.netconomy.jiraassistant.base.scripts;

import com.atlassian.jira.rest.client.api.domain.Issue;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;
import net.netconomy.jiraassistant.base.services.filters.ChangedIssuesFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TempoScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempoScript.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    ChangedIssuesFilterService changedIssuesFilterService;

    @Autowired
    BacklogFilterService backlogFilterService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    FileOutput fileOutput;

    public void executeTempoScript() {

        // System.out.println("---- Jira Issue Skript Start ----");

        List<String> projects = new ArrayList<>();
        String filter = "";
        DateTime startDate = new DateTime(2015, 1, 1, 0, 0);
        DateTime endDate = new DateTime(2015, 2, 1, 0, 0);
        DateTime abortDate = new DateTime(2017, 1, 1, 0, 0);
        List<String> finishedStatusNames;
        List<String> closedStatusNames;
        List<String> bugDefectTypes = new ArrayList<>();
        List<Issue> finishedIssues;
        List<String> issueKeys;
        Double currentStoryPoints;
        Integer finishedStories = 0;
        Integer finishedTasks = 0;
        Integer fixedBugsDefects = 0;
        Integer closedBugsDefects = 0;
        Integer createdBugsDefects = 0;
        Double finishedStoryPoints = 0.0;

        ProjectConfiguration projectConfiguration;
        ClientCredentials credentials;

        CSVTable csvTable = new CSVTable();
        CSVLine csvLine;

        projects.add("PROJ1");
        projects.add("PROJ2");

        try {

            projectConfiguration = configurationService.getProjectConfiguration();
            credentials = configurationService.getClientCredentials();

            bugDefectTypes.addAll(projectConfiguration.getBugIssueTypes());
            bugDefectTypes.addAll(projectConfiguration.getDefectIssueTypes());

            finishedStatusNames = projectConfiguration.getFinishedStatus();
            closedStatusNames = projectConfiguration.getClosedStatus();

            do {

                csvLine = new CSVLine();
                finishedStories = 0;
                finishedTasks = 0;
                fixedBugsDefects = 0;
                closedBugsDefects = 0;
                createdBugsDefects = 0;
                finishedStoryPoints = 0.0;

                // System.out.println(startDate.toString() + " to " + endDate.toString());
                csvLine.addStringToLine(startDate.toString() + " to " + endDate.toString());

                issueKeys = changedIssuesFilterService.getKeysOfIssuesChangedDuringTimeToStatesWorkaround(null,
                        projects, startDate, endDate, finishedStatusNames, credentials);

                filter = backlogFilterService.generateFilterForIssueKeys(issueKeys);

                if (filter == null) {

                    startDate = startDate.plusMonths(1);
                    endDate = endDate.plusMonths(1);

                    continue;
                }

                // System.out.println("   Filter: " + filter);

                finishedIssues = jiraSearch.searchJiraGetAllIssues(credentials, filter);

                // System.out.println("   finished Issues: " + finishedIssues.size());
                csvLine.addStringToLine("" + finishedIssues.size());

                closedBugsDefects = changedIssuesFilterService.getNumberOfIssuesChangedDuringTimeToStatesWorkaround(
                        bugDefectTypes, projects, startDate, endDate, closedStatusNames, credentials);

                filter = changedIssuesFilterService.createFilterForCreatedIssues(bugDefectTypes, projects, null,
                        startDate, endDate);

                createdBugsDefects = jiraSearch.searchJiraGetNumberOfResults(credentials, filter);

                for (Issue current : finishedIssues) {

                    if (projectConfiguration.getStoryIssueTypes()
                            .contains(current.getIssueType().getName())) {
                        finishedStories++;
                    } else if (projectConfiguration.getDefectIssueTypes()
                            .contains(current.getIssueType().getName())) {
                        fixedBugsDefects++;
                    } else if (projectConfiguration.getBugIssueTypes()
                            .contains(current.getIssueType().getName())) {
                        fixedBugsDefects++;
                    } else {
                        finishedTasks++;
                    }

                    currentStoryPoints = advancedIssueService.getEstimation(current, configurationService
                            .getProjectConfiguration().getEstimationFieldName());

                    finishedStoryPoints = finishedStoryPoints + currentStoryPoints;

                }

                // System.out.println("   finished Stories: " + finishedStories);
                csvLine.addStringToLine(finishedStories.toString());
                // System.out.println("   finished StoryPoints: " + finishedStoryPoints);
                csvLine.addStringToLine(finishedStoryPoints.toString());
                // System.out.println("   finished Tasks: " + finishedTasks);
                csvLine.addStringToLine(finishedTasks.toString());
                // System.out.println("   fixed Bugs/Defects: " + fixedBugsDefects);
                csvLine.addStringToLine(fixedBugsDefects.toString());
                // System.out.println("   closed Bugs/Defects: " + closedBugsDefects);
                csvLine.addStringToLine(closedBugsDefects.toString());
                // System.out.println("   created Bugs/Defects: " + createdBugsDefects);
                csvLine.addStringToLine(createdBugsDefects.toString());

                csvTable.addCSVLineToTable(csvLine);

                startDate = startDate.plusMonths(1);
                endDate = endDate.plusMonths(1);

            } while (!startDate.equals(abortDate));

            fileOutput.writeCSVTableToFile(csvTable, "skript_output.csv", null, null);

            // System.out.println("---- Jira Issue Skript End ----");

        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            // e.printStackTrace();
        }

    }

}
