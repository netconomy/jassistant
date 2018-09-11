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
import java.util.List;

import net.netconomy.jiraassistant.base.JiraAssistantException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

/**
 * This Class reads Field Values from Jira Issues into a List.
 * 
 * @author mweilbuchner
 *
 */
@Service
public class FieldReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldReaderService.class);

    /**
     * Reads the the Value from the JSON Object of the named Field and returns the Value(s)
     * 
     * @param issue
     *            the Issue to read
     * @param fieldName
     *            the name of the Field to Read
     * @return a List of Objects or an empty List if there was no Value
     */
    public List<Object> readFieldValue(Issue issue, String fieldName) {

        IssueField issueField = issue.getFieldByName(fieldName);
        JSONArray jsonArray = null;
        List<Object> result = new ArrayList<>();

        if (issueField == null) {
            throw new JiraAssistantException("Field with Name '" + fieldName + "' does not exist.");
        }

        if (issueField.getValue() == null) {
            return result;
        }

        if (issueField.getValue().getClass().equals(JSONArray.class)) {
            jsonArray = (JSONArray) issueField.getValue();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    result.add(jsonArray.get(i));
                } catch (JSONException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        } else {
            result.add(issueField.getValue());
        }

        return result;
    }

}
