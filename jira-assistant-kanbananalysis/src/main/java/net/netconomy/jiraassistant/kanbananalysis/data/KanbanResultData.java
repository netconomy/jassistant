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
package net.netconomy.jiraassistant.kanbananalysis.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.sprintanalysis.data.AdditionalDefectBugStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.IssueStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;

public class KanbanResultData implements Comparable<KanbanResultData> {

    private String usedFilter = "";

    private Boolean withAltEstimations;

    private String startDate;
    private String endDate;

    private Integer numberOfIssues = 0;
    private Integer numberOfSubIssues = 0;

    private Double numberOfStoryPoints;

    private Map<String, Integer> issuesByEstimation;

    private Integer numberOfAllFinishedIssues = 0;
    private Integer numberOfAllClosedIssues = 0;

    private Double numberOfAllFinishedStoryPoints;
    private Double numberOfAllClosedStoryPoints;

    private Map<String, Integer> finishedIssuesByEstimation;
    private Map<String, Integer> closedIssuesByEstimation;

    private Integer minutesSpentOnIssues = 0;
    private Integer minutesSpentOnSubIssues = 0;

    private Integer reopenCount = 0;
    private Integer minutesSpentOnSubBugs = 0;

    private IssueStatistics storyStatistics;
    private IssueStatistics defectBugStatistics;
    private IssueStatistics taskStatistics;

    private AltIssueStatistics altStoryStatistics;
    private AltIssueStatistics altDefectBugStatistics;
    private AltIssueStatistics altTaskStatistics;

    private KanbanMetrics kanbanMetrics;

    private Map<String, KanbanMetrics> kanbanMetricsByEstimation = new HashMap<>();

    private FlaggingStatistics flaggingStatistics;

    private AdditionalDefectBugStatistics additionalDefectBugStatistics;

    private List<IssueLight> issueListLight = new ArrayList<>();

    private List<String> observedProjects = new ArrayList<>();

    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> waitingStatus = new ArrayList<>();
    private List<String> implementedStatus = new ArrayList<>();
    private List<String> finishedStatus = new ArrayList<>();
    private List<String> closedStatus = new ArrayList<>();

    private List<String> storyIssueTypes = new ArrayList<>();
    private List<String> defectBugIssueTypes = new ArrayList<>();
    private List<String> taskIssueTypes = new ArrayList<>();

    public KanbanResultData(Boolean withAltEstimations) {

        if(withAltEstimations) {
            this.withAltEstimations = true;
            this.issuesByEstimation = new HashMap<>();
            this.finishedIssuesByEstimation = new HashMap<>();
            this.closedIssuesByEstimation = new HashMap<>();
        } else {
            this.withAltEstimations = false;
            this.numberOfStoryPoints = 0.0;
            this.numberOfAllFinishedStoryPoints = 0.0;
            this.numberOfAllClosedStoryPoints = 0.0;
        }

    }

    // Lists
    public void addIssueWithEstimation(String altEstimation) {
        if(issuesByEstimation.containsKey(altEstimation)) {
            issuesByEstimation.put(altEstimation, issuesByEstimation.get(altEstimation) + 1);
        } else {
            issuesByEstimation.put(altEstimation, 1);
        }
    }

    public void addFinishedIssuesByEstimation(Map<String, Integer> finishedIssuesByEstimation) {
        this.finishedIssuesByEstimation.putAll(finishedIssuesByEstimation);
    }

    public void addClosedIssuesByEstimation(Map<String, Integer> closedIssuesByEstimation) {
        this.closedIssuesByEstimation.putAll(closedIssuesByEstimation);
    }

    public void addKanbanMetricsByEstimation(Map<String, KanbanMetrics> kanbanMetricsByEstimation) {
        this.kanbanMetricsByEstimation.putAll(kanbanMetricsByEstimation);
    }

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
    
    public void addLightIssue(IssueLight lightIssue) {
        this.issueListLight.add(lightIssue);
    }

    public void addAllLightIssues(List<IssueLight> lightIssueList) {
        this.issueListLight.addAll(lightIssueList);
    }

