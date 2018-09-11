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
package net.netconomy.jiraassistant.projectstatus.data;

import java.util.HashMap;
import java.util.Map;

public class IssuesInGroupingData {

    protected Integer numberOfIssues = 0;

    protected Double numberOfStoryPoints = 0.0;

    protected Integer numberOfUnEstimatedIssues = 0;

    protected Map<String, Integer> issueTypes = new HashMap<>();

    public IssuesInGroupingData() {

    }

    public void countIssue() {
        numberOfIssues++;
    }

    public void increaseStoryPoints(Double inc) {
        numberOfStoryPoints += inc;
    }

    public void countUnEstimatedIssue() {
        numberOfUnEstimatedIssues++;
    }

    public void countIssueType(String issueTypeName) {

        if (issueTypes.containsKey(issueTypeName)) {
            issueTypes.put(issueTypeName, issueTypes.get(issueTypeName) + 1);
        } else {
            issueTypes.put(issueTypeName, 1);
        }

    }

    // Getters and Setters
    public Integer getNumberOfIssues() {
        return numberOfIssues;
    }

    public void setNumberOfIssues(Integer numberOfIssues) {
        this.numberOfIssues = numberOfIssues;
    }

    public Double getNumberOfStoryPoints() {
        return numberOfStoryPoints;
    }

    public void setNumberOfStoryPoints(Double numberOfStoryPoints) {
        this.numberOfStoryPoints = numberOfStoryPoints;
    }

    public Integer getNumberOfUnEstimatedIssues() {
        return numberOfUnEstimatedIssues;
    }

    public void setNumberOfUnEstimatedIssues(Integer numberOfUnEstimatedIssues) {
        this.numberOfUnEstimatedIssues = numberOfUnEstimatedIssues;
    }

    public Map<String, Integer> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(Map<String, Integer> issueTypes) {
        this.issueTypes = issueTypes;
    }

    @Override
    public String toString() {
        return "IssuesInStatusData [numberOfIssues=" + numberOfIssues + ", numberOfStoryPoints=" + numberOfStoryPoints
                + ", numberOfUnEstimatedIssues=" + numberOfUnEstimatedIssues + ", issueTypes=" + issueTypes + "]";
    }

}
