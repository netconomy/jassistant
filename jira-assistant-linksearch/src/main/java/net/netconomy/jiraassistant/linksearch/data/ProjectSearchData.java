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
package net.netconomy.jiraassistant.linksearch.data;

import java.util.ArrayList;
import java.util.List;

public class ProjectSearchData {

    private String project = "";
    private List<String> issueTypes = new ArrayList<>();
    private Boolean negateIssueTypes = false;
    private List<String> status = new ArrayList<>();
    private Boolean negateStatus = false;
    private String andClause = "";

    public ProjectSearchData() {

    }

    public ProjectSearchData(ProjectSearchDataBuilder builder) {
        this.project = builder.project;
        this.issueTypes = builder.issueTypes;
        this.negateIssueTypes = builder.negateIssueTypes;
        this.status = builder.status;
        this.negateStatus = builder.negateStatus;
        this.andClause = builder.andClause;
    }

    // Adders
    public void addIssueType(String issueType) {
        this.issueTypes.add(issueType);
    }

    public void addStatus(String status) {
        this.status.add(status);
    }

    // Getters and Setters
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public Boolean getNegateIssueTypes() {
        return negateIssueTypes;
    }

    public void setNegateIssueTypes(Boolean negateIssueTypes) {
        this.negateIssueTypes = negateIssueTypes;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Boolean getNegateStatus() {
        return negateStatus;
    }

    public void setNegateStatus(Boolean negateStatus) {
        this.negateStatus = negateStatus;
    }

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    @Override
    public String toString() {
        return "ProjectSearchData [project=" + project + ", issueTypes=" + issueTypes + ", negateIssueTypes="
                + negateIssueTypes + ", status=" + status + ", negateStatus=" + negateStatus + ", andClause="
                + andClause + "]";
    }

    public static class ProjectSearchDataBuilder {

        private final String project;
        private List<String> issueTypes = new ArrayList<>();
        private Boolean negateIssueTypes = false;
        private List<String> status = new ArrayList<>();
        private Boolean negateStatus = false;
        private String andClause = "";

        public ProjectSearchDataBuilder(String project) {
            this.project = project;
        }

        public ProjectSearchDataBuilder issueTypes(List<String> issueTypes) {
            this.issueTypes = issueTypes;
            return this;
        }

        public ProjectSearchDataBuilder negateIssueTypes(Boolean negateIssueTypes) {
            this.negateIssueTypes = negateIssueTypes;
            return this;
        }

        public ProjectSearchDataBuilder status(List<String> status) {
            this.status = status;
            return this;
        }

        public ProjectSearchDataBuilder negateStatus(Boolean negateStatus) {
            this.negateStatus = negateStatus;
            return this;
        }

        public ProjectSearchDataBuilder andClause(String andClause) {
            this.andClause = andClause;
            return this;
        }

        public ProjectSearchData build() {
            return new ProjectSearchData(this);
        }

    }

}
