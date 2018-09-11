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
package net.netconomy.jiraassistant.sprintforecast.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.services.CSVService;
import net.netconomy.jiraassistant.sprintforecast.data.Forecast;
import net.netconomy.jiraassistant.sprintforecast.data.ForecastedSprint;

@Service
public class ForecastToCSVService {

    @Autowired
    CSVService csvService;

    public CSVTable generateCSVTableFromForecast(Forecast forecast) {
        CSVTable table = new CSVTable();
        CSVLine currentLine = new CSVLine();
        Integer sprintCount = 0;

        List<ForecastedSprint> forecastedSprints = forecast.getForecastedSprints();

        List<String> wantedFields = forecastedSprints.get(0).getForecastedIssues().get(0).getWantedFields();

        table.addCSVLineToTable(csvService.generateFirstLine(wantedFields));

        for (ForecastedSprint currentSprint : forecastedSprints) {

            currentLine.addStringToLine("Forecasted Sprint " + (sprintCount + 1));
            table.addCSVLineToTable(currentLine);
            currentLine = new CSVLine();

            for (IssueLight currentIssue : currentSprint.getForecastedIssues()) {

                table.addCSVLineToTable(csvService.generateLineFromLightIssue(currentIssue));

            }

            table.addEmptyLineToTable();
            sprintCount++;

        }

        currentLine.addStringToLine("The following Issues were ignored for the Forecast, "
                + "because they miss an Estimation or are to big to fit into the given Velocity:");
        table.addCSVLineToTable(currentLine);
        currentLine = new CSVLine();

        for (IssueLight currentIssue : forecast.getIgnoredIssues()) {

            table.addCSVLineToTable(csvService.generateLineFromLightIssue(currentIssue));

        }

        table.addEmptyLineToTable();

        currentLine.addStringToLine("This Forecast was created " + forecast.getForeCastDate());
        table.addCSVLineToTable(currentLine);

        return table;
    }

}
