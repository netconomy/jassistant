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
package net.netconomy.jiraassistant.sprintanalysis.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataLight;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;

public class SprintResultData implements Comparable<SprintResultData> {

    private SprintData sprintData;

    private Integer numberOfAllIssuesStart = 0;
    private Integer numberOfAllIssuesEnd = 0;

    private Integer numberOfSubIssuesEnd = 0;

    private Integer plannedDeliveredIssues = 0;

    private Double numberOfAllStoryPointsStart = 0.0;
    private Double numberOfAllStoryPointsEnd = 0.0;

    private Double numberOfAllFinishedStoryPoints = 0.0;
    private Double numberOfAllClosedStoryPoints = 0.0;

    private Double plannedDeliveredStoryPoints = 0.0;

    private Integer reopenCount = 0;
    private Integer minutesSpentOnSubBugs = 0;

    private IssueStatistics storyStatistics;
    private IssueStatistics defectBugStatistics;
    private IssueStatistics taskStatistics;

    private FlaggingStatistics flaggingStatistics;

    private AdditionalDefectBugStatistics additionalDefectBugStatistics;

    private SprintDataDelta sprintDataDelta;

    private List<String> observedProjects = new ArrayList<>();

    private SprintDataLight sprintDataLight;

    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> implementedStatus = new ArrayList<>();
    private List<String> finishedStatus = new ArrayList<>();
    private List<String> closedStatus = new ArrayList<>();

    private List<String> storyIssueTypes = new ArrayList<>();
    private List<String> defectBugIssueTypes = new ArrayList<>();
    private List<String> taskIssueTypes = new ArrayList<>();

    public SprintResultData() {

    }

    // Lists
    public void addStoryIssueType(String storyIssueTypeName) {
        this.storyIssueTypes.add(storyIssueTypeName);
    }

    public void addDefectBugIssueType(String defectBugIssueTypeName) {
        this.defectBugIssueTypes.add(defectBugIssueTypeName);
    }

    public void addAllDefectBugIssueType(List<String> defectBugIssueTypeNames) {
        this.defectBugIssueTypes.addAll(defectBugIssueTypeNames);
    }

    public void addTaskIssueType(String taskIssueTypeName) {
        this.taskIssueTypes.add(taskIssueTypeName);
    }

    public void addProjektKeyIfNotContained(String projektKey) {
        if (!this.observedProjects.contains(projektKey)) {
            this.observedProjects.add(projektKey);
        }
    }

    public void addAllProjektKeysIfNotContained(List<String> projektKeys) {
        for (String currentKey : projektKeys) {
            addProjektKeyIfNotContained(currentKey);
        }
    }

    // Getters and Setter
    public SprintData getSprintData() {
        return sprintData;
    }

    public void setSprintData(SprintData sprintData) {
        this.sprintData = sprintData;
    }

    public Integer getNumberOfAllIssuesStart() {
        return numberOfAllIssuesStart;
    }

    public void setNumberOfAllIssuesStart(Integer numberOfAllIssuesStart) {
        this.numberOfAllIssuesStart = numberOfAllIssuesStart;
    }

    public Double getNumberOfAllStoryPointsStart() {
        return numberOfAllStoryPointsStart;
    }

    public void setNumberOfAllStoryPointsStart(Double numberOfAllStoryPointsStart) {
        this.numberOfAllStoryPointsStart = numberOfAllStoryPointsStart;
    }

    public Integer getNumberOfAllIssuesEnd() {
        return numberOfAllIssuesEnd;
    }

    public void setNumberOfAllIssuesEnd(Integer numberOfAllIssuesEnd) {
        this.numberOfAllIssuesEnd = numberOfAllIssuesEnd;
    }

    public Double getNumberOfAllStoryPointsEnd() {
        return numberOfAllStoryPointsEnd;
    }

    public void setNumberOfAllStoryPointsEnd(Double numberOfAllStoryPointsEnd) {
        this.numberOfAllStoryPointsEnd = numberOfAllStoryPointsEnd;
    }

    public Double getNumberOfAllFinishedStoryPoints() {
        return numberOfAllFinishedStoryPoints;
    }

    public void setNumberOfAllFinishedStoryPoints(Double numberOfAllFinishedStoryPoints) {
        this.numberOfAllFinishedStoryPoints = numberOfAllFinishedStoryPoints;
    }

    public Integer getNumberOfSubIssuesEnd() {
        return numberOfSubIssuesEnd;
    }

    public void setNumberOfSubIssuesEnd(Integer numberOfSubIssuesEnd) {
        this.numberOfSubIssuesEnd = numberOfSubIssuesEnd;
    }

    public List<String> getInProgressStatus() {
        return inProgressStatus;
    }

