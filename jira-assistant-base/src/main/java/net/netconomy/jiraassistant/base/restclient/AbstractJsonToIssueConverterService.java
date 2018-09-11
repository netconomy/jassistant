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
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.ChangelogItem;
import com.atlassian.jira.rest.client.api.domain.FieldType;
import com.atlassian.jira.rest.client.api.domain.Issue;

public abstract class AbstractJsonToIssueConverterService implements JsonToIssueConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJsonToIssueConverterService.class);

    /**
     * This Function converts a JSONObject into an Issue, it takes additional Fields an Expands into Account
     * 
     * @param jsonIssue
     * @param additionalFields
     * @param expand
     * @return
     * @throws JSONException
     */
    @Override
    public abstract Issue convertJsonObjectToIssue(JSONObject jsonIssue, String additionalFields, String expand)
            throws JSONException;

    protected Iterable<ChangelogItem> getChangeLogItemsFromJsonArray(JSONArray jsonChangelogItems) throws JSONException {

        List<ChangelogItem> changeLogItems = new ArrayList<>();
        JSONObject jsonChangeLogItem;
        ChangelogItem changeLogItem;

        for (int i = 0; i < jsonChangelogItems.length(); i++) {

            jsonChangeLogItem = jsonChangelogItems.getJSONObject(i);

            changeLogItem = new ChangelogItem(
                    FieldType.valueOf(jsonChangeLogItem.getString("fieldtype").toUpperCase()),
                    jsonChangeLogItem.getString("field"), jsonChangeLogItem.getString("from"),
                    jsonChangeLogItem.getString("fromString"), jsonChangeLogItem.getString("to"),
                    jsonChangeLogItem.getString("toString"));

            changeLogItems.add(changeLogItem);

        }

        return changeLogItems;

    }

    protected Collection<ChangelogGroup> getChangeLogFromJsonObject(JSONObject jsonChangelog) throws JSONException {

        Collection<ChangelogGroup> changelogCollection = new ArrayList<>();
        JSONArray jsonHistories;
        JSONObject jsonHistoryEntry;
        ChangelogGroup changelogGroup;
        JSONObject jsonAuthor;
        BasicUser author = null;
        Iterable<ChangelogItem> changelogItems;

        jsonHistories = jsonChangelog.getJSONArray("histories");

        for (int i = 0; i < jsonHistories.length(); i++) {

            jsonHistoryEntry = jsonHistories.getJSONObject(i);

            changelogItems = getChangeLogItemsFromJsonArray(jsonHistoryEntry.getJSONArray("items"));

            try {
                jsonAuthor = jsonHistoryEntry.getJSONObject("author");
    
                author = new BasicUser(URI.create(jsonAuthor.getString("self")), jsonAuthor.getString("name"),
                        jsonAuthor.getString("displayName"));
            } catch (JSONException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No Author found for ChangeLog Item. Properbly automated Action", e);
                }
            }

            changelogGroup = new ChangelogGroup(author, DateTime.parse(jsonHistoryEntry.getString("created")),
                    changelogItems);

            changelogCollection.add(changelogGroup);

        }

        return changelogCollection;

    }

}
