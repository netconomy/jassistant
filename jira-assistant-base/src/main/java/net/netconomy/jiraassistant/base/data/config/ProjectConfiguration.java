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
package net.netconomy.jiraassistant.base.data.config;

import java.util.ArrayList;
import java.util.List;

public class ProjectConfiguration {

    private String estimationFieldName;
    private String altEstimationFieldName;
    private String resolutionDateFieldName;
    private String sprintFieldName;
    private String defectReasonFieldName;
    private String defectReasonInfoFieldName;
    private String flaggedFieldName;
    private String flaggedFieldValue;

    private List<String> reopenedStatus = new ArrayList<>();
    private List<String> testedStatus = new ArrayList<>();
    private List<String> rejectedStatus = new ArrayList<>();
    private List<String> testableStatus = new ArrayList<>();
    private List<String> inTestingStatus = new ArrayList<>();
    private List<String> defectTriageStatus = new ArrayList<>();
    private List<String> storyIssueTypes = new ArrayList<>();
    private List<String> defectIssueTypes = new ArrayList<>();
    private List<String> bugIssueTypes = new ArrayList<>();
    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> waitingStatus = new ArrayList<>();
    private List<String> implementedStatus = new ArrayList<>();
    private List<String> finishedStatus = new ArrayList<>();
    private List<String> liveStatus = new ArrayList<>();
    private List<String> closedStatus = new ArrayList<>();

    public ProjectConfiguration() {

    }

    public String getEstimationFieldName() {
        return estimationFieldName;
    }

    public void setEstimationFieldName(String estimationFieldName) {
        this.estimationFieldName = estimationFieldName;
    }

    public String getResolutionDateFieldName() {
        return resolutionDateFieldName;
    }

    public void setResolutionDateFieldName(String resolutionDateFieldName) {
        this.resolutionDateFieldName = resolutionDateFieldName;
    }

    public String getSprintFieldName() {
        return sprintFieldName;
    }

    public void setSprintFieldName(String sprintFieldName) {
        this.sprintFieldName = sprintFieldName;
    }

    public List<String> getStoryIssueTypes() {
        return storyIssueTypes;
    }

    public void setStoryIssueTypes(List<String> storyIssueTypes) {
        this.storyIssueTypes = storyIssueTypes;
    }

    public List<String> getDefectIssueTypes() {
        return defectIssueTypes;
    }

    public void setDefectIssueTypes(List<String> defectIssueTypes) {
        this.defectIssueTypes = defectIssueTypes;
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

    public List<String> getReopenedStatus() {
        return reopenedStatus;
    }

    public void setReopenedStatus(List<String> reopenedStatus) {
        this.reopenedStatus = reopenedStatus;
    }

    public List<String> getTestedStatus() {
        return testedStatus;
    }

    public void setTestedStatus(List<String> testedStatus) {
        this.testedStatus = testedStatus;
    }

    public List<String> getRejectedStatus() {
        return rejectedStatus;
    }

    public void setRejectedStatus(List<String> rejectedStatus) {
        this.rejectedStatus = rejectedStatus;
    }

    public List<String> getTestableStatus() {
        return testableStatus;
    }

    public void setTestableStatus(List<String> testableStatus) {
        this.testableStatus = testableStatus;
    }

    public List<String> getDefectTriageStatus() {
        return defectTriageStatus;
    }

    public void setDefectTriageStatus(List<String> defectTriageStatus) {
        this.defectTriageStatus = defectTriageStatus;
    }

    public List<String> getBugIssueTypes() {
        return bugIssueTypes;
    }

    public void setBugIssueTypes(List<String> bugIssueTypes) {
        this.bugIssueTypes = bugIssueTypes;
    }

    public List<String> getInTestingStatus() {
        return inTestingStatus;
    }

    public void setInTestingStatus(List<String> inTestingStatus) {
        this.inTestingStatus = inTestingStatus;
    }

    public String getDefectReasonFieldName() {
        return defectReasonFieldName;
    }

    public void setDefectReasonFieldName(String defectReasonFieldName) {
        this.defectReasonFieldName = defectReasonFieldName;
    }

    public String getDefectReasonInfoFieldName() {
        return defectReasonInfoFieldName;
    }

    public void setDefectReasonInfoFieldName(String defectReasonInfoFieldName) {
        this.defectReasonInfoFieldName = defectReasonInfoFieldName;
    }

    public String getFlaggedFieldName() {
        return flaggedFieldName;
    }

    public void setFlaggedFieldName(String flaggedFieldName) {
        this.flaggedFieldName = flaggedFieldName;
    }

    public String getFlaggedFieldValue() {
        return flaggedFieldValue;
    }

    public void setFlaggedFieldValue(String flaggedFieldValue) {
        this.flaggedFieldValue = flaggedFieldValue;
    }

    public List<String> getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(List<String> closedStatus) {
        this.closedStatus = closedStatus;
    }

    public List<String> getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(List<String> waitingStatus) {
        this.waitingStatus = waitingStatus;
    }

    public String getAltEstimationFieldName() {
        return altEstimationFieldName;
    }

    public void setAltEstimationFieldName(String altEstimationFieldName) {
        this.altEstimationFieldName = altEstimationFieldName;
    }

    public List<String> getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(List<String> liveStatus) {
        this.liveStatus = liveStatus;
    }

    @Override
    public String toString() {
        return "ProjectConfiguration{" +
            "estimationFieldName='" + estimationFieldName + '\'' +
            ", altEstimationFieldName='" + altEstimationFieldName + '\'' +
            ", resolutionDateFieldName='" + resolutionDateFieldName + '\'' +
            ", sprintFieldName='" + sprintFieldName + '\'' +
            ", defectReasonFieldName='" + defectReasonFieldName + '\'' +
            ", defectReasonInfoFieldName='" + defectReasonInfoFieldName + '\'' +
            ", flaggedFieldName='" + flaggedFieldName + '\'' +
            ", flaggedFieldValue='" + flaggedFieldValue + '\'' +
            ", reopenedStatus=" + reopenedStatus +
            ", testedStatus=" + testedStatus +
            ", rejectedStatus=" + rejectedStatus +
            ", testableStatus=" + testableStatus +
            ", inTestingStatus=" + inTestingStatus +
            ", defectTriageStatus=" + defectTriageStatus +
            ", storyIssueTypes=" + storyIssueTypes +
            ", defectIssueTypes=" + defectIssueTypes +
            ", bugIssueTypes=" + bugIssueTypes +
            ", inProgressStatus=" + inProgressStatus +
            ", waitingStatus=" + waitingStatus +
            ", implementedStatus=" + implementedStatus +
            ", finishedStatus=" + finishedStatus +
            ", liveStatus=" + liveStatus +
            ", closedStatus=" + closedStatus +
            '}';
    }

}
