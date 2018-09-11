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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class RestConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestConnector.class);

    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    private RestTemplate getRestTemplate(ClientCredentials clientCredentials) {

        String credentialsString;
        List<Header> headerList = new ArrayList<Header>();
        HttpClient httpClient;
        ClientHttpRequestFactory requestFactory;
        RestTemplate restTemplate;

        credentialsString = clientCredentials.getUserName() + ":" + clientCredentials.getPassword();

        // Add the Basic Authentication Header to the Client Headers
        headerList.add(new BasicHeader("Authorization", "Basic "
                + Base64.encodeBase64String(credentialsString.getBytes())));

        httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();

        requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        restTemplate = new RestTemplate(requestFactory);

        return restTemplate;

    }

    /**
     * Gets a JSONObject for a REST Call
     * 
     * @param clientCredentials
     * @param restUrl
     *            the complete URL of the REST Call
     * @return
     */
    public JSONObject getJsonFromRest(ClientCredentials clientCredentials, String restUrl) {

        RestTemplate restTemplate;
        String response;

        try {

            restTemplate = getRestTemplate(clientCredentials);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calling REST API with URL: {}", restUrl);
            }

            response = restTemplate.getForObject(restUrl, String.class);

            JSONObject jsonObject = new JSONObject(response);

            return jsonObject;

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while Connection to REST API with Url: " + restUrl, e);
        }

    }

    /**
     * Gets a JSONArray for a REST Call
     * 
     * @param clientCredentials
     * @param restUrl
     *            the complete URL of the REST Call
     * @return
     */
    public JSONArray getJsonArrayFromRest(ClientCredentials clientCredentials, String restUrl) {

        RestTemplate restTemplate;
        String response;

        try {

            restTemplate = getRestTemplate(clientCredentials);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calling REST API with URL: {}", restUrl);
            }

            response = restTemplate.getForObject(restUrl, String.class);

            JSONArray jsonArray = new JSONArray(response);

            return jsonArray;

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while Connection to REST API with Url: " + restUrl, e);
        }

    }

    /**
     * Sends a JSON Body via the given HTTP Method to the given URL
     *
     * @param clientCredentials
     * @param restUrl
     * @param body
     * @param httpMethod
     * @return
     */
    private ResponseEntity<String> jsonToRest(ClientCredentials clientCredentials, String restUrl, JSONObject body,
                                              HttpMethod httpMethod) {
        RestTemplate restTemplate;
        ResponseEntity<String> responseEntity;

        HttpHeaders requestHeaders = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(mediaTypeList);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(body.toString(), requestHeaders);

        restTemplate = getRestTemplate(clientCredentials);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling REST API with {} to URL: {} and body : {}", httpMethod.toString(), restUrl, body.toString());
        }

        responseEntity = restTemplate.exchange(restUrl, httpMethod, requestEntity, String.class);

        return responseEntity;

    }

    /**
     * Sends a JSON Body via PUT to the given URL
     *
     * @param clientCredentials
     * @param restUrl
     * @param body
     * @return
     */
    public ResponseEntity<String> putJsonToRest(ClientCredentials clientCredentials, String restUrl, JSONObject body) {

        return jsonToRest(clientCredentials, restUrl, body, HttpMethod.PUT);

    }

    /**
     * Sends a JSON Body via POST to the given URL
     *
     * @param clientCredentials
     * @param restUrl
     * @param body
     * @return
     */
    public ResponseEntity<String> postJsonToRest(ClientCredentials clientCredentials, String restUrl, JSONObject body) {

        return jsonToRest(clientCredentials, restUrl, body, HttpMethod.POST);

    }

}
