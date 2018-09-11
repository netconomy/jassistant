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
import java.util.List;

import net.netconomy.jiraassistant.base.data.IssueLight;

public class SprintDataLight extends SprintData {

    private List<IssueLight> issueListLight = new ArrayList<>();

    private List<String> wantedFields = new ArrayList<>();

    public SprintDataLight() {

    }

    public SprintDataLight(SprintData sprintData) {
        super(sprintData);
    }

    public void addLightIssue(IssueLight issueLight) {
        this.issueListLight.add(issueLight);
    }

    // Getters and Setters
    public List<IssueLight> getIssueListLight() {
        return issueListLight;
    }

    public void setIssueListLight(List<IssueLight> issueListLight) {
        this.issueListLight = issueListLight;
    }

    public List<String> getWantedFields() {
        return wantedFields;
    }

    public void setWantedFields(List<String> wantedFields) {
        this.wantedFields = wantedFields;
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
