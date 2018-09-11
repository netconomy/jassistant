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
package net.netconomy.jiraassistant.supportanalysis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.supportanalysis.data.ErrorCategoryData;

@Service
public class AdditionalSupportDataService {

    @Autowired
    AdvancedIssueService advancedIssueService;

    public Map<String, ErrorCategoryData> gatherErrorCategoryData(List<Issue> issueList, SupportConfiguration config,
            ClientCredentials credentials, DateTime startDate, DateTime endDate) {

        Map<String, ErrorCategoryData> errorCategoryMap = new HashMap<>();
        List<String> errorCategoryFieldValues;
        Integer newCount = 0;
        ErrorCategoryData currentErrorCategoryData;
        Integer spentTimeOnIssueMinutesFull = 0;
        Double spentTimeOnIssueMinutesAliqout = 0.0;
        Integer newSpentTimeMinutesFull = 0;
        Double newSpentTimeMinutesAliqout = 0.0;

        for (Issue currentIssue : issueList) {

            errorCategoryFieldValues = advancedIssueService.getMultipleChoiceFieldValues(currentIssue,
                    config.getErrorCategoryFieldName());

            if (errorCategoryFieldValues.isEmpty()) {
                continue;
            }

            spentTimeOnIssueMinutesFull = advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue,
                    startDate, endDate, true);
            spentTimeOnIssueMinutesAliqout = spentTimeOnIssueMinutesFull.doubleValue()
                    / errorCategoryFieldValues.size();

            for (String currentErrorCategory : errorCategoryFieldValues) {

                if (errorCategoryMap.containsKey(currentErrorCategory)) {
                    currentErrorCategoryData = errorCategoryMap.get(currentErrorCategory);

                    currentErrorCategoryData.setCategoryCount(currentErrorCategoryData.getCategoryCount() + 1);
                    currentErrorCategoryData.setCategoryMinutesSpentFull(currentErrorCategoryData
                            .getCategoryMinutesSpentFull() + spentTimeOnIssueMinutesFull);
                    currentErrorCategoryData.setCategoryMinutesSpentAliquot(currentErrorCategoryData
                            .getCategoryMinutesSpentAliquot() + spentTimeOnIssueMinutesAliqout);

                    errorCategoryMap.put(currentErrorCategory, currentErrorCategoryData);
                } else {
                    newCount = 1;
                    newSpentTimeMinutesFull = spentTimeOnIssueMinutesFull;
                    newSpentTimeMinutesAliqout = spentTimeOnIssueMinutesAliqout;

                    errorCategoryMap.put(currentErrorCategory, new ErrorCategoryData(currentErrorCategory, newCount,
                            newSpentTimeMinutesAliqout, newSpentTimeMinutesFull));
                }



            }

        }

        return errorCategoryMap;

    }

    public Map<String, Integer> countTechnicalSeverity(List<Issue> issueList, SupportConfiguration config) {

        Map<String, Integer> technicalSeverityMap = new HashMap<>();
        IssueField technicalSeverityField;
        JSONObject technicalSeverityFieldJSON;
        String technicalSeverityFieldValue;
        Integer currentCount = 0;
        Integer newCount = 0;

        for (Issue currentIssue : issueList) {

            technicalSeverityField = currentIssue.getFieldByName(config.getTechnicalSeverityFieldName());

            if (technicalSeverityField == null || technicalSeverityField.getValue() == null
                    || technicalSeverityField.getValue().toString().isEmpty()) {
                continue;
            }

            try {

                technicalSeverityFieldJSON = new JSONObject(technicalSeverityField.getValue().toString());

                technicalSeverityFieldValue = technicalSeverityFieldJSON.optString("value");

                if (technicalSeverityFieldValue == null || technicalSeverityFieldValue.isEmpty()
                        || "null".equals(technicalSeverityFieldValue)) {
                    continue;
                }

                if (technicalSeverityMap.containsKey(technicalSeverityFieldValue)) {
                    currentCount = technicalSeverityMap.get(technicalSeverityFieldValue);
                    newCount = currentCount + 1;
                } else {
                    newCount = 1;
                }

                technicalSeverityMap.put(technicalSeverityFieldValue, newCount);

            } catch (JSONException e) {
                throw new JiraAssistantException("The Field " + config.getTechnicalSeverityFieldName()
                        + " could not be read from the Issue " + currentIssue.getKey(), e);
            }

        }

        return technicalSeverityMap;
    }
    
}
