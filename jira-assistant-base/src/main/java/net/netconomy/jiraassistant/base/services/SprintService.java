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
package net.netconomy.jiraassistant.base.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.sun.istack.Nullable;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataFull;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataLight;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.restclient.JiraAgileRestService;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;

@Service
public class SprintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintService.class);

    @Autowired
    JiraSearch search;

    @Autowired
    FieldReaderService reader;

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    JiraAgileRestService agileRestService;

    /**
     * Sets the given Value in SprintData if the Value is not null.
     * 
     * @param sprintData
     * @param fieldName
     * @param value
     */
    private void setValueInSprintData(SprintData sprintData, String fieldName, String value) {

        if ("<null>".equals(value)) {
            return;
        }

        switch (fieldName) {

        case "id":
            sprintData.setId(Integer.parseInt(value));
            break;
        case "rapidViewId":
            sprintData.setRapidViewId(Integer.parseInt(value));
            break;
        case "name":
            sprintData.setName(value);
            break;
        case "state":
            sprintData.setState(value);
            break;
        case "startDate":
            sprintData.setStartDate(DateTime.parse(value).toString());
            break;
        case "endDate":
            sprintData.setEndDate(DateTime.parse(value).toString());
            break;
        case "completeDate":
            sprintData.setCompleteDate(DateTime.parse(value).toString());
            break;
        default:
            LOGGER.debug("Unknown Token in Sprintstring. Field Name: '{}' Value: '{}'", fieldName, value);

        }

    }

    /**
     * Reads the given Sprint-String into a SprintMetaData Object. The Format for Sprint-String is
     * Text[rapidViewId=[valid Integer],state=[String],name=[String],startDate=[valid DateTime or null], endDate=[valid
     * DateTime or null],completeDate=[valid DateTime or null],id=[valid Integer]].
     * 
     * @param sprintString
     */
    public SprintData parseSprintString(String sprintString) {
        System.err.println("--------------------");
        System.err.println(sprintString);
        System.err.println("--------------------");
        SprintData sprintData = new SprintData();
        String tempString = null;
        String[] snipets = null;
        String[] pair = null;
        String name = null;
        String value = null;
		String equals = "=";
		String separator = ",";

        tempString = sprintString.substring(sprintString.indexOf('[') + 1, sprintString.lastIndexOf(']'));

        snipets = tempString.split(separator);

        for (String singleSnipet : snipets) {
			
			//Skip Snipets without an equals, to avoid null pointers
			if(!singleSnipet.contains(equals)) {
                continue;
			}
			
            pair = singleSnipet.split(equals);
			if (pair.length > 1) {
                name = pair[0];
                value = pair[1];

                setValueInSprintData(sprintData, name, value);
            }
        }

        return sprintData;

    }

    /**
     * Get Sprint MetaData From Issue
     * 
     * @param issue
     *            the Issue to read from
     * @param sprintIdentifier
     *            the ID or the Name of the right Sprint
     * @param idenentifiedByID
     *            true ... when the Id of the Sprint is given as Identifier, false when the Sprint Name is given
     * @return the filled SprintMetaData Object or null when the named Sprint was not found in the given Issue
     */
    SprintData getSprintMetaDataFromIssue(Issue issue, String sprintIdentifier, Boolean idenentifiedByID) {

        String sprintString = "";
        String identifierString;

        SprintData sprintData;

        List<Object> objectList;

        objectList = reader.readFieldValue(issue, "Sprint");

        for (Object current : objectList) {
            if (current.getClass().equals(String.class)) {

                sprintString = (String) current;

                sprintData = parseSprintString(sprintString);

                if (idenentifiedByID) {
                    if (sprintData.getId().toString().equals(sprintIdentifier)) {
                        return sprintData;
                    }
                } else {
                    if (sprintData.getName().equals(sprintIdentifier)) {
                        return sprintData;
                    }
                }

            }

        }

        if (idenentifiedByID) {
            identifierString = "Sprint IDs";
        } else {
            identifierString = "Sprint Names";
        }

        throw new JiraAssistantException("The given Sprint Identifier '" + sprintIdentifier
                + "' did not fit any of the found " + identifierString + " from Issue: " + issue.getKey());

    }

    /**
     * Gets the Sprint Data from Jira. No Issue Information.
     * 
     * @param credentials
     *            the Credentials to access Jira
     * @param sprintIdentifier
     *            the ID or the Name of the right Sprint
     * @param idenentifiedByID
     *            true ... when the Id of the Sprint is given as Identifier, false when the Sprint Name is given
     * @return the Sprint Information or null if the Sprint could not be found
     */
    @Nullable
    public SprintData getSprintMetaData(ClientCredentials credentials, String sprintIdentifier, Boolean idenentifiedByID) {

        List<Issue> result;
        Issue issue;
        String jqlQuery;

        if (idenentifiedByID) {
            jqlQuery = "Sprint = " + sprintIdentifier;
        } else {
            jqlQuery = "Sprint = '" + sprintIdentifier + "'";
        }

        result = search.searchJira(credentials, jqlQuery, 1, 0, null);

        if (result.isEmpty()) {
            LOGGER.info("The Sprint {} was not found.", sprintIdentifier);
            return null;
        }

        issue = result.get(0);

        return getSprintMetaDataFromIssue(issue, sprintIdentifier, idenentifiedByID);
    }

    /**
     * Gets the Sprint Data from Jira including all Issues in this Sprint.
     * 
     * @param credentials
     *            the Credentials to access Jira
     * @param sprintIdentifier
     *            the ID or the Name of the right Sprint
     * @param idenentifiedByID
     *            true ... when the Id of the Sprint is given as Identifier, false when the Sprint Name is given
     * @param withChangelog
     *            if true the Changelog of the Issues will be included
     * @return the Sprint Information or null if the Sprint could not be found or was empty
     */
    @Nullable
    public SprintDataFull getFullSprintData(ClientCredentials credentials, String sprintIdentifier,
            Boolean idenentifiedByID, Boolean withChangelog) {

        SprintData basicSprintData;
        SprintDataFull sprintData;
        List<String> sprintIssueKeys;
        String jqlQuery;

        if (idenentifiedByID) {
            jqlQuery = "Sprint = " + sprintIdentifier;
        } else {
            jqlQuery = "Sprint = '" + sprintIdentifier + "'";
        }

        sprintIssueKeys = search.searchJiraGetAllKeys(credentials, jqlQuery);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving data of {} issues.", sprintIssueKeys.size());
        }

        basicSprintData = getSprintMetaData(credentials, sprintIdentifier, idenentifiedByID);

        if (basicSprintData == null) {
            return null;
        }

        sprintData = new SprintDataFull(basicSprintData);

        sprintData.addAllIssues(basicIssueService.getMultipleIssues(credentials, sprintIssueKeys, withChangelog)
                .values());

        return sprintData;
    }

    /**
     * This Function creates a SprintDataLight with all Information from the SprintDataFull. The Key, Issue Type,
     * Project, Status, Summary, Assignee, Resolution and FixVersions will be Default in the Light Data. If one of the
     * Defaults is null it will not be contained. For Additional Fields fill the wantedFields List with the Names of the
     * wanted Fields. When you don't want additional Fields leave it empty.
     * 
     * @param sprintDataFull
     * @param wantedFields
     * @return
     */
    public SprintDataLight getSprintDataLight(SprintDataFull sprintDataFull, List<String> wantedFields, List<Issue> issues) {

        SprintDataLight sprintDataLight = new SprintDataLight(sprintDataFull);

        sprintDataLight.setWantedFields(wantedFields);

        for (Issue currentIssue : issues) {

            sprintDataLight.addLightIssue(advancedIssueService.getIssueLight(currentIssue, wantedFields));

        }

        return sprintDataLight;
    }

    /**
     * Gets the Sprint Data from Jira including light Issue Information from this Sprint. Only the wanted Fields are
     * being exported.
     * 
     * @param credentials
     *            the Credentials to access Jira
     * @param sprintIdentifier
     *            the ID or the Name of the right Sprint
     * @param idenentifiedByID
     *            true ... when the Id of the Sprint is given as Identifier, false when the Sprint Name is given
     * @param wantedFields
     *            the Fields to be included in the light Issue Data
     * @return the Sprint Information or null if the Sprint was empty or could not be found
     */
    @Nullable
    public SprintDataLight getLightSprintData(ClientCredentials credentials, String sprintIdentifier,
            Boolean idenentifiedByID, List<String> wantedFields, List<Issue> issues) {

        SprintDataFull sprintDataFull;

        sprintDataFull = getFullSprintData(credentials, sprintIdentifier, idenentifiedByID, false);

        if (sprintDataFull != null) {
            return getSprintDataLight(sprintDataFull, wantedFields, issues);
        } else {
            return null;
        }

    }

    /**
     * Adds the Issues that were added during the Sprint according to Jira Agile to SprintDataDelta
     * 
     * @param credentials
     * @param sprintDataDelta
     */
    void addIssueKeysAddedToAgileSprint(ClientCredentials credentials, SprintDataDelta sprintDataDelta, IssueFilter issueFilter) {

        JSONObject sprintReport;
        JSONObject contentsJson;
        JSONObject addedIssueKeysJson;
        String currentKey;
        @SuppressWarnings("rawtypes")
        Iterator keyIterator;
        Integer issueCount = 0;

        try {

            sprintReport = agileRestService.getSprintReport(credentials, sprintDataDelta.getRapidViewId(),
                    sprintDataDelta.getId());

            contentsJson = sprintReport.getJSONObject("contents");

            addedIssueKeysJson = contentsJson.getJSONObject("issueKeysAddedDuringSprint");

            keyIterator = addedIssueKeysJson.keys();

            while (keyIterator.hasNext()) {
                currentKey = keyIterator.next().toString();
                if (!this.basicIssueService.isIncluded(currentKey, issueFilter)) {
                    continue;
                }

                sprintDataDelta.addIssueKeyToAddedIssues(currentKey);

                issueCount++;

            }

            sprintDataDelta.setAddedIssues(issueCount);

        } catch (JSONException e) {
            throw new JiraAssistantException("Error during the retrieval of removed Issues.", e);
        }

    }

    /**
     * Adds the Issues that were removed during the Sprint according to Jira Agile to SprintDataDelta
     * 
     * @param credentials
     * @param sprintDataDelta
     */
    void addIssuesRemovedFromAgileSprint(ClientCredentials credentials, SprintDataDelta sprintDataDelta, IssueFilter issueFilter) {

        JSONObject sprintReport;
        JSONObject contentsJson;
        JSONArray removedIssuesJson;
        JSONObject currentIssueJson;
        List<String> removedIssueKeys = new ArrayList<>();
        Integer issueCount = 0;

        try {

            sprintReport = agileRestService.getSprintReport(credentials, sprintDataDelta.getRapidViewId(),
                    sprintDataDelta.getId());

            contentsJson = sprintReport.getJSONObject("contents");

            removedIssuesJson = contentsJson.getJSONArray("puntedIssues");

            for (int i = 0; i < removedIssuesJson.length(); i++) {

                currentIssueJson = removedIssuesJson.getJSONObject(i);
                String key = currentIssueJson.getString("key");
                if (!this.basicIssueService.isIncluded(key, issueFilter)) {
                    continue;
                }
                removedIssueKeys.add(key);

                issueCount++;

            }

            sprintDataDelta.addAllIssueKeyToRemovedIssues(removedIssueKeys);

            sprintDataDelta.setRemovedIssues(issueCount);

        } catch (JSONException e) {
            throw new JiraAssistantException("Error during the retrieval of removed Issues.", e);
        }

    }

    Double calculateStoryPoints(ClientCredentials credentials, List<String> issueKeysToCalculate,
            String estimationFieldName) {

        Double storyPointSum = 0.0;
        Map<String, Issue> issueMap;

        issueMap = basicIssueService.getMultipleIssues(credentials, issueKeysToCalculate, false);

        for (Issue currentIssue : issueMap.values()) {

            storyPointSum += advancedIssueService.getEstimation(currentIssue, estimationFieldName);

        }

        return storyPointSum;

    }

    /**
     * removes the false positives in the "removed Issues during Sprint" from SprintDataDelta based on Issue History
     * Data
     * 
     * @param sprintData
     * @param sprintDataDelta
     */
    void correctIssuesRemovedFromSprint(SprintDataFull sprintData, SprintDataDelta sprintDataDelta, IssueFilter issueFilter) {
        
        Issue currentIssue;
        List<String> removedKeysCorrect = new ArrayList<>();
        
        for (String currentKey : sprintDataDelta.getRemovedIssueKeys()) {
            if (!this.basicIssueService.isIncluded(currentKey, issueFilter)) {
                continue;
            }
            
            currentIssue = sprintData.getIssueByKey(currentKey);
            
            // if the currentIssue is found in the SprintDataFull it was not really removed from the Sprint
            if (currentIssue != null) {
                continue;
            } else {
                removedKeysCorrect.add(currentKey);
            }

        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Issues removed from Sprint: '{}' before correction: {}, after correction {}.",
                    sprintData.getName(), sprintDataDelta.getRemovedIssues(), removedKeysCorrect.size());
        }
        
        sprintDataDelta.setRemovedIssueKeys(removedKeysCorrect);
        sprintDataDelta.setRemovedIssues(removedKeysCorrect.size());

    }

    /**
     * This function creates a SprintDataDelta with all meta information about a sprint from the SprintDataFull. The
     * SprintDataDelta will contain a list of keys of issues that were added or removed after the sprint was
     * started. It contains no issue information of added issues.
     * 
     * @param credentials
     * @param sprintData
     *            has to be Sprint Data with
     * @param estimationFieldName
     * @return
     */
    public SprintDataDelta getSprintDataDelta(ClientCredentials credentials, SprintDataFull sprintData,
            String estimationFieldName, IssueFilter issueFilter) {

        SprintDataDelta sprintDataDelta;

        sprintDataDelta = new SprintDataDelta(sprintData);

        if (sprintDataDelta.getRapidViewId() == null) {
            LOGGER.warn("The RapidView ID was null for Sprint {} with ID {} so no Sprint Delta could be calculated.",
                    sprintData.getName(), sprintData.getId());

            return sprintDataDelta;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Trying to retrieve Sprint Delta Data for Sprint {} with ID {}", sprintData.getName(), sprintData.getId());
        }

        addIssueKeysAddedToAgileSprint(credentials, sprintDataDelta, issueFilter);

        addIssuesRemovedFromAgileSprint(credentials, sprintDataDelta, issueFilter);

        correctIssuesRemovedFromSprint(sprintData, sprintDataDelta, issueFilter);

        sprintDataDelta.setAddedStoryPoints(calculateStoryPoints(credentials, sprintDataDelta.getAddedIssueKeys(),
                estimationFieldName));

        sprintDataDelta.setRemovedStoryPoints(calculateStoryPoints(credentials, sprintDataDelta.getRemovedIssueKeys(),
                estimationFieldName));

        return sprintDataDelta;

    }

    /**
     * The SprintDataDelta will contain a List of IssueKeys of Issues that were added or removed after the Sprint was
     * started. It contains no Issue Information of added Issues.
     * 
     * @param credentials
     * @param sprintIdentifier
     * @param idenentifiedByID
     * @param estimationFieldName
     * @return SprintDataDelta or null if the Sprint was not found or was empty
     */
    @Nullable
    public SprintDataDelta getSprintDataDelta(ClientCredentials credentials, String sprintIdentifier,
            Boolean idenentifiedByID, String estimationFieldName, IssueFilter issueFilter) {

        SprintDataFull sprintData;
        SprintDataDelta sprintDataDelta;

        sprintData = getFullSprintData(credentials, sprintIdentifier, idenentifiedByID, true);

        if (sprintData == null) {
            return null;
        }

        sprintDataDelta = getSprintDataDelta(credentials, sprintData, estimationFieldName, issueFilter);

        return sprintDataDelta;

    }

}
