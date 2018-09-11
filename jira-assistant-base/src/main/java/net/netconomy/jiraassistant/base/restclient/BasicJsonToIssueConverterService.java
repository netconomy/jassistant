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
package net.netconomy.jiraassistant.base.restclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Attachment;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicPriority;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.BasicWatchers;
import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Operations;
import com.atlassian.jira.rest.client.api.domain.Resolution;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.atlassian.jira.rest.client.api.domain.TimeTracking;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.Worklog;

@Service
public class BasicJsonToIssueConverterService extends AbstractJsonToIssueConverterService {

    @Override
    public Issue convertJsonObjectToIssue(JSONObject jsonIssue, String additionalFields, String expand)
            throws JSONException {

        Issue issue;
        JSONObject jsonFields;
        JSONObject jsonProject;
        BasicProject project;
        BasicPriority priority;
        JSONObject jsonIssueType;
        IssueType issueType;
        JSONObject jsonStatus;
        JSONObject jsonPriority;
        Status status;
        Collection<Attachment> attachments = new ArrayList<>();
        Collection<Version> affectedVersions = new ArrayList<>();
        Collection<Version> fixVersions = new ArrayList<>();
        Collection<BasicComponent> components = new ArrayList<>();
        Collection<IssueField> issueFields = new ArrayList<>();
        Collection<Comment> comments = new ArrayList<>();
        BasicVotes votes;
        Collection<Worklog> worklogs = new ArrayList<>();
        BasicWatchers watchers;
        Iterable<String> expandos = new ArrayList<>();
        Collection<ChangelogGroup> changelog = new ArrayList<>();
        Set<String> labels = new HashSet<>();
        String[] additionalFieldsArray;
        IssueField currentIssueField;

        String description = null;
        Resolution resolution = null;
        User reporter = null;
        User assignee = null;
        DateTime created = null;
        DateTime updateDate = null;
        DateTime dueDate = null;
        TimeTracking timeTracking = null;
        URI transitionsUri = null;
        Collection<IssueLink> issueLinks = null;
        Collection<Subtask> subtasks = null;
        Operations operations = null;

        jsonFields = jsonIssue.getJSONObject("fields");
        jsonProject = jsonFields.getJSONObject("project");
        jsonIssueType = jsonFields.getJSONObject("issuetype");
        jsonStatus = jsonFields.getJSONObject("status");
        jsonPriority = jsonFields.getJSONObject("priority");

        project = new BasicProject(URI.create(jsonProject.getString("self")), jsonProject.getString("key"),
                jsonProject.getLong("id"), jsonProject.getString("name"));

        priority = new BasicPriority(URI.create(jsonPriority.getString("self")), jsonProject.getLong("id"),
                jsonPriority.getString("name"));

        issueType = new IssueType(URI.create(jsonIssueType.getString("self")), jsonIssueType.getLong("id"),
                jsonIssueType.getString("name"), jsonIssueType.getBoolean("subtask"),
                jsonIssueType.getString("description"), URI.create(jsonIssueType.getString("iconUrl")));

        status = new Status(URI.create(jsonStatus.getString("self")), jsonStatus.getLong("id"),
                jsonStatus.getString("name"), jsonStatus.getString("description"), URI.create(jsonStatus
                        .getString("iconUrl")));

        votes = new BasicVotes(null, 0, false);

        watchers = new BasicWatchers(null, false, 0);

        // When expand is null, the Changelog was extracted
        if (expand == null || expand.contains("changelog")) {
            changelog = getChangeLogFromJsonObject(jsonIssue.getJSONObject("changelog"));
        }

        created = DateTime.parse(jsonFields.getString("created"));

        // Issue Fields
        if (additionalFields != null) {
            additionalFieldsArray = additionalFields.split(",");

            for (String currentField : additionalFieldsArray) {
                currentIssueField = new IssueField(currentField, currentField, null, jsonFields.optString(currentField));

                issueFields.add(currentIssueField);
            }
        }

        issue = new Issue(jsonFields.getString("summary"), URI.create(jsonIssue.getString("self")),
                jsonIssue.getString("key"), jsonIssue.getLong("id"), project, issueType, status, description, // null
                priority,
                resolution, // null
                attachments, reporter, // null
                assignee, // null
                created, updateDate, // null
                dueDate, // null
                affectedVersions, fixVersions, components, timeTracking, // null
                issueFields, comments, transitionsUri, // null
                issueLinks, // null
                votes, worklogs, watchers, expandos, subtasks, // null
                changelog, operations, // null
                labels);

        return issue;

    }

}
