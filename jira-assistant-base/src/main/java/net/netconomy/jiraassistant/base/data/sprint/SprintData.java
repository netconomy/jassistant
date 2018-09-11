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
package net.netconomy.jiraassistant.base.data.sprint;

import org.joda.time.DateTime;

/**
 * 
 * This Class holds all MetaData about a Sprint, that can be extracted over the JIRA API but no Issue Information.
 * 
 * @author mweilbuchner
 *
 */
public class SprintData {

    protected Integer id;
    protected Integer rapidViewId;
    protected String name;
    protected String state;
    protected String startDate;
    protected String endDate;
    protected String completeDate;

    public SprintData() {

    }

    public SprintData(SprintData sprintData) {
        this.id = sprintData.id;
        this.rapidViewId = sprintData.rapidViewId;
        this.name = sprintData.name;
        this.state = sprintData.state;
        this.startDate = sprintData.startDate;
        this.endDate = sprintData.endDate;
        this.completeDate = sprintData.completeDate;
    }

    public DateTime getStartDateAsDateTime() {
        return DateTime.parse(startDate);
    }

    public DateTime getEndDateAsDateTime() {
        return DateTime.parse(endDate);
    }

    public DateTime getCompleteDateAsDateTime() {
        return DateTime.parse(completeDate);
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRapidViewId() {
        return rapidViewId;
    }

    public void setRapidViewId(Integer rapidViewId) {
        this.rapidViewId = rapidViewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public String toString() {
        return "SprintData [id=" + id + ", rapidViewId=" + rapidViewId + ", name=" + name + ", state=" + state
                + ", startDate=" + startDate + ", endDate=" + endDate + ", completeDate=" + completeDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SprintData)) {
            return false;
        } else {
            SprintData other = (SprintData) obj;

            return !(id == null) && id.equals(other.id);
        }
    }

}
