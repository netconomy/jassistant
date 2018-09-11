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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.rapidviewinformation.RapidViewData;
import net.netconomy.jiraassistant.base.data.rapidviewinformation.RapidViewDataCollection;
import net.netconomy.jiraassistant.base.data.rapidviewinformation.SprintDataCollection;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.restclient.JiraAgileRestService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;

@Service
public class RapidViewInformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidViewInformationService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    JiraAgileRestService jiraAgileRestService;

    @Autowired
    SprintService sprintService;

    /**
     * Retrieves basic Information about all existing and viewable Rapidviews.
     * 
     * @param clientCredentials
     * @return
     * @throws JSONException
     */
    public RapidViewDataCollection getAllRapidViews(ClientCredentials clientCredentials) throws JSONException {

        RapidViewDataCollection rapidViewDataCollection = new RapidViewDataCollection();
        JSONArray rapidViews = jiraAgileRestService.getAllRapidViewsLight(clientCredentials);
        JSONObject currentView;
        Integer currentId;
        String currentName;
        Boolean sprintSupportEnabled;
        
        for(int i=0;i<rapidViews.length();i++) {
            currentView = rapidViews.optJSONObject(i);
            
            if(currentView != null) {
                currentId = currentView.getInt("id");
                currentName = currentView.getString("name");
                sprintSupportEnabled = currentView.getBoolean("sprintSupportEnabled");
                
                rapidViewDataCollection.addRapidView(currentId, currentName, sprintSupportEnabled);
            }
            
        }
        
        return rapidViewDataCollection;

    }

    /**
     * Retrieves basic Information about all existing and viewable Rapidviews. If withSprintSupportEnabled is true only
     * Scrum Boards will be returned.
     * 
     * @param clientCredentials
     * @param withSprintSupportEnabled
     * @return
     * @throws JSONException
     */
    public RapidViewDataCollection getAllRapidViews(ClientCredentials clientCredentials,
            Boolean withSprintSupportEnabled) throws JSONException {

        RapidViewDataCollection filteredRapidViewDataCollection = new RapidViewDataCollection();
        RapidViewDataCollection allRapidViewDataCollection;

        allRapidViewDataCollection = getAllRapidViews(clientCredentials);

        for (RapidViewData currentRapidView : allRapidViewDataCollection.getRapidViewList()) {

            if (currentRapidView.getSprintSupportEnabled().equals(withSprintSupportEnabled)) {
                filteredRapidViewDataCollection.addRapidView(currentRapidView);
            }

        }

        return filteredRapidViewDataCollection;

    }

    /**
     * Retrieves all IDs of the Sprints accessible through the given Rapid Board.
     * 
     * @param clientCredentials
     * @param rapidViewIdString
     * @return
     * @throws JSONException
     */
    public List<Integer> getAllSprintIdsForRapidView(ClientCredentials clientCredentials, String rapidViewIdString)
            throws JSONException {

        List<Integer> sprintIdList = new ArrayList<>();
        JSONArray sprintArray;
        JSONObject currentSprint;
        Integer currentId;
        Integer rapidViewId;

        rapidViewId = Integer.valueOf(rapidViewIdString);

        sprintArray = jiraAgileRestService.getAllSprintsForBoard(clientCredentials, rapidViewId);

        for (int i = 0; i < sprintArray.length(); i++) {
            currentSprint = sprintArray.optJSONObject(i);

            if (currentSprint != null) {
                currentId = currentSprint.getInt("id");

                sprintIdList.add(currentId);
            }

        }

        return sprintIdList;

    }
    
    /**
     * Retrieves basic Information about all Sprints accessible through the given Rapid Board.
     * 
     * @param clientCredentials
     * @param rapidViewIdString
     * @return
     * @throws JSONException
     */
    public SprintDataCollection getSprintsForRapidView(ClientCredentials clientCredentials, String rapidViewIdString)
            throws JSONException {

        SprintDataCollection sprintDataCollection = new SprintDataCollection();
        List<Integer> sprintIdList;
        SprintData currentSprint;

        sprintIdList = getAllSprintIdsForRapidView(clientCredentials, rapidViewIdString);

        for (Integer currentId : sprintIdList) {
            currentSprint = sprintService.getSprintMetaData(clientCredentials, currentId.toString(), true);

            if (currentSprint == null) {
                LOGGER.debug("The Sprint with the ID {} containes no Issues and could not be analysed.", currentId);
                continue;
            }

            sprintDataCollection.addSprintData(currentSprint);
        }

        return sprintDataCollection;

    }

    /**
     * Retrieves basic Information about all Sprints completed after the given Date accessible through the given Rapid
     * Board.
     * 
     * @param clientCredentials
     * @param rapidViewIdString
     * @param startDateString
     * @return
     * @throws JSONException
     */
    public SprintDataCollection getSprintsForRapidViewCompletedAfterDate(ClientCredentials clientCredentials,
            String rapidViewIdString, String startDateString) throws JSONException {

        SprintDataCollection filteredSprintDataCollection = new SprintDataCollection();
        SprintDataCollection allSprintDataCollection;
        DateTime startDate;
        
        startDate = DateTime.parse(startDateString);
        
        allSprintDataCollection = getSprintsForRapidView(clientCredentials, rapidViewIdString);

        for (SprintData currentSprint : allSprintDataCollection.getSprintDataList()) {

            if (currentSprint != null && currentSprint.getCompleteDate() != null
                    && currentSprint.getCompleteDateAsDateTime().isAfter(startDate)) {
                filteredSprintDataCollection.addSprintData(currentSprint);
            }
        }

        return filteredSprintDataCollection;

    }

}
