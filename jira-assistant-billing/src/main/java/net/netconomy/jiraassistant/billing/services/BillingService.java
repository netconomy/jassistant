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
package net.netconomy.jiraassistant.billing.services;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.google.common.base.Joiner;
import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.SprintService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.billing.data.BillingData;
import net.netconomy.jiraassistant.billing.data.BillingIssue;

@Service
public class BillingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    BillingJiraFilterService billingJiraFilterService;

    @Autowired
    SprintService sprintService;

    Double getPersonDays(Integer bookedTimeInTimeFrameMinutes, Double hoursInAPersonDay) {

        Double bookedTimeInHours;
        Double bookedTimeInPersonDays;
        Double bookedTimeInTimeFrameMinutesDouble = Double.valueOf(bookedTimeInTimeFrameMinutes);

        bookedTimeInHours = bookedTimeInTimeFrameMinutesDouble / 60.0;

        bookedTimeInPersonDays = bookedTimeInHours / hoursInAPersonDay;

        return bookedTimeInPersonDays;

    }

    /**
     * Rounds Person Days to the next higher Quarter of a Person Day.
     */
    Double roundPersonDays(Double personDays) {

        Double roundedPersonDays = 0.0;

        roundedPersonDays = Math.ceil(personDays * 4.0) / 4.0;

        return roundedPersonDays;

    }

    private Comment getLastComment(Iterable<Comment> iterable) {

        Iterator<Comment> iterator = iterable.iterator();
        Comment currentComment = null;

        while (iterator.hasNext()) {
            currentComment = iterator.next();
        }

        return currentComment;

    }

    private String getEpicName(ClientCredentials credentials, String issueKey) {

        String epicName = "";
        Issue issue = basicIssueService.getIssue(credentials, issueKey, false);

        if (issue.getFieldByName("Epic Name") != null && issue.getFieldByName("Epic Name").getValue() != null) {
            epicName = issue.getFieldByName("Epic Name").getValue().toString();
        }

        return epicName;

    }

    private String extractJSONObjectValue(JSONObject jsonObject) {

        if (jsonObject.has("value")) {
            return jsonObject.optString("value");
        } else {
            return jsonObject.toString();
        }

    }

    Map<String, String> fetchAdditionalFields(Issue issue, List<String> additionalFields) {

        Map<String, String> additionalFieldValues = new HashMap<>();
        IssueField currentField;
        Object currentValue;

        for (String currentFieldName : additionalFields) {

            currentField = issue.getFieldByName(currentFieldName);

            if (currentField != null && currentField.getValue() != null) {

                currentValue = currentField.getValue();

                if (JSONObject.class.isInstance(currentValue)) {
                    additionalFieldValues.put(currentFieldName, extractJSONObjectValue((JSONObject) currentValue));
                } else if (JSONArray.class.isInstance(currentValue)) {
                    JSONArray jsonArray = (JSONArray) currentValue;
                    List<String> list = new ArrayList<>();
                    Joiner joiner = Joiner.on(", ");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        if(jsonObject != null) {
                            list.add(extractJSONObjectValue(jsonObject));
                        } else {
                            String value = jsonArray.optString(i);

                            if ("Sprint".equalsIgnoreCase(currentFieldName)) {
                                SprintData sprint = sprintService.parseSprintString(value);

                                list.add(sprint.toString());
                            } else {
                                list.add(value);
                            }
                        }

                    }

                    additionalFieldValues.put(currentFieldName, joiner.join(list));
                } else {
                    additionalFieldValues.put(currentFieldName, currentValue.toString());
                }

            }

        }

        return additionalFieldValues;

    }

    private BillingIssue generateBillingIssue(ClientCredentials credentials, Issue issue, DateTime startDate,
        DateTime endDate, Double hoursInAPersonDay, List<String> linksToList, List<String> additionalFields)
        throws ConfigurationException, JSONException {

        BillingIssue billingIssue;
        Integer bookedTimeInTimeFrameMinutes;
        Double bookedPersonDays;
        Integer originalEstimateMinutes;
        String accountFieldValue;
        JSONObject accountJSONObject;
        String currentLinkedIssueKey;
        String currentLinkedIssueKeySnippet;
        Comment lastComment;
        IssueField currentField;
        JSONArray jsonArray;

        billingIssue = new BillingIssue(advancedIssueService.getIssueLight(issue, configuration.getIssueConfiguration()
            .getWantedFields()));

        bookedTimeInTimeFrameMinutes = advancedIssueService.getMinutesSpentWorkaround(credentials, issue, startDate,
            endDate, true);

        billingIssue.setBookedTimeInTimeFrameMinutes(Double.valueOf(bookedTimeInTimeFrameMinutes));

        if (bookedTimeInTimeFrameMinutes.equals(0)) {
            return null;
        }

        originalEstimateMinutes = issue.getTimeTracking().getOriginalEstimateMinutes();

        if (originalEstimateMinutes != null) {

            billingIssue.setOriginalEstimateMinutes(Double.valueOf(originalEstimateMinutes));
            billingIssue.setPersonDaysEstimated(getPersonDays(originalEstimateMinutes, hoursInAPersonDay));

        }

        bookedPersonDays = getPersonDays(bookedTimeInTimeFrameMinutes, hoursInAPersonDay);

        billingIssue.setPersonDaysBooked(bookedPersonDays);

        billingIssue.setPersonDaysBookedRounded(roundPersonDays(bookedPersonDays));

        billingIssue.setPriority(issue.getPriority().getName());

        if (issue.getComponents() != null) {
            for (BasicComponent current : issue.getComponents()) {
                billingIssue.addComponent(current.getName());
            }
        }

        billingIssue.setReporter(issue.getReporter().getDisplayName());

        lastComment = getLastComment(issue.getComments());

        if (lastComment != null) {
            billingIssue.setLastComment(lastComment.getBody());
        }

        currentField = issue.getFieldByName("Account");

        if (currentField != null && currentField.getValue() != null) {

            accountFieldValue = currentField.getValue().toString();

            accountJSONObject = new JSONObject(accountFieldValue);

            billingIssue.setAccountKey(accountJSONObject.optString("key"));
            billingIssue.setAccountName(accountJSONObject.optString("name"));

        }

        currentField = issue.getFieldByName("Epic Link");

        if (currentField != null && currentField.getValue() != null) {

            billingIssue.setEpicKey(currentField.getValue().toString());
            billingIssue.setEpicName(getEpicName(credentials, currentField.getValue().toString()));

        }

        currentField = issue.getFieldByName("Aufwand Wartungsprotokoll");

        if (currentField != null && currentField.getValue() != null) {

            billingIssue.setPersonDaysBillableMaintenanceLog(Double.parseDouble(currentField.getValue().toString()));

        }

        currentField = issue.getFieldByName("Im Wartungsprotokoll");

        if (currentField != null && currentField.getValue() != null) {

            jsonArray = new JSONArray(currentField.getValue().toString());

            billingIssue.setInMaintenanceLog(jsonArray.getJSONObject(0).getString("value"));

        }

        billingIssue.setAdditionalFields(fetchAdditionalFields(issue, additionalFields));

        if (linksToList != null && !linksToList.isEmpty()) {

            for (IssueLink currentLink : issue.getIssueLinks()) {

                currentLinkedIssueKey = currentLink.getTargetIssueKey();

                currentLinkedIssueKeySnippet = currentLinkedIssueKey.substring(0, currentLinkedIssueKey.indexOf('-'));

                if (linksToList.contains(currentLinkedIssueKeySnippet)) {

                    billingIssue.addLinkedIssueKeyToList(currentLinkedIssueKey);

                }

            }

        }

        return billingIssue;

    }

    Boolean parentContainedInList(Issue subIssue, List<Issue> issues) {

        Iterator<Subtask> currentSubtasks;
        Subtask currentSubtask;

        for (Issue current : issues) {

            currentSubtasks = current.getSubtasks().iterator();

            while (currentSubtasks.hasNext()) {
                currentSubtask = currentSubtasks.next();

                if (currentSubtask.getIssueKey().equals(subIssue.getKey())) {
                    return true;
                }
            }

        }

        return false;

    }

    private List<Issue> subIssuesWithoutParentInList(List<Issue> subIssueList, List<Issue> issueList) {

        List<Issue> subIssuesWithoutParentInList = new ArrayList<>();

        for (Issue currentSubIssue : subIssueList) {

            if (!parentContainedInList(currentSubIssue, issueList)) {
                subIssuesWithoutParentInList.add(currentSubIssue);
            }

        }

        return subIssuesWithoutParentInList;

    }

    private List<Issue> collectIssuesForBilling(ClientCredentials credentials, List<String> identifiers, DateTime startDate,
        DateTime endDate, List<String> issueTypes, String andClause,
        Boolean identifiedByProjects, List<String> additionalAccounts,
        BillingData billingData) {

        String issueFilter;
        List<Issue> issueList;
        List<Issue> subIssueList;
        List<Issue> resultList = new ArrayList<>();

        issueFilter = billingJiraFilterService.generateBillingFilter(identifiers, startDate, endDate,
                issueTypes, andClause, additionalAccounts, false, identifiedByProjects);

        billingData.setUsedFilterIssues(issueFilter);

        issueList = jiraSearch.searchJiraGetAllIssues(credentials, issueFilter);

        resultList.addAll(issueList);

        issueFilter = billingJiraFilterService.generateBillingFilter(identifiers, startDate, endDate,
                issueTypes, andClause, additionalAccounts, true, identifiedByProjects);

        billingData.setUsedFilterSubIssues(issueFilter);

        subIssueList = jiraSearch.searchJiraGetAllIssues(credentials, issueFilter);

        resultList.addAll(subIssuesWithoutParentInList(subIssueList, issueList));

        return resultList;

    }

    public BillingData generateBillingData(ClientCredentials credentials, List<String> identifiers, DateTime startDate,
        DateTime endDate, Double hoursInAPersonDay, List<String> issueTypes, String andClause,
        Boolean identifiedByProjects, List<String> linksToList, List<String> additionalAccounts,
        List<String> additionalFields) {

        BillingData billingData = new BillingData();
        BillingIssue currentBillingIssue;

        List<Issue> issueList;

        issueList = collectIssuesForBilling(credentials, identifiers, startDate, endDate, issueTypes, andClause,
            identifiedByProjects, additionalAccounts, billingData);

        if (identifiedByProjects) {
            billingData.setProjectKeys(identifiers);
            billingData.setAccountKeys(additionalAccounts);
        } else {
            billingData.setAccountKeys(identifiers);
        }

        if (issueTypes != null && !issueTypes.isEmpty()) {
            billingData.setIssueTypes(issueTypes);
        }

        if (additionalFields != null && !additionalFields.isEmpty()) {
            billingData.setAdditionalFields(additionalFields);
        }

        billingData.setStartDate(startDate.toString());
        billingData.setEndDate(endDate.toString());

        billingData.setHoursInAPersonday(hoursInAPersonDay);
        billingData.setAndClause(andClause);

        if (linksToList != null && !linksToList.isEmpty()) {
            billingData.setLinksToList(linksToList);
        }

        try {

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Processing {} Issues for Billing.", issueList.size());
            }

            for (Issue current : issueList) {
                currentBillingIssue = generateBillingIssue(credentials, current, startDate, endDate, hoursInAPersonDay,
                    linksToList, additionalFields);

                // Only Issues with booked Time are added
                if (currentBillingIssue != null) {
                    billingData.addBillingIssue(currentBillingIssue);
                }
            }

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Configuration.", e);
        } catch (JSONException e) {
            throw new JiraAssistantException("Error while JSON Data Conversion.", e);
        }

        return billingData;
    }

    public BillingData generateBillingData(ClientCredentials credentials, List<String> identifiers,
        DateTime month, Double hoursInAPersonDay, List<String> issueTypes, String andClause,
        Boolean identifiedByProjects, List<String> linksToList, List<String> additionalAccounts,
        List<String> additionalFields) {

        DateTime startDate = new DateTime(month.getYear(), month.getMonthOfYear(), 1, 0, 0);
        DateTime endDate = startDate.plusMonths(1);

        return generateBillingData(credentials, identifiers, startDate, endDate, hoursInAPersonDay, issueTypes,
            andClause, identifiedByProjects, linksToList, additionalAccounts, additionalFields);
    }

}
