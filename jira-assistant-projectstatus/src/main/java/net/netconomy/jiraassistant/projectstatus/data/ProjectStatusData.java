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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

public class ProjectStatusData implements Comparable<ProjectStatusData> {

    private String creationDate;
    private List<String> projects;
    private List<String> excludedTypes;
    private String andClause;

    private String usedFilter;

    private String customGroupingBy;

    private Integer numberOfAnalysedIssues = 0;

    private StatusGroupingData openIssues;
    private StatusGroupingData inProgressIssues;
    private StatusGroupingData waitingIssues;
    private StatusGroupingData implementedIssues;
    private StatusGroupingData finishedIssues;
    private StatusGroupingData closedIssues;

    private Set<String> openStatus = new HashSet<>();
    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> waitingStatus = new ArrayList<>();
    private List<String> implementedStatus = new ArrayList<>();
    private List<String> finishedStatus = new ArrayList<>();
    private List<String> closedStatus = new ArrayList<>();

    public ProjectStatusData() {

    }

    public void countIssue() {
        numberOfAnalysedIssues++;
    }

    public boolean addOpenStatus(String statusName) {
        return openStatus.add(statusName);
    }

    // Getters and Setters
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getExcludedTypes() {
        return excludedTypes;
    }

    public void setExcludedTypes(List<String> excludedTypes) {
        this.excludedTypes = excludedTypes;
    }

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    public String getUsedFilter() {
        return usedFilter;
    }

    public void setUsedFilter(String usedFilter) {
        this.usedFilter = usedFilter;
    }

    public String getCustomGroupingBy() {
        return customGroupingBy;
    }

    public void setCustomGroupingBy(String customGroupingBy) {
        this.customGroupingBy = customGroupingBy;
    }

    public Integer getNumberOfAnalysedIssues() {
        return numberOfAnalysedIssues;
    }

    public void setNumberOfAnalysedIssues(Integer numberOfAnalysedIssues) {
        this.numberOfAnalysedIssues = numberOfAnalysedIssues;
    }

    public StatusGroupingData getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(StatusGroupingData openIssues) {
        this.openIssues = openIssues;
    }

    public StatusGroupingData getInProgressIssues() {
        return inProgressIssues;
    }

    public void setInProgressIssues(StatusGroupingData inProgressIssues) {
        this.inProgressIssues = inProgressIssues;
    }

    public StatusGroupingData getWaitingIssues() {
        return waitingIssues;
    }

    public void setWaitingIssues(StatusGroupingData waitingIssues) {
        this.waitingIssues = waitingIssues;
    }

    public StatusGroupingData getImplementedIssues() {
        return implementedIssues;
    }

    public void setImplementedIssues(StatusGroupingData implementedIssues) {
        this.implementedIssues = implementedIssues;
    }

    public StatusGroupingData getFinishedIssues() {
        return finishedIssues;
    }

    public void setFinishedIssues(StatusGroupingData finishedIssues) {
        this.finishedIssues = finishedIssues;
    }

    public StatusGroupingData getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(StatusGroupingData closedIssues) {
        this.closedIssues = closedIssues;
    }

    public Set<String> getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Set<String> openStatus) {
        this.openStatus = openStatus;
    }

    public List<String> getInProgressStatus() {
        return inProgressStatus;
    }

    public void setInProgressStatus(List<String> inProgressStatus) {
        this.inProgressStatus = inProgressStatus;
    }

    public List<String> getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(List<String> waitingStatus) {
        this.waitingStatus = waitingStatus;
    }

    public List<String> getImplementedStatus() {
        return implementedStatus;
    }

    public void setImplementedStatus(List<String> implementedStatus) {
        this.implementedStatus = implementedStatus;
    }

    public List<String> getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(List<String> finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public List<String> getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(List<String> closedStatus) {
        this.closedStatus = closedStatus;
    }

    @Override
    public String toString() {
        return "ProjectStatusData [creationDate=" + creationDate + ", projects=" + projects + ", excludedTypes="
                + excludedTypes + ", andClause=" + andClause + ", usedFilter=" + usedFilter + ", customGroupingBy="
                + customGroupingBy + ", numberOfAnalysedIssues=" + numberOfAnalysedIssues + ", openIssues="
                + openIssues + ", inProgressIssues=" + inProgressIssues + ", waitingIssues=" + waitingIssues
                + ", implementedIssues=" + implementedIssues + ", finishedIssues=" + finishedIssues + ", closedIssues="
                + closedIssues + ", openStatus=" + openStatus + ", inProgressStatus=" + inProgressStatus
                + ", waitingStatus=" + waitingStatus + ", implementedStatus=" + implementedStatus + ", finishedStatus="
                + finishedStatus + ", closedStatus=" + closedStatus + "]";
    }

    public DateTime getCreationDateAsDateTime() {
        return DateTime.parse(creationDate);
    }

    @Override
    public int compareTo(ProjectStatusData compareKanbanResultData) {

        DateTime thisDateTime = this.getCreationDateAsDateTime();
        DateTime compareDate = compareKanbanResultData.getCreationDateAsDateTime();

        return thisDateTime.compareTo(compareDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((usedFilter == null) ? 0 : usedFilter.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ProjectStatusData)) {
            return false;
        } else {
            ProjectStatusData other = (ProjectStatusData) obj;

            return !(usedFilter == null) && usedFilter.equals(other.usedFilter);
        }
    }

}
