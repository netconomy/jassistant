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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.api.domain.Issue;

public interface JsonToIssueConverter {

    /**
     * This Function converts a JSONObject into an Issue, it takes additional Fields an Expands into Account
     * 
     * @param jsonIssue
     * @param additionalFields
     * @param expand
     * @return
     * @throws JSONException
     */
    public Issue convertJsonObjectToIssue(JSONObject jsonIssue, String additionalFields, String expand)
            throws JSONException;

}
