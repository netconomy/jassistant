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

import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;

public class IssueStatistics {

    private Integer numberOfIssues = 0;
    private Integer numberOfSubIssues = 0;

    private Double numberOfStoryPoints = 0.0;

    private Integer plannedDeliveredIssues = 0;
    private Double plannedDeliveredStoryPoints = 0.0;

    private Integer reopenCount = 0;

    private Integer minutesSpent = 0;

    private Integer inProgressIssues = 0;
    private Integer waitingIssues = 0;
    private Integer implementedIssues = 0;
    private Integer finishedIssues = 0;
    private Integer closedIssues = 0;

    private Double inProgressStoryPoints = 0.0;
    private Double waitingStoryPoints = 0.0;
    private Double implementedStoryPoints = 0.0;
    private Double finishedStoryPoints = 0.0;
    private Double closedStoryPoints = 0.0;

    private List<String> inProgressKeys = new ArrayList<>();
    private List<String> waitingKeys = new ArrayList<>();
    private List<String> implementedKeys = new ArrayList<>();
    private List<String> finishedKeys = new ArrayList<>();
    private List<String> closedKeys = new ArrayList<>();

    private FlaggingStatistics flaggingStatistics;

    public IssueStatistics() {

    }

    // Adders
    public void addInProgressKey(String inProgressKey) {
        this.inProgressKeys.add(inProgressKey);
    }

    public void addWaitingKey(String waitingKey) {
        this.waitingKeys.add(waitingKey);
    }

    public void addImplementedKey(String implementedKey) {
        this.implementedKeys.add(implementedKey);
    }

    public void addFinishedKey(String finishedKey) {
        this.finishedKeys.add(finishedKey);
    }

    public void addClosedKey(String closedKey) {
        this.closedKeys.add(closedKey);
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

    public Integer getImplementedIssues() {
        return implementedIssues;
    }

    public void setImplementedIssues(Integer implementedIssues) {
        this.implementedIssues = implementedIssues;
    }

    public Integer getFinishedIssues() {
        return finishedIssues;
    }

    public void setFinishedIssues(Integer finishedIssues) {
        this.finishedIssues = finishedIssues;
    }

    public Double getImplementedStoryPoints() {
        return implementedStoryPoints;
    }

    public void setImplementedStoryPoints(Double implementedStoryPoints) {
        this.implementedStoryPoints = implementedStoryPoints;
    }

    public Double getFinishedStoryPoints() {
        return finishedStoryPoints;
    }

    public void setFinishedStoryPoints(Double finishedStoryPoints) {
        this.finishedStoryPoints = finishedStoryPoints;
    }

    public Integer getInProgressIssues() {
        return inProgressIssues;
    }

    public void setInProgressIssues(Integer inProgressIssues) {
        this.inProgressIssues = inProgressIssues;
    }

    public Double getInProgressStoryPoints() {
        return inProgressStoryPoints;
    }

    public void setInProgressStoryPoints(Double inProgressStoryPoints) {
        this.inProgressStoryPoints = inProgressStoryPoints;
    }

    public List<String> getImplementedKeys() {
        return implementedKeys;
    }

    public void setImplementedKeys(List<String> implementedKeys) {
        this.implementedKeys = implementedKeys;
    }

    public List<String> getFinishedKeys() {
        return finishedKeys;
    }

    public void setFinishedKeys(List<String> finishedKeys) {
        this.finishedKeys = finishedKeys;
    }

    public List<String> getInProgressKeys() {
        return inProgressKeys;
    }

    public void setInProgressKeys(List<String> inProgressKeys) {
        this.inProgressKeys = inProgressKeys;
    }

    public Integer getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(Integer minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public Integer getNumberOfSubIssues() {
        return numberOfSubIssues;
    }

    public void setNumberOfSubIssues(Integer numberOfSubIssues) {
        this.numberOfSubIssues = numberOfSubIssues;
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

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(Integer closedIssues) {
        this.closedIssues = closedIssues;
    }

    public Double getClosedStoryPoints() {
        return closedStoryPoints;
    }

    public void setClosedStoryPoints(Double closedStoryPoints) {
        this.closedStoryPoints = closedStoryPoints;
    }

    public List<String> getClosedKeys() {
        return closedKeys;
    }

    public void setClosedKeys(List<String> closedKeys) {
        this.closedKeys = closedKeys;
    }

    public FlaggingStatistics getFlaggingStatistics() {
        return flaggingStatistics;
    }

    public void setFlaggingStatistics(FlaggingStatistics flaggingStatistics) {
        this.flaggingStatistics = flaggingStatistics;
    }

    public Integer getWaitingIssues() {
        return waitingIssues;
    }

    public void setWaitingIssues(Integer waitingIssues) {
        this.waitingIssues = waitingIssues;
    }

    public Double getWaitingStoryPoints() {
        return waitingStoryPoints;
    }

    public void setWaitingStoryPoints(Double waitingStoryPoints) {
        this.waitingStoryPoints = waitingStoryPoints;
    }

    public List<String> getWaitingKeys() {
        return waitingKeys;
    }

    public void setWaitingKeys(List<String> waitingKeys) {
        this.waitingKeys = waitingKeys;
    }

    @Override
    public String toString() {
        return "IssueStatistics [numberOfIssues=" + numberOfIssues + ", numberOfSubIssues=" + numberOfSubIssues
                + ", numberOfStoryPoints=" + numberOfStoryPoints + ", plannedDeliveredIssues=" + plannedDeliveredIssues
                + ", plannedDeliveredStoryPoints=" + plannedDeliveredStoryPoints + ", reopenCount=" + reopenCount
                + ", minutesSpent=" + minutesSpent + ", inProgressIssues=" + inProgressIssues + ", waitingIssues="
                + waitingIssues + ", implementedIssues=" + implementedIssues + ", finishedIssues=" + finishedIssues
                + ", closedIssues=" + closedIssues + ", inProgressStoryPoints=" + inProgressStoryPoints
                + ", waitingStoryPoints=" + waitingStoryPoints + ", implementedStoryPoints=" + implementedStoryPoints
                + ", finishedStoryPoints=" + finishedStoryPoints + ", closedStoryPoints=" + closedStoryPoints
                + ", inProgressKeys=" + inProgressKeys + ", waitingKeys=" + waitingKeys + ", implementedKeys="
                + implementedKeys + ", finishedKeys=" + finishedKeys + ", closedKeys=" + closedKeys
                + ", flaggingStatistics=" + flaggingStatistics + "]";
    }

}
