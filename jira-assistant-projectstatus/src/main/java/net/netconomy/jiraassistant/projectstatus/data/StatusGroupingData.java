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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatusGroupingData extends IssuesInGroupingData {

    private String statusGroupName;

    private Map<String, CustomGroupingData> customGroupingMap;
    
    private Set<String> statusList = new HashSet<>();

    public StatusGroupingData(String statusGroupName) {
        super();
        this.statusGroupName = statusGroupName;
    }

    public boolean addStatus(String statusName) {
        return statusList.add(statusName);
    }

    public void putCustomGrouping(String groupingValue, CustomGroupingData updatedGroupingData) {

        if (customGroupingMap == null) {
            customGroupingMap = new HashMap<>();
        }

        customGroupingMap.put(groupingValue, updatedGroupingData);

    }
    
    //Getters and Setters
    public String getStatusGroupName() {
        return statusGroupName;
    }

    public void setStatusGroupName(String statusGroupName) {
        this.statusGroupName = statusGroupName;
    }

    public Map<String, CustomGroupingData> getCustomGroupingMap() {
        return customGroupingMap;
    }

    public void setCustomGroupingMap(Map<String, CustomGroupingData> customGroupingMap) {
        this.customGroupingMap = customGroupingMap;
    }

    public Set<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(Set<String> statusList) {
        this.statusList = statusList;
    }

    @Override
    public String toString() {
        return "StatusGroupingData [statusGroupName=" + statusGroupName + ", customGroupingMap=" + customGroupingMap
                + ", statusList=" + statusList + ", numberOfIssues=" + numberOfIssues + ", numberOfStoryPoints="
                + numberOfStoryPoints + ", numberOfUnEstimatedIssues=" + numberOfUnEstimatedIssues + ", issueTypes="
                + issueTypes + "]";
    }

}