    // Getters and Setters
    public Integer getNumberOfIssues() {
        return numberOfIssues;
    }

    public void setNumberOfIssues(Integer numberOfIssues) {
        this.numberOfIssues = numberOfIssues;
    }

    public Integer getNumberOfSubIssues() {
        return numberOfSubIssues;
    }

    public void setNumberOfSubIssues(Integer numberOfSubIssues) {
        this.numberOfSubIssues = numberOfSubIssues;
    }

    public String getUsedFilter() {
        return usedFilter;
    }

    public void setUsedFilter(String usedFilter) {
        this.usedFilter = usedFilter;
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

    public Double getNumberOfAllFinishedStoryPoints() {
        return numberOfAllFinishedStoryPoints;
    }

    public void setNumberOfAllFinishedStoryPoints(Double numberOfAllFinishedStoryPoints) {
        this.numberOfAllFinishedStoryPoints = numberOfAllFinishedStoryPoints;
    }

    public Double getNumberOfAllClosedStoryPoints() {
        return numberOfAllClosedStoryPoints;
    }

    public void setNumberOfAllClosedStoryPoints(Double numberOfAllClosedStoryPoints) {
        this.numberOfAllClosedStoryPoints = numberOfAllClosedStoryPoints;
    }

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getMinutesSpentOnSubBugs() {
        return minutesSpentOnSubBugs;
    }

    public void setMinutesSpentOnSubBugs(Integer minutesSpentOnSubBugs) {
        this.minutesSpentOnSubBugs = minutesSpentOnSubBugs;
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

    public FlaggingStatistics getFlaggingStatistics() {
        return flaggingStatistics;
    }

    public void setFlaggingStatistics(FlaggingStatistics flaggingStatistics) {
        this.flaggingStatistics = flaggingStatistics;
    }

    public AdditionalDefectBugStatistics getAdditionalDefectBugStatistics() {
        return additionalDefectBugStatistics;
    }

    public void setAdditionalDefectBugStatistics(AdditionalDefectBugStatistics additionalDefectBugStatistics) {
        this.additionalDefectBugStatistics = additionalDefectBugStatistics;
    }

    public List<String> getObservedProjects() {
        return observedProjects;
    }

    public void setObservedProjects(List<String> observedProjects) {
        this.observedProjects = observedProjects;
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

    public List<String> getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(List<String> closedStatus) {
        this.closedStatus = closedStatus;
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

    public Integer getNumberOfAllFinishedIssues() {
        return numberOfAllFinishedIssues;
    }

    public void setNumberOfAllFinishedIssues(Integer numberOfAllFinishedIssues) {
        this.numberOfAllFinishedIssues = numberOfAllFinishedIssues;
    }

    public Integer getNumberOfAllClosedIssues() {
        return numberOfAllClosedIssues;
    }

    public void setNumberOfAllClosedIssues(Integer numberOfAllClosedIssues) {
        this.numberOfAllClosedIssues = numberOfAllClosedIssues;
    }

    public Double getNumberOfStoryPoints() {
        return numberOfStoryPoints;
    }

    public void setNumberOfStoryPoints(Double numberOfStoryPoints) {
        this.numberOfStoryPoints = numberOfStoryPoints;
    }

    public Integer getMinutesSpentOnIssues() {
        return minutesSpentOnIssues;
    }

    public void setMinutesSpentOnIssues(Integer minutesSpentOnIssues) {
        this.minutesSpentOnIssues = minutesSpentOnIssues;
    }

    public Integer getMinutesSpentOnSubIssues() {
        return minutesSpentOnSubIssues;
    }

    public void setMinutesSpentOnSubIssues(Integer minutesSpentOnSubIssues) {
        this.minutesSpentOnSubIssues = minutesSpentOnSubIssues;
    }

    public KanbanMetrics getKanbanMetrics() {
        return kanbanMetrics;
    }

    public void setKanbanMetrics(KanbanMetrics kanbanMetrics) {
        this.kanbanMetrics = kanbanMetrics;
    }

    public List<String> getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(List<String> waitingStatus) {
        this.waitingStatus = waitingStatus;
    }

    public AltIssueStatistics getAltStoryStatistics() {
        return altStoryStatistics;
    }

    public void setAltStoryStatistics(AltIssueStatistics altStoryStatistics) {
        this.altStoryStatistics = altStoryStatistics;
    }

    public AltIssueStatistics getAltDefectBugStatistics() {
        return altDefectBugStatistics;
    }

    public void setAltDefectBugStatistics(AltIssueStatistics altDefectBugStatistics) {
        this.altDefectBugStatistics = altDefectBugStatistics;
    }

    public AltIssueStatistics getAltTaskStatistics() {
        return altTaskStatistics;
    }

    public void setAltTaskStatistics(AltIssueStatistics altTaskStatistics) {
        this.altTaskStatistics = altTaskStatistics;
    }

    public Boolean getWithAltEstimations() {
        return withAltEstimations;
    }

    public void setWithAltEstimations(Boolean withAltEstimations) {
        this.withAltEstimations = withAltEstimations;
    }

    @Override
    public String toString() {
        return "KanbanResultData{" +
            "usedFilter='" + usedFilter + '\'' +
            ", withAltEstimations=" + withAltEstimations +
            ", startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", numberOfIssues=" + numberOfIssues +
            ", numberOfSubIssues=" + numberOfSubIssues +
            ", numberOfStoryPoints=" + numberOfStoryPoints +
            ", issuesByEstimation=" + issuesByEstimation +
            ", numberOfAllFinishedIssues=" + numberOfAllFinishedIssues +
            ", numberOfAllClosedIssues=" + numberOfAllClosedIssues +
            ", numberOfAllFinishedStoryPoints=" + numberOfAllFinishedStoryPoints +
            ", numberOfAllClosedStoryPoints=" + numberOfAllClosedStoryPoints +
            ", finishedIssuesByEstimation=" + finishedIssuesByEstimation +
            ", closedIssuesByEstimation=" + closedIssuesByEstimation +
            ", minutesSpentOnIssues=" + minutesSpentOnIssues +
            ", minutesSpentOnSubIssues=" + minutesSpentOnSubIssues +
            ", reopenCount=" + reopenCount +
            ", minutesSpentOnSubBugs=" + minutesSpentOnSubBugs +
            ", storyStatistics=" + storyStatistics +
            ", defectBugStatistics=" + defectBugStatistics +
            ", taskStatistics=" + taskStatistics +
            ", altStoryStatistics=" + altStoryStatistics +
            ", altDefectBugStatistics=" + altDefectBugStatistics +
            ", altTaskStatistics=" + altTaskStatistics +
            ", kanbanMetrics=" + kanbanMetrics +
            ", flaggingStatistics=" + flaggingStatistics +
            ", additionalDefectBugStatistics=" + additionalDefectBugStatistics +
            ", issueListLight=" + issueListLight +
            ", observedProjects=" + observedProjects +
            ", inProgressStatus=" + inProgressStatus +
            ", waitingStatus=" + waitingStatus +
            ", implementedStatus=" + implementedStatus +
            ", finishedStatus=" + finishedStatus +
            ", closedStatus=" + closedStatus +
            ", storyIssueTypes=" + storyIssueTypes +
            ", defectBugIssueTypes=" + defectBugIssueTypes +
            ", taskIssueTypes=" + taskIssueTypes +
            '}';
    }

    public DateTime getStartDateAsDateTime() {
        return DateTime.parse(startDate);
    }

    public DateTime getEndDateAsDateTime() {
        return DateTime.parse(endDate);
    }

    @Override
    public int compareTo(KanbanResultData compareKanbanResultData) {

        DateTime thisDateTime = this.getStartDateAsDateTime();
        DateTime compareDate = compareKanbanResultData.getStartDateAsDateTime();

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

        if (!(obj instanceof KanbanResultData)) {
            return false;
        } else {
            KanbanResultData other = (KanbanResultData) obj;

            return !(usedFilter == null) && usedFilter.equals(other.usedFilter);
        }
    }

}