    public void setInProgressStatus(List<String> inProgressStatus) {
        this.inProgressStatus = inProgressStatus;
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

    public List<String> getStoryIssueTypes() {
        return storyIssueTypes;
    }

    public void setStoryIssueTypes(List<String> storyIssueTypes) {
        this.storyIssueTypes = storyIssueTypes;
    }

    public List<String> getDefectBugIssueTypes() {
        return defectBugIssueTypes;
    }

    public void setDefectBugIssueTypes(List<String> defectBugIssueTypes) {
        this.defectBugIssueTypes = defectBugIssueTypes;
    }

    public List<String> getTaskIssueTypes() {
        return taskIssueTypes;
    }

    public void setTaskIssueTypes(List<String> taskIssueTypes) {
        this.taskIssueTypes = taskIssueTypes;
    }

    public IssueStatistics getStoryStatistics() {
        return storyStatistics;
    }

    public void setStoryStatistics(IssueStatistics storyStatistics) {
        this.storyStatistics = storyStatistics;
    }

    public IssueStatistics getDefectBugStatistics() {
        return defectBugStatistics;
    }

    public void setDefectBugStatistics(IssueStatistics defectBugStatistics) {
        this.defectBugStatistics = defectBugStatistics;
    }

    public IssueStatistics getTaskStatistics() {
        return taskStatistics;
    }

    public void setTaskStatistics(IssueStatistics taskStatistics) {
        this.taskStatistics = taskStatistics;
    }

    public SprintDataLight getSprintDataLight() {
        return sprintDataLight;
    }

    public void setSprintDataLight(SprintDataLight sprintDataLight) {
        this.sprintDataLight = sprintDataLight;
    }

    public SprintDataDelta getSprintDataDelta() {
        return sprintDataDelta;
    }

    public void setSprintDataDelta(SprintDataDelta sprintDataDelta) {
        this.sprintDataDelta = sprintDataDelta;
    }

    public Integer getMinutesSpentOnSubBugs() {
        return minutesSpentOnSubBugs;
    }

    public void setMinutesSpentOnSubBugs(Integer minutesSpentOnSubBugs) {
        this.minutesSpentOnSubBugs = minutesSpentOnSubBugs;
    }

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getPlannedDeliveredIssues() {
        return plannedDeliveredIssues;
    }

    public void setPlannedDeliveredIssues(Integer plannedDeliveredIssues) {
        this.plannedDeliveredIssues = plannedDeliveredIssues;
    }

    public Double getPlannedDeliveredStoryPoints() {
        return plannedDeliveredStoryPoints;
    }

    public void setPlannedDeliveredStoryPoints(Double plannedDeliveredStoryPoints) {
        this.plannedDeliveredStoryPoints = plannedDeliveredStoryPoints;
    }

    public AdditionalDefectBugStatistics getAdditionalDefectBugStatistics() {
        return additionalDefectBugStatistics;
    }

    public void setAdditionalDefectBugStatistics(AdditionalDefectBugStatistics additionalDefectBugStatistics) {
        this.additionalDefectBugStatistics = additionalDefectBugStatistics;
    }

    public List<String> getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(List<String> closedStatus) {
        this.closedStatus = closedStatus;
    }

    public Double getNumberOfAllClosedStoryPoints() {
        return numberOfAllClosedStoryPoints;
    }

    public void setNumberOfAllClosedStoryPoints(Double numberOfAllClosedStoryPoints) {
        this.numberOfAllClosedStoryPoints = numberOfAllClosedStoryPoints;
    }

    public List<String> getObservedProjects() {
        return observedProjects;
    }

    public void setObservedProjects(List<String> containedProjects) {
        this.observedProjects = containedProjects;
    }

    public FlaggingStatistics getFlaggingStatistics() {
        return flaggingStatistics;
    }

    public void setFlaggingStatistics(FlaggingStatistics flaggingStatistics) {
        this.flaggingStatistics = flaggingStatistics;
    }

    @Override
    public String toString() {
        return "SprintResultData [sprintData=" + sprintData + ", observedProjects=" + observedProjects
                + ", numberOfAllIssuesStart=" + numberOfAllIssuesStart + ", numberOfAllIssuesEnd="
                + numberOfAllIssuesEnd + ", numberOfSubIssuesEnd=" + numberOfSubIssuesEnd + ", plannedDeliveredIssues="
                + plannedDeliveredIssues + ", numberOfAllStoryPointsStart=" + numberOfAllStoryPointsStart
                + ", numberOfAllStoryPointsEnd=" + numberOfAllStoryPointsEnd + ", numberOfAllFinishedStoryPoints="
                + numberOfAllFinishedStoryPoints + ", numberOfAllClosedStoryPoints=" + numberOfAllClosedStoryPoints
                + ", plannedDeliveredStoryPoints=" + plannedDeliveredStoryPoints + ", reopenCount=" + reopenCount
                + ", minutesSpentOnSubBugs=" + minutesSpentOnSubBugs + ", storyStatistics=" + storyStatistics
                + ", defectBugStatistics=" + defectBugStatistics + ", taskStatistics=" + taskStatistics
                + ", flaggingStatistics=" + flaggingStatistics + ", additionalDefectBugStatistics="
                + additionalDefectBugStatistics + ", sprintDataDelta=" + sprintDataDelta + ", sprintDataLight="
                + sprintDataLight + ", inProgressStatus=" + inProgressStatus + ", implementedStatus="
                + implementedStatus + ", finishedStatus=" + finishedStatus + ", closedStatus=" + closedStatus
                + ", storyIssueTypes=" + storyIssueTypes + ", defectBugIssueTypes=" + defectBugIssueTypes
                + ", taskIssueTypes=" + taskIssueTypes + "]";
    }

    @Override
    public int compareTo(SprintResultData compareSprintResultData) {

        DateTime thisDateTime = this.getSprintData().getStartDateAsDateTime();
        DateTime compareDate = compareSprintResultData.getSprintData().getStartDateAsDateTime();

        return thisDateTime.compareTo(compareDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sprintData == null) ? 0 : sprintData.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SprintResultData)) {
            return false;
        } else {
            SprintResultData other = (SprintResultData) obj;

            return !(sprintData == null) && sprintData.equals(other.sprintData);
        }
    }

}
