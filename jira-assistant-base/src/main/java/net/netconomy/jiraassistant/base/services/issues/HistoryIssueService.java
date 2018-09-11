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
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.ChangelogItem;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class HistoryIssueService {

    @Autowired
    BasicIssueService basicIssueService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryIssueService.class);

    /**
     * Get a List of ChangeLog Entries for the given Issue
     * 
     * @param credential
     * @param issueKey
     * @return
     */
    public List<ChangelogGroup> getChangelogForIssue(ClientCredentials credential, String issueKey) {

        List<ChangelogGroup> changeLog = new ArrayList<>();

        Issue issue = basicIssueService.getIssue(credential, issueKey, true);

        Iterable<ChangelogGroup> changeLogEntries = issue.getChangelog();

        if (changeLogEntries != null) {
            for (ChangelogGroup currentgroup : changeLogEntries) {
                changeLog.add(currentgroup);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Changelog of Issue {} was null.", issueKey);
            }
        }

        return changeLog;
    }

    /**
     * Get the Value of a Field of an Issue at the given point in Time. If there was no Value Change found, null will be
     * returned. The given Issue has to have an expanded Changelog, or else an exception will be thrown. <b>Warning:</b> The
     * fieldName is case sensitive.
     * 
     * @param issue
     * @param fieldName
     * @param time
     * @return
     */
    private String getFieldValueAtTimeFromChangelogEntries(Issue issue, String fieldName, DateTime time) {

        String valueAtTime = null;
        Iterable<ChangelogGroup> changeLogEntries;
        DateTime currentClosestChangeDate = null;
        DateTime creationDate;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                // Is the Entry relevant for the given Field?
                if (!currentItem.getField().equals(fieldName)) {
                    continue;
                }

                // If the happened Status Changes occurred after the given Time
                // the original Status is taken
                if (valueAtTime == null) {
                    currentClosestChangeDate = issue.getCreationDate();

                    if (currentItem.getFromString() == null) {
                        valueAtTime = "";
                    } else {
                        valueAtTime = currentItem.getFromString();
                    }
                }

                // Did this Change happen after the current Change but before
                // the given Time?
                if (creationDate.isBefore(time) && creationDate.isAfter(currentClosestChangeDate)) {
                    currentClosestChangeDate = creationDate;

                    valueAtTime = currentItem.getToString();

                }

            }
        }

        return valueAtTime;

    }

    /**
     * Get the Value of a Field of an Issue at the given point in Time. If there was no Value Change found, the current
     * Value will be returned. The given Issue has to have an expanded Changelog, or else an exception will be thrown. <b>Warning:</b> The
     * fieldName is case sensitive.
     * 
     * @param issue
     * @param fieldName
     * @param time
     * @return
     */
    public String getFieldValueAtTime(Issue issue, String fieldName, DateTime time) {

        String valueAtTime;

        // Get the Value out of the History if it was changed
        valueAtTime = getFieldValueAtTimeFromChangelogEntries(issue, fieldName, time);

        // when there is no Value Change in the History, the current Value is
        // the Value at the given Time
        if (valueAtTime == null) {
            if (issue.getFieldByName(fieldName) == null) {
                throw new JiraAssistantException("A Field with the name '" + fieldName
                        + "' does not exist. Be careful field names are case sensitive in Jira.");
            }

            if (issue.getFieldByName(fieldName).getValue() == null) {
                valueAtTime = "";
            } else {
                valueAtTime = issue.getFieldByName(fieldName).getValue().toString();
            }
        }

        return valueAtTime;
    }

    /**
     * Get the Status of an Issue at the given point in Time. If there was no Status Change found, the current Status
     * will be returned. The given Issue has to have an expanded Changelog, or else an exception will be thrown.
     * 
     * @param issue
     * @param time
     * @return
     */
    public String getStatusAtTime(Issue issue, DateTime time) {

        String statusAtTime = "";
        Iterable<ChangelogGroup> changeLogEntries;
        DateTime currentClosestChangeDate = null;
        DateTime creationDate;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                if (!"status".equals(currentItem.getField())) {
                    continue;
                }

                // If the happened Status Changes occurred after the given Time
                // the original Status is taken
                if (statusAtTime.isEmpty()) {
                    currentClosestChangeDate = issue.getCreationDate();
                    statusAtTime = currentItem.getFromString();
                }

                // Did this Change happen after the current Change but before
                // the given Time?
                if (creationDate.isBefore(time) && creationDate.isAfter(currentClosestChangeDate)) {
                    currentClosestChangeDate = creationDate;

                    statusAtTime = currentItem.getToString();

                }

            }
        }

        // when there is no Status related Change in the History, the current
        // status is the Status at the given Time
        if (statusAtTime.isEmpty()) {
            statusAtTime = issue.getStatus().getName();
        }

        return statusAtTime;
    }

    /**
     * Get the Status of an Issue at the given point in Time. If there was no Status Change found, the current Status
     * will be returned.
     * 
     * @param credentials
     * @param issueKey
     * @param time
     * @return
     */
    public String getStatusAtTime(ClientCredentials credentials, String issueKey, DateTime time) {

        Issue issue = basicIssueService.getIssue(credentials, issueKey, true);

        String statusAtTime = getStatusAtTime(issue, time);

        return statusAtTime;

    }

    /**
     * Get the Count how often the given Field of the given Issue was set to the given Value during the given Time
     * Window.
     * 
     * @param issue
     * @param fieldName
     * @param fieldValue
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getValueCountForIssueField(Issue issue, String fieldName, String fieldValue, DateTime startDate,
            DateTime endDate) {

        Integer valueCount = 0;

        Iterable<ChangelogGroup> changeLogEntries;
        DateTime creationDate;
        String currentField = "";
        String currentToString = "";

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                currentField = currentItem.getField();
                currentToString = currentItem.getToString();

                // Only Changes to the stated Field and into the stated Value are counted
                if (!(currentField.equalsIgnoreCase(fieldName) && currentToString.equalsIgnoreCase(fieldValue))) {
                    continue;
                }

                // Did this Change happen during the given Period?
                if ((creationDate.isAfter(startDate) || creationDate.isEqual(startDate)) && creationDate.isBefore(endDate)) {
                    valueCount++;
                }

            }
        }

        return valueCount;

    }

    /**
     * Get the Count how often the given Issue was set to the State with the given Name during the given Time.
     * 
     * @param issue
     * @param stateName
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getStateCountForIssue(Issue issue, String stateName, DateTime startDate, DateTime endDate) {

        return getValueCountForIssueField(issue, "status", stateName, startDate, endDate);
    }

    /**
     * Get the Count how often Issues from the given List were set to the State with the given Name during the given
     * Time.
     * 
     * @param issueList
     * @param stateName
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getStateCountForIssues(List<Issue> issueList, String stateName, DateTime startDate, DateTime endDate) {

        Integer reopenCount = 0;

        for (Issue currentIssue : issueList) {
            reopenCount += getStateCountForIssue(currentIssue, stateName, startDate, endDate);
        }

        return reopenCount;
    }

    /**
     * Get the Duration how long the given Field of the given Issue was set to the given Value during the given Time
     * Window.
     * 
     * @param issue
     * @param fieldName
     * @param fieldValue
     * @param startDate
     * @param endDate
     * @return
     */
    public Duration getValueDurationForIssueField(Issue issue, String fieldName, String fieldValue, DateTime startDate,
            DateTime endDate) {

        Duration valueDuration = new Duration(0);

        DateTime currentStart = startDate;
        Boolean valueActive = false;

        Iterable<ChangelogGroup> changeLogEntries;
        DateTime creationDate;
        String currentToString;
        String currentFromString;
        String valueAtStart;
        String valueAtEnd;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        if ("status".equals(fieldName)) {
            valueAtStart = getStatusAtTime(issue, startDate);
            valueAtEnd = getStatusAtTime(issue, endDate);
        } else {
            valueAtStart = getFieldValueAtTime(issue, fieldName, startDate);
            valueAtEnd = getFieldValueAtTime(issue, fieldName, endDate);
        }

        // Field was set at the Start of the given Period
        valueActive = valueAtStart.equalsIgnoreCase(fieldValue);

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            // Only Changes in the given Time Window are counted
            if (creationDate.isBefore(startDate) || creationDate.isAfter(endDate)) {
                continue;
            }

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                // Only Changes to the stated Field are counted
                if (!currentItem.getField().equalsIgnoreCase(fieldName)) {
                    continue;
                }

                currentToString = currentItem.getToString();
                currentFromString = currentItem.getFromString();
                
                if (!valueActive && currentToString.equalsIgnoreCase(fieldValue)) {
                    currentStart = creationDate;
                    valueActive = true;
                } else if (valueActive && currentFromString.equalsIgnoreCase(fieldValue)) {
                    valueDuration = valueDuration.plus(new Duration(currentStart, creationDate));
                    valueActive = false;
                }

            }
        }

        // Field was set at the End of the given Period
        if (valueActive && valueAtEnd.equalsIgnoreCase(fieldValue)) {
            valueDuration = valueDuration.plus(new Duration(currentStart, endDate));
            valueActive = false;
        }

        return valueDuration;

    }

    /**
     * is True if there is any Changelog Entry for the given Issue in the given Period
     * 
     * @param issue
     * @param startDate
     * @param endDate
     * @return
     */
    public Boolean issueWasUpdatedDuring(Issue issue, DateTime startDate, DateTime endDate) {

        Iterable<ChangelogGroup> changeLogEntries;
        DateTime creationDate;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            // Did this Change happen during the given Period?
            if (creationDate.isAfter(startDate) && creationDate.isBefore(endDate)) {
                return true;
            }
        }

        return false;

    }

    /**
     * is True if there is any Changelog Entry for the given Issue in the given Period
     * 
     * @param credentials
     * @param issueKey
     * @param startDate
     * @param endDate
     * @return
     */
    public Boolean issueWasUpdatedDuring(ClientCredentials credentials, String issueKey, DateTime startDate,
            DateTime endDate) {

        Issue issue = basicIssueService.getIssue(credentials, issueKey, true);

        return issueWasUpdatedDuring(issue, startDate, endDate);

    }

    /**
     * Returns the DateTime of the last Time a given Field was changed. If the Field was never changed the CreationDate
     * of the Issue is returned.
     * 
     * @param issue
     * @param fieldName
     * @return
     */
    public DateTime getDateTimeOfLastFieldChange(Issue issue, String fieldName) {

        Iterable<ChangelogGroup> changeLogEntries;
        DateTime currentLatestChangeDate = issue.getCreationDate();
        DateTime creationDate;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                // Is the Entry relevant for the given Field?
                if (!currentItem.getField().equals(fieldName)) {
                    continue;
                }

                currentLatestChangeDate = creationDate;

            }
        }

        return currentLatestChangeDate;

    }

    /**
     * Returns the DateTime of the first Time a given Issue was set into a Status from the given List after the given Date.
     * If the Issue was never Set into a Status from the List after the given Date it returns null. If the Date is null
     * this Function will just give you the first Time the Issue was in one of the given Status.
     *
     * @param issue
     * @param statusList
     * @param date
     * @return
     */
    public DateTime getDateTimeOfFirstStatusInSpecifiedListAfter(Issue issue, List<String> statusList, DateTime date) {

        Iterable<ChangelogGroup> changeLogEntries;
        DateTime creationDate;

        changeLogEntries = issue.getChangelog();

        if (changeLogEntries == null) {
            throw new JiraAssistantException("The given Issue had no expanded ChangeLog.");
        }

        for (ChangelogGroup currentGroup : changeLogEntries) {

            creationDate = currentGroup.getCreated();

            if (date != null && creationDate.isBefore(date)) {
                continue;
            }

            for (ChangelogItem currentItem : currentGroup.getItems()) {

                if (!"status".equals(currentItem.getField())) {
                    continue;
                }

                if (statusList.contains(currentItem.getToString())) {
                    return creationDate;
                }

            }
        }

        return null;

    }

    /**
     * Returns the DateTime of the first Time a given Issue was set into a Status from the given List. If the Issue was
     * never Set into a Status from the List it returns null.
     * 
     * @param issue
     * @param statusList
     * @return
     */
    public DateTime getDateTimeOfFirstStatusInSpecifiedList(Issue issue, List<String> statusList) {

        return getDateTimeOfFirstStatusInSpecifiedListAfter(issue, statusList, null);
    }

    /**
     * Get the Resolution Date for an Issue or null if it is not resolved.
     * 
     * @param issue
     * @return
     */
    public DateTime getResolutionDate(Issue issue, String fieldName) {

        DateTime resolutionDate;
        IssueField resolutionDateField;
        Object fieldValue;

        resolutionDateField = issue.getFieldByName(fieldName);

        if (resolutionDateField != null) {

            fieldValue = resolutionDateField.getValue();

            if (fieldValue != null) {
                resolutionDate = DateTime.parse(fieldValue.toString());

                return resolutionDate;
            }

        }

        return null;

    }

}
