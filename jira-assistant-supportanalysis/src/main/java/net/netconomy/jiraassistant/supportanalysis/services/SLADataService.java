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
package net.netconomy.jiraassistant.supportanalysis.services;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.IssueField;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaCycle;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaData;

@Service
public class SLADataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SLADataService.class);

    SlaCycle parseSlaCycle(JSONObject slaCycleJSON) {

        SlaCycle slaCycle = new SlaCycle();

        if (slaCycleJSON == null) {
            return null;
        }

        slaCycle.setBreached(slaCycleJSON.optBoolean("breached"));
        slaCycle.setPaused(slaCycleJSON.optBoolean("paused"));
        slaCycle.setWithinCalendarHours(slaCycleJSON.optBoolean("withinCalendarHours"));

        if (slaCycleJSON.opt("goalDuration") != null) {
            slaCycle.setGoalDurationMinutes(slaCycleJSON.optJSONObject("goalDuration").optDouble("millis") / 60000);
        }
        if (slaCycleJSON.opt("elapsedTime") != null) {
            slaCycle.setElapsedTimeMinutes(slaCycleJSON.optJSONObject("elapsedTime").optDouble("millis") / 60000);
        }
        if (slaCycleJSON.opt("remainingTime") != null) {
            slaCycle.setRemainingTimeMinutes(slaCycleJSON.optJSONObject("remainingTime").optDouble("millis") / 60000);
        }

        if (slaCycleJSON.opt("startTime") != null) {
            slaCycle.setStartTime(new DateTime(Long.parseLong(slaCycleJSON.optJSONObject("startTime").optString("epochMillis"))));
        }
        if (slaCycleJSON.opt("stopTime") != null) {
            slaCycle.setStopTime(new DateTime(Long.parseLong(slaCycleJSON.optJSONObject("stopTime").optString("epochMillis"))));
        }
        if (slaCycleJSON.opt("breachTime") != null) {
            slaCycle.setBreachTime(new DateTime(Long.parseLong(slaCycleJSON.optJSONObject("breachTime").optString("epochMillis"))));
        }

        return slaCycle;

    }

    public SlaData parseSlaField(IssueField slaField) {

        SlaData slaData = new SlaData();
        JSONObject fieldDataJSON;
        JSONArray completeCyclesJSON;
        JSONObject ongoingCycleJSON;

        if (slaField == null || slaField.getValue() == null) {
            return null;
        }

        try {
            fieldDataJSON = new JSONObject(slaField.getValue().toString());

            completeCyclesJSON = fieldDataJSON.optJSONArray("completedCycles");
            ongoingCycleJSON = fieldDataJSON.optJSONObject("ongoingCycle");

            if (completeCyclesJSON != null) {
                for (int i = 0; i < completeCyclesJSON.length(); i++) {
                    slaData.addCompleteCycle(parseSlaCycle(completeCyclesJSON.optJSONObject(i)));
                }
            }

            if (ongoingCycleJSON != null) {
                slaData.setOngoingCycle(parseSlaCycle(ongoingCycleJSON));
            }

            return slaData;
        } catch (JSONException e) {
            LOGGER.error("Error While Parsing SLA Field '{}' into JSON Object.", slaField.getName());
            throw new JiraAssistantException(e.getMessage(), e);
        }

    }

}
