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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SprintDataDelta extends SprintData {

    private Integer addedIssues = 0;

    private Integer removedIssues = 0;

    private Double addedStoryPoints = 0.0;

    private Double removedStoryPoints = 0.0;

    private List<String> addedIssueKeys = new ArrayList<>();

    private List<String> removedIssueKeys = new ArrayList<>();

    public SprintDataDelta() {

    }

    public SprintDataDelta(SprintData sprintData) {
        super(sprintData);
    }

    public void addIssueKeyToAddedIssues(String addedIssueKey) {
        this.addedIssueKeys.add(addedIssueKey);
    }

    public void addAllIssueKeyToAddedIssues(Collection<String> addedIssueKeyCollection) {
        this.addedIssueKeys.addAll(addedIssueKeyCollection);
    }

    public void addIssueKeyToRemovedIssues(String removedIssueKey) {
        this.removedIssueKeys.add(removedIssueKey);
    }

    public void addAllIssueKeyToRemovedIssues(Collection<String> removedIssueKeyCollection) {
        this.removedIssueKeys.addAll(removedIssueKeyCollection);
    }

    // Getters and Setters
    public Integer getAddedIssues() {
        return addedIssues;
    }

    public void setAddedIssues(Integer addedIssues) {
        this.addedIssues = addedIssues;
    }

    public Integer getRemovedIssues() {
        return removedIssues;
    }

    public void setRemovedIssues(Integer removedIssues) {
        this.removedIssues = removedIssues;
    }

    public Double getAddedStoryPoints() {
        return addedStoryPoints;
    }

    public void setAddedStoryPoints(Double addedStoryPoints) {
        this.addedStoryPoints = addedStoryPoints;
    }

    public Double getRemovedStoryPoints() {
        return removedStoryPoints;
    }

    public void setRemovedStoryPoints(Double removedStoryPoints) {
        this.removedStoryPoints = removedStoryPoints;
    }

    public List<String> getAddedIssueKeys() {
        return addedIssueKeys;
    }

    public void setAddedIssueKeys(List<String> addedIssueKeys) {
        this.addedIssueKeys = addedIssueKeys;
    }

    public List<String> getRemovedIssueKeys() {
        return removedIssueKeys;
    }

    public void setRemovedIssueKeys(List<String> removedIssueKeys) {
        this.removedIssueKeys = removedIssueKeys;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
