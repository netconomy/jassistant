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
package net.netconomy.jiraassistant.base.services.issues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.atlassian.jira.rest.client.api.domain.TimeTracking;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.Worklog;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.restclient.JiraIssueRestService;

@Service
public class AdvancedIssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedIssueService.class);

    @Autowired
    BasicIssueService basicIssueService;
    
    @Autowired
    JiraIssueRestService jiraIssueRestService;

    /**
     * Returns a IssueLight Object with basic Data and the wantedFields from the given Issue.
     * 
     * @param issue
     * @param wantedFields
     * @return
     */
    public IssueLight getIssueLight(Issue issue, List<String> wantedFields) {

        IssueLight lightIssue = new IssueLight();

        lightIssue.setKey(issue.getKey());
        lightIssue.setProject(issue.getProject().getName());
        lightIssue.setIssueType(issue.getIssueType().getName());
        lightIssue.setSummary(issue.getSummary());
        lightIssue.setStatus(issue.getStatus().getName());
        lightIssue.setPriority(issue.getPriority().getName());
        lightIssue.setWantedFields(wantedFields);

        if (issue.getAssignee() != null) {
            lightIssue.setAssignee(issue.getAssignee().getName());
        }

        if (issue.getResolution() != null) {
            lightIssue.setResolution(issue.getResolution().getName());
        }

        if (issue.getFixVersions() != null) {
            for (Version currentVersion : issue.getFixVersions()) {
                lightIssue.addFixVersion(currentVersion.getName());
            }
        }

        if (issue.getCreationDate() != null) {
            lightIssue.setCreatedDate(issue.getCreationDate().toString());
        }

        if (issue.getUpdateDate() != null) {
            lightIssue.setUpdatedDate(issue.getUpdateDate().toString());
        }

        if (issue.getDueDate() != null) {
            lightIssue.setDueDate(issue.getDueDate().toString());
        }

        if (issue.getFieldByName("Desired Date") != null && issue.getFieldByName("Desired Date").getValue() != null) {
            lightIssue.setDesiredDate(issue.getFieldByName("Desired Date").getValue().toString());
        }

        lightIssue.setMinutesSpent(getMinutesSpent(issue));

        if (issue.getFieldByName("Original Estimate") != null && issue.getFieldByName("Original Estimate").getValue() != null) {
            lightIssue.setMinutesEstimated(Integer.parseInt(issue.getFieldByName("Original Estimate").getValue().toString())/60);
        }

        if(lightIssue.getMinutesEstimated() != 0) {
            lightIssue.setMinutesRemaining(lightIssue.getMinutesEstimated() - lightIssue.getMinutesSpent());
        } else {
            lightIssue.setMinutesRemaining(null);
        }

        if (issue.getFieldByName("Account") != null && issue.getFieldByName("Account").getValue() != null) {

            JSONObject accountObject = (JSONObject) issue.getFieldByName("Account").getValue();

            lightIssue.setAccountKey(accountObject.optString("key"));
            lightIssue.setAccountName(accountObject.optString("name"));

        }

        for (String currentField : wantedFields) {
            if (issue.getFieldByName(currentField) == null || issue.getFieldByName(currentField).getValue() == null) {
                lightIssue.addField(currentField, null);
            } else {
                lightIssue.addField(currentField, issue.getFieldByName(currentField).getValue().toString());
            }
        }

        return lightIssue;
    }

    /**
     * Returns the Double Value of the Field with the given Name or 0.0.
     * 
     * @param issue
     * @param estimationFieldName
     * @return
     */
    public Double getEstimation(Issue issue, String estimationFieldName) {
        Object estimationFieldValue = null;
        IssueField estimationField = null;

        estimationField = issue.getFieldByName(estimationFieldName);

        if (estimationField != null) {
            estimationFieldValue = issue.getFieldByName(estimationFieldName).getValue();
        } else {
            return 0.0;
        }

        if (estimationFieldValue != null && estimationFieldValue.getClass().equals(Double.class)) {
            return (Double) estimationFieldValue;
        } else {
            return 0.0;
        }
    }

    /**
     * Returns the String Value of the Field with the given Name or an empty String.
     *
     * @param issue
     * @param altEstimationFieldName
     * @return
     */
    public String getAltEstimation(Issue issue, String altEstimationFieldName) {
        Object altEstimationFieldValue = null;
        JSONObject jsonObject;
        IssueField altEstimationField = null;

        altEstimationField = issue.getFieldByName(altEstimationFieldName);

        if (altEstimationField != null) {
            altEstimationFieldValue = issue.getFieldByName(altEstimationFieldName).getValue();
        } else {
            return "";
        }

        if (altEstimationFieldValue != null && altEstimationFieldValue.getClass().equals(JSONObject.class)) {
            jsonObject = (JSONObject) altEstimationFieldValue;
            return jsonObject.optString("value");
        } else if (altEstimationFieldValue != null && altEstimationFieldValue.getClass().equals(String.class)) {
            return (String) altEstimationFieldValue;
        } else {
            return "";
        }
    }

    Double getMinutesSpentFromWorklog(ClientCredentials clientCredentials, String issueKey, DateTime startDate,
            DateTime endDate) {

        Double minutesSpent = 0.0;
        Double secondsSpent = 0.0;
        String secondsSpentString;
        JSONArray worklogJsonArray;
        JSONObject currentWorklog;
        DateTime startedDate;

        worklogJsonArray = jiraIssueRestService.getWorklogForIssue(clientCredentials, issueKey);

        for (int i = 0; i < worklogJsonArray.length(); i++) {
            currentWorklog = worklogJsonArray.optJSONObject(i);

            if (currentWorklog != null) {
                startedDate = DateTime.parse(currentWorklog.optString("started"));
                secondsSpentString = currentWorklog.optString("timeSpentSeconds");

                if (!startedDate.isBefore(startDate) && startedDate.isBefore(endDate)
                        && secondsSpentString != null) {
                    secondsSpent = Double.valueOf(secondsSpentString);
                    minutesSpent += secondsSpent / 60.0;
                }

            }

        }

        return minutesSpent;

    }

    /**
     * This Function accesses the API directly and retrieves the Minutes spent over the Worklog API. Optionally it can
     * also add the Minutes spent on Sub Issues.
     * 
     * @param clientCredentials
     * @param issue
     * @param startDate
     * @param endDate
     * @param withSubIssues
     * @return
     */
    public Integer getMinutesSpentWorkaround(ClientCredentials clientCredentials, Issue issue, DateTime startDate, DateTime endDate,
            Boolean withSubIssues) {

        Double minutesSpent = 0.0;
        StringBuilder logMessage = new StringBuilder();
        Iterable<Subtask> subIssues;
        Integer numberOfSubIssues = 0;
        
        logMessage.append("Retrieving Worklogs for Issue " + issue.getKey());

        if(withSubIssues) {
            logMessage.append(" including SubIssues.");
        } else {
            logMessage.append(" without SubIssues.");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(logMessage.toString());
        }
        
        minutesSpent += getMinutesSpentFromWorklog(clientCredentials, issue.getKey(), startDate, endDate);
        
        subIssues = issue.getSubtasks();
        
        if (subIssues instanceof Collection) {
            numberOfSubIssues = ((Collection<Subtask>)subIssues).size();
        }
        
        if (withSubIssues && numberOfSubIssues != 0) {
            for (Subtask currentSubTask : subIssues) {
                minutesSpent += getMinutesSpentFromWorklog(clientCredentials, currentSubTask.getIssueKey(),
                        startDate, endDate);
            }
        }

        return minutesSpent.intValue();

    }

    /**
     * Gets the Minutes Spent on this Issue, between startDate and endDate, from the Worklog. This Functions only able
     * to use 25 Worklog Entries.
     * 
     * @deprecated Since Jira only delivers 25 Worklog Entries the Results of this Function can be wrong if there are
     *             more then 25 Entries on the given Issue.
     * 
     * @param issue
     * @param startDate
     * @param endDate
     * @return
     */
    @Deprecated
    public Integer getMinutesSpent(Issue issue, DateTime startDate, DateTime endDate) {

        Integer minutesSpent = 0;

        for (Worklog currentWorklog : issue.getWorklogs()) {

            if (!currentWorklog.getStartDate().isBefore(startDate) && currentWorklog.getStartDate().isBefore(endDate)) {
                minutesSpent += currentWorklog.getMinutesSpent();
            }
        }

        return minutesSpent;
    }

    /**
     * Gets the Minutes Spent on this Issue. This Function doesn't use the Worklog Entries and uses all time bookings on
     * the Issue. Can also get the Time including the Sub Issues. Through inconsistencies in the JRJC Framework this
     * Function only works properly with Issues fetched through the REST Workaround. You need to fetch the additional
     * Fields timespent and aggregatetimespent.
     * 
     * @param issue
     * @param withSubIssues
     * @return
     */
    public Integer getMinutesSpentForWorkaround(Issue issue, Boolean withSubIssues) {

        Integer minutesSpentFromTimeTracking = 0;
        IssueField timeSpentField;

        if (withSubIssues) {
            timeSpentField = issue.getField("aggregatetimespent");
        } else {
            timeSpentField = issue.getField("timespent");
        }

        if (timeSpentField != null && timeSpentField.getValue() != null && timeSpentField.getValue().toString() != null
            && !timeSpentField.getValue().toString().equalsIgnoreCase("null")) {
            try {
                minutesSpentFromTimeTracking = Integer.parseInt(timeSpentField.getValue().toString()) / 60;
            } catch (NumberFormatException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Error while formatting {} to an Integer", timeSpentField.getValue().toString(), e);
                }
            }
        }

        return minutesSpentFromTimeTracking;
    }

    /**
     * Gets the Minutes Spent on this Issue. This Function doesn't use the Worklog Entries and uses all time bookings on
     * the Issue. Does not get Time booked on Sub Issues.
     * 
     * @param issue
     * @return
     */
    public Integer getMinutesSpent(Issue issue) {

        Integer minutesSpentFromTimeTracking = 0;

        TimeTracking timeTracking = issue.getTimeTracking();

        if (timeTracking != null && timeTracking.getTimeSpentMinutes() != null) {
            minutesSpentFromTimeTracking = timeTracking.getTimeSpentMinutes();
        }

        return minutesSpentFromTimeTracking;
    }

    /**
     * Get the Values of a Multiple Choice Jira Field from the given Issue
     * 
     * @param issue
     * @param multipleChoiceFieldName
     * @return
     */
    public List<String> getMultipleChoiceFieldValues(Issue issue, String multipleChoiceFieldName) {

        List<String> defectReasonList = new ArrayList<>();

        IssueField defectReasonField = issue.getFieldByName(multipleChoiceFieldName);

        if (defectReasonField == null) {
            return defectReasonList;
        }

        JSONArray defectReasonArray;

        try {

            if (defectReasonField.getValue() != null) {

                defectReasonArray = (JSONArray) defectReasonField.getValue();

                for (int i = 0; i < defectReasonArray.length(); i++) {
                    defectReasonList.add(defectReasonArray.getJSONObject(i).getString("value"));
                }

            }

            return defectReasonList;

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while reading Defect Reason from Issue: " + issue.getKey(), e);
        }

    }

    /**
     * Get the Values of a Multi Value free Tag Field Jira Field from the given Issue
     * 
     * @param issue
     * @param multiValueFreeTagFieldName
     * @return
     */
    public List<String> getMultiValueFreeTagFieldValues(Issue issue, String multiValueFreeTagFieldName) {

        List<String> defectReasonInfoList = new ArrayList<>();

        IssueField defectReasonInfoField = issue.getFieldByName(multiValueFreeTagFieldName);

        JSONArray defectReasonInfoArray;

        if (defectReasonInfoField == null) {
            return defectReasonInfoList;
        }

        try {

            if (defectReasonInfoField.getValue() != null) {

                defectReasonInfoArray = (JSONArray) defectReasonInfoField.getValue();

                for (int i = 0; i < defectReasonInfoArray.length(); i++) {
                    defectReasonInfoList.add(defectReasonInfoArray.getString(i));
                }

            }

            return defectReasonInfoList;

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while reading Defect Reason Info from Issue: " + issue.getKey(), e);
        }

    }

    /**
     * Return the Issue Type of the Parent Issue of Sub Issues. Will be an empty String if the Issue is no Sub Issue.
     * 
     * @param issue
     * @return
     * @throws JSONException
     */
    public String getParentIssueTypeName(Issue issue) throws JSONException {

        JSONObject parentObject;
        JSONObject parentIssueFields;
        JSONObject parentIssueType;

        if (issue.getFieldByName("Parent") == null) {
            return "";
        }

        parentObject = (JSONObject) issue.getFieldByName("Parent").getValue();

        parentIssueFields = parentObject.getJSONObject("fields");

        parentIssueType = parentIssueFields.getJSONObject("issuetype");

        return parentIssueType.getString("name");
    }

}
