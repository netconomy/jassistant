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

import static org.junit.Assert.assertEquals;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.supportanalysis.SupportAnalysisDIConfiguration;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaCycle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { SupportAnalysisDIConfiguration.class })
public class SLADataServiceTest {

    @Autowired
    private SLADataService slaDataService;

    private JSONObject completeCyclesData;
    private JSONObject ongoingCyclesData;

    private JSONObject startTime1;
    private JSONObject stopTime1;
    private JSONObject goalDuration1;
    private JSONObject elapsedTime1;
    private JSONObject remainingTime1;

    private JSONObject startTime2;
    private JSONObject breachTime2;
    private JSONObject goalDuration2;
    private JSONObject elapsedTime2;
    private JSONObject remainingTime2;
    
    @Before
    public void setUp() throws Exception {
        
        completeCyclesData = new JSONObject();

        startTime1 = new JSONObject();
        stopTime1 = new JSONObject();
        goalDuration1 = new JSONObject();
        elapsedTime1 = new JSONObject();
        remainingTime1 = new JSONObject();

        startTime1.put("epochMillis", 1468242988661L);
        stopTime1.put("epochMillis", 1468325433300L);

        goalDuration1.put("millis", 28800000);
        elapsedTime1.put("millis", 28444639);
        remainingTime1.put("millis", 355361);

        completeCyclesData.put("startTime", startTime1);
        completeCyclesData.put("stopTime", stopTime1);
        completeCyclesData.put("breached", false);
        completeCyclesData.put("goalDuration", goalDuration1);
        completeCyclesData.put("elapsedTime", elapsedTime1);
        completeCyclesData.put("remainingTime", remainingTime1);
        
        ongoingCyclesData = new JSONObject();

        startTime2 = new JSONObject();
        breachTime2 = new JSONObject();
        goalDuration2 = new JSONObject();
        elapsedTime2 = new JSONObject();
        remainingTime2 = new JSONObject();

        startTime2.put("epochMillis", 1470656597743L);
        breachTime2.put("epochMillis", 1470739397743L);

        goalDuration2.put("millis", 28800000);
        elapsedTime2.put("millis", 10640242);
        remainingTime2.put("millis", 18159758);

        ongoingCyclesData.put("startTime", startTime2);
        ongoingCyclesData.put("breachTime", breachTime2);
        ongoingCyclesData.put("breached", false);
        ongoingCyclesData.put("paused", false);
        ongoingCyclesData.put("withinCalendarHours", true);
        ongoingCyclesData.put("goalDuration", goalDuration2);
        ongoingCyclesData.put("elapsedTime", elapsedTime2);
        ongoingCyclesData.put("remainingTime", remainingTime2);

    }

    @Test
    public void parseSlaCycleTestComplete() {

        SlaCycle actual;

        actual = slaDataService.parseSlaCycle(completeCyclesData);

        assertEquals("Start Time was not parsed correctly from complete Cycle.", new DateTime(1468242988661L),
                actual.getStartTime());
        assertEquals("Stop Time was not parsed correctly from complete Cycle.", new DateTime(1468325433300L),
                actual.getStopTime());
        assertEquals("Breached was not parsed correctly from complete Cycle.", false, actual.getBreached());
        assertEquals("Goal Duration was not parsed correctly from complete Cycle.", Double.valueOf(28800000.0 / 60000.0),
                actual.getGoalDurationMinutes());
        assertEquals("Elapsed Time was not parsed correctly from complete Cycle.",
                Double.valueOf(28444639.0 / 60000.0), actual.getElapsedTimeMinutes());
        assertEquals("Remaining Time was not parsed correctly from ongoing Cycle.",
                Double.valueOf(355361.0 / 60000.0), actual.getRemainingTimeMinutes());
        
    }

    @Test
    public void parseSlaCycleTestOngoing() {

        SlaCycle actual;
        
        actual = slaDataService.parseSlaCycle(ongoingCyclesData);

        assertEquals("Start Time was not parsed correctly from ongoing Cycle.", new DateTime(1470656597743L),
                actual.getStartTime());
        assertEquals("Breach Time was not parsed correctly from ongoing Cycle.", new DateTime(1470739397743L),
                actual.getBreachTime());
        assertEquals("Breached was not parsed correctly from ongoing Cycle.", false, actual.getBreached());
        assertEquals("Paused was not parsed correctly from ongoing Cycle.", false, actual.getPaused());
        assertEquals("Within Calendar Hours was not parsed correctly from ongoing Cycle.", true, actual.getWithinCalendarHours());
        assertEquals("Goal Duration was not parsed correctly from complete Cycle.", Double.valueOf(28800000.0 / 60000.0),
                actual.getGoalDurationMinutes());
        assertEquals("Elapsed Time was not parsed correctly from ongoing Cycle.", Double.valueOf(10640242.0 / 60000.0),
                actual.getElapsedTimeMinutes());
        assertEquals("Remaining Time was not parsed correctly from ongoing Cycle.",
                Double.valueOf(18159758.0 / 60000.0), actual.getRemainingTimeMinutes());


    }

    @After
    public void tearDown() throws Exception {
    }

}

