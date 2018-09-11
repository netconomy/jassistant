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
package net.netconomy.jiraassistant.restservices.services;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.util.ErrorCollection;

import net.netconomy.jiraassistant.base.output.JsonOutput;

@Service
public class JsonAnswerWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAnswerWrapper.class);

    @Autowired
    JsonOutput jsonOutput;

    private String wrapJsonRest(String jsonData, Integer errorStatus, String errorMessage) {

        JSONObject wrappedJson;
        JSONObject dataJson;
        JSONObject errorJson;

        try {

            wrappedJson = new JSONObject();
            dataJson = new JSONObject(jsonData);
            errorJson = new JSONObject();

            if (errorMessage == null || errorMessage.isEmpty()) {
                errorJson.put("errors", false);
            } else {
                errorJson.put("errors", true);

                if (errorStatus != null) {
                    errorJson.put("status", errorStatus);
                }

                errorJson.put("message", errorMessage);
            }

            wrappedJson.put("data", dataJson);
            wrappedJson.put("error", errorJson);

            return wrappedJson.toString();

        } catch (JSONException e) {

            LOGGER.error(e.getMessage(), e);
            return "{'data': {},'error': {'errors': 'true','message': '" + e.getMessage() + "'}}";

        }

    }

    /**
     * Wrap the JSON Data for Output to the REST API, so that Errors are listed and can be read by the GUI.
     * 
     * @param jsonData
     * @param exception
     * @return
     */
    public String wrapJsonForRest(String jsonData, Exception exception) {

        Integer errorStatus;
        String errorMessage;
        RestClientException restClientException;
        ErrorCollection collection;

        if (exception == null) {
            errorStatus = null;
            errorMessage = null;
        } else {

            if (exception instanceof RestClientException) {

                restClientException = (RestClientException) exception;

                collection = restClientException.getErrorCollections().iterator().next();

                errorStatus = collection.getStatus();
                errorMessage = collection.getErrorMessages().iterator().next();

            } else {
                errorStatus = null;
                errorMessage = exception.getMessage();
            }
        }

        return wrapJsonRest(jsonData, errorStatus, errorMessage);

    }

    /**
     * Converts the given Object to JSON and wraps it so it can be put out through REST
     * 
     * @param object
     * @return
     */
    public String wrapObjectForJSONOutput(Object object) {

        String jsonDataString;
        String wrappedJsonString;

        jsonDataString = jsonOutput.convertObjectToJSONString(object);

        wrappedJsonString = wrapJsonForRest(jsonDataString, null);

        return wrappedJsonString;

    }

}
