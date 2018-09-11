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
package net.netconomy.jiraassistant.sprintforecast.data;

import java.util.ArrayList;
import java.util.List;

import net.netconomy.jiraassistant.base.data.IssueLight;

public class Forecast {

    private String foreCastDate;
    private String usedFilter = "";
    private Integer numberOfForecastedSprints = 0;
    private Double forecastVelocity = 0.0;

    private List<ForecastedSprint> forecastedSprints = new ArrayList<>();

    private List<IssueLight> ignoredIssues = new ArrayList<>();

    public Forecast() {

    }

    public void addForecastedSprint(ForecastedSprint forecastedSprint) {
        this.forecastedSprints.add(forecastedSprint);
    }

    public void addIgnoredIssue(IssueLight ignoredIssue) {
        this.ignoredIssues.add(ignoredIssue);
    }

    // Getters and Setters
    public Integer getNumberOfForecastedSprints() {
        return numberOfForecastedSprints;
    }

    public void setNumberOfForecastedSprints(Integer numberOfForcastedSprints) {
        this.numberOfForecastedSprints = numberOfForcastedSprints;
    }

    public String getForeCastDate() {
        return foreCastDate;
    }

    public void setForeCastDate(String foreCastDate) {
        this.foreCastDate = foreCastDate;
    }

    public List<ForecastedSprint> getForecastedSprints() {
        return forecastedSprints;
    }

    public void setForecastedSprints(List<ForecastedSprint> forcastedSprints) {
        this.forecastedSprints = forcastedSprints;
    }

    public List<IssueLight> getIgnoredIssues() {
        return ignoredIssues;
    }

    public void setIgnoredIssues(List<IssueLight> ignoredIssues) {
        this.ignoredIssues = ignoredIssues;
    }

    public String getUsedFilter() {
        return usedFilter;
    }

    public void setUsedFilter(String usedFilter) {
        this.usedFilter = usedFilter;
    }

    public Double getForecastVelocity() {
        return forecastVelocity;
    }

    public void setForecastVelocity(Double forecastVelocity) {
        this.forecastVelocity = forecastVelocity;
    }

    @Override
    public String toString() {
        return "Forecast [foreCastDate=" + foreCastDate + ", usedFilter=" + usedFilter + ", numberOfForecastedSprints="
                + numberOfForecastedSprints + ", forecastVelocity=" + forecastVelocity + ", forecastedSprints="
                + forecastedSprints + ", ignoredIssues=" + ignoredIssues + "]";
    }

}
